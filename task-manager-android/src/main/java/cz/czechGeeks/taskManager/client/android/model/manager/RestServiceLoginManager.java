package cz.czechGeeks.taskManager.client.android.model.manager;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.model.ErrorMessage;
import cz.czechGeeks.taskManager.client.android.model.LoginModel;
import cz.czechGeeks.taskManager.client.android.util.ProgressDialogUtil;

public class RestServiceLoginManager implements LoginManager {

	private static final int STATUS_CODE_OK = 200;
	private static final int STATUS_CODE_SYSTEM_ERROR = 500;
	private static final int STATUS_CODE_NOT_AUTHORIZED = 401;

	private static final String LOG_TAG = "RestServiceLoginManager";

	private final Context context;
	private final ProgressDialog dialog;

	private final String baseUrl;

	public RestServiceLoginManager(Context context) {
		this.context = context;
		this.dialog = ProgressDialogUtil.create(context);

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		this.baseUrl = preferences.getString(context.getString(R.string.app_settings_rest_server_url_key), null) + "/Login";
	}

	@Override
	public void signIn(final String userName, final String password, final AsyncTaskCallBack<LoginModel> callBack) {
		ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();

		final String URL = baseUrl;
		final String REQUEST_METHOD = "GET";
		final String USER_NAME = userName;
		final String PASSWORD = password;

		Log.d(LOG_TAG, "Pripojeni k URL: " + URL);
		Log.d(LOG_TAG, "Metoda: " + REQUEST_METHOD);
		Log.d(LOG_TAG, "Uzivatelske jmeno: " + USER_NAME);
		Log.d(LOG_TAG, "Heslo: " + PASSWORD);

		if (networkInfo != null && networkInfo.isConnected()) {
			dialog.show();

			new Thread() {
				public void run() {
					LoginModel signInModel = null;// Navratova hodnota
					ErrorMessage errorMessage = null;// Chyba

					HttpURLConnection connection = null;

					try {
						URL url = new URL(URL);

						connection = (HttpURLConnection) url.openConnection();
						connection.setRequestMethod(REQUEST_METHOD);
						connection.setRequestProperty("Accept", "application/json");
						connection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString((USER_NAME + ":" + PASSWORD).getBytes(), Base64.NO_WRAP));

						connection.connect();

						int responseCode = connection.getResponseCode();
						Log.i(LOG_TAG, "Kod odpovedi:" + responseCode);

						if (responseCode == STATUS_CODE_OK) {
							signInModel = new ObjectMapper().readValue(connection.getInputStream(), LoginModel.class);
							Log.i(LOG_TAG, "Uzivatel prihlasen");

						} else if (responseCode == STATUS_CODE_NOT_AUTHORIZED) {
							errorMessage = new ErrorMessage(context.getString(R.string.signIn_error_noValid));
							Log.e(LOG_TAG, "Neplatne uzivatelske jmeno nebo heslo!");

						} else if (responseCode == STATUS_CODE_SYSTEM_ERROR) {
							errorMessage = new ObjectMapper().readValue(connection.getInputStream(), ErrorMessage.class);
							Log.e(LOG_TAG, "Systemova chyba: " + errorMessage.getMessage());
						}

					} catch (final Exception e) {
						errorMessage = new ErrorMessage(e.getLocalizedMessage());
						Log.e(LOG_TAG, "Systemova chyba: " + errorMessage.getMessage());
					} finally {
						if (connection != null) {
							connection.disconnect();
						}
					}

					final LoginModel retValue = signInModel;
					final ErrorMessage errorMessageValue = errorMessage;

					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							dialog.dismiss();
							if (errorMessageValue != null) {
								callBack.onError(errorMessageValue);
							} else {
								callBack.onSuccess(retValue);
							}
						}
					});
				}
			}.start();
		} else {
			callBack.onError(new ErrorMessage(context.getString(R.string.connection_error_noEnabled)));
		}
	}

	@Override
	public LoginModel get(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LoginModel> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
