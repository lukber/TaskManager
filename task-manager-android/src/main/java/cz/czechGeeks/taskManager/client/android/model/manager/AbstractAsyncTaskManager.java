package cz.czechGeeks.taskManager.client.android.model.manager;

import java.net.HttpURLConnection;
import java.net.URL;

import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.model.ErrorMessage;
import cz.czechGeeks.taskManager.client.android.util.ProgressDialogUtils;
import cz.czechGeeks.taskManager.client.android.util.StorageAndPreferencesUtils;
import cz.czechGeeks.taskManager.client.android.util.StorageAndPreferencesUtils.ConnectionItems;

public abstract class AbstractAsyncTaskManager {

	public enum RequestMethod {
		GET, POST, PUT, DELETE
	}

	public static final int STATUS_CODE_OK = 200;
	public static final int STATUS_CODE_CREATED = 201;
	public static final int STATUS_CODE_SYSTEM_ERROR = 500;
	public static final int STATUS_CODE_NOT_AUTHORIZED = 401;

	private static final String LOG_TAG = "AsyncTaskManager";

	private final Context context;
	private final ProgressDialog dialog;

	public AbstractAsyncTaskManager(Context context) {
		this.context = context;
		this.dialog = ProgressDialogUtils.create(context);
	}

	protected Context getContext() {
		return context;
	}

	protected <T> void run(String baseUrlPostFix, RequestMethod requestMethod, final Class<T> returnValueClass, final AsyncTaskCallBack<T> callBack) {
		run(baseUrlPostFix, requestMethod, null, returnValueClass, callBack);
	}

	protected <T> void run(String baseUrlPostFix, RequestMethod requestMethod, final Object REQUEST_VALUE, final Class<T> returnValueClass, final AsyncTaskCallBack<T> callBack) {
		ConnectionItems connectionItems = StorageAndPreferencesUtils.getConnectionItems(context);

		final String URL = connectionItems.BASE_URL + baseUrlPostFix;
		final String USER_NAME = connectionItems.USER_NAME;
		final String PASSWORD = connectionItems.PASSWORD;
		final RequestMethod REQUEST_METHOD = requestMethod;

		run(URL, USER_NAME, PASSWORD, REQUEST_METHOD, REQUEST_VALUE, returnValueClass, callBack);
	}

	protected <T> void run(final String URL, final String USER_NAME, final String PASSWORD, final RequestMethod REQUEST_METHOD, final Class<T> returnValueClass, final AsyncTaskCallBack<T> callBack) {
		run(URL, USER_NAME, PASSWORD, REQUEST_METHOD, null, returnValueClass, callBack);
	}

	private <T> void run(final String URL, final String USER_NAME, final String PASSWORD, final RequestMethod REQUEST_METHOD, final Object REQUEST_VALUE, final Class<T> returnValueClass, final AsyncTaskCallBack<T> callBack) {
		Log.d(LOG_TAG, "Pripojeni k URL: " + URL);
		Log.d(LOG_TAG, "Metoda: " + REQUEST_METHOD);
		Log.d(LOG_TAG, "Uzivatelske jmeno: " + USER_NAME);
		Log.d(LOG_TAG, "Heslo: " + PASSWORD);

		ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			dialog.show();

			new Thread() {
				public void run() {
					T returnValue = null;// Navratova hodnota
					ErrorMessage errorMessage = null;// Chyba

					HttpURLConnection connection = null;

					try {
						URL url = new URL(URL);

						connection = (HttpURLConnection) url.openConnection();
						connection.setConnectTimeout(30000);
						connection.setRequestMethod(REQUEST_METHOD.name());
						connection.setRequestProperty("Accept", "application/json");
						connection.setRequestProperty("Content-Type", "application/json");
						connection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString((USER_NAME + ":" + PASSWORD).getBytes(), Base64.NO_WRAP));

						if (REQUEST_VALUE != null) {
							new ObjectMapper().writeValue(connection.getOutputStream(), REQUEST_VALUE);
						}

						connection.connect();

						int responseCode = connection.getResponseCode();
						Log.i(LOG_TAG, "Kod odpovedi:" + responseCode);

						if (responseCode == STATUS_CODE_OK || responseCode == STATUS_CODE_CREATED) {
							if (REQUEST_METHOD != RequestMethod.DELETE) {
								returnValue = new ObjectMapper().readValue(connection.getInputStream(), returnValueClass);
							}
							Log.i(LOG_TAG, "Odpoved ze serveru OK.");

						} else if (responseCode == STATUS_CODE_NOT_AUTHORIZED) {
							errorMessage = new ErrorMessage(context.getString(R.string.error_noValidUserNameOrPass));
							Log.e(LOG_TAG, "Neplatne uzivatelske jmeno nebo heslo!");

						} else if (responseCode == STATUS_CODE_SYSTEM_ERROR) {
							errorMessage = new ObjectMapper().readValue(connection.getErrorStream(), ErrorMessage.class);
							Log.e(LOG_TAG, "Systemova chyba: " + errorMessage.getMessage());
						} else {
							throw new IllegalStateException("Nedefinovany kod vystupu: " + responseCode);
						}

					} catch (final Exception e) {
						errorMessage = new ErrorMessage(e.getMessage());
						Log.e(LOG_TAG, "Systemova chyba: " + errorMessage.getMessage(), e);
					} finally {
						if (connection != null) {
							connection.disconnect();
						}
					}

					final T retValue = returnValue;
					final ErrorMessage returnErrorMessage = errorMessage;

					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							dialog.dismiss();
							if (returnErrorMessage == null) {
								callBack.onSuccess(retValue);
							} else {
								// nastala chyba
								callBack.onError(returnErrorMessage);
							}
						}
					});
				}
			}.start();
		} else {
			callBack.onError(new ErrorMessage(context.getString(R.string.error_connection)));
		}
	}

}
