package cz.czechGeeks.taskManager.client.android.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;
import cz.czechGeeks.taskManager.client.android.R;

/**
 * Utilita pro praci s nastavenim a datastorage
 * 
 * @author lukasb
 * 
 */
public class StorageAndPreferencesUtils {

	private static final String LAST_LOADED_TASK_ID_FILE_NAME_PREFIX = "LastLoadedTaskId";

	public static class ConnectionItems {
		public String BASE_URL;
		public String USER_NAME;
		public String PASSWORD;
	}

	/**
	 * Vraci udaje potrebne k navazani spojeni se serverem
	 * 
	 * @param context
	 * @return
	 */
	public static ConnectionItems getConnectionItems(Context context) {
		ConnectionItems connectionItems = new ConnectionItems();

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		connectionItems.BASE_URL = preferences.getString(context.getString(R.string.app_settings_rest_server_url_key), "http://10.0.2.2:8080/task-manager-war/rest");
		connectionItems.USER_NAME = preferences.getString(context.getString(R.string.app_settings_userName_key), null);
		connectionItems.PASSWORD = preferences.getString(context.getString(R.string.app_settings_password_key), null);

		return connectionItems;
	}

	/**
	 * Ulozeni hodnot do datastorage
	 * 
	 * @param connectionItems
	 * @param context
	 */
	public static void saveConnectionItems(ConnectionItems connectionItems, Context context) {
		Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

		editor.putString(context.getString(R.string.app_settings_rest_server_url_key), connectionItems.BASE_URL);
		editor.putString(context.getString(R.string.app_settings_userName_key), connectionItems.USER_NAME);
		editor.putString(context.getString(R.string.app_settings_password_key), connectionItems.PASSWORD);

		editor.commit();
	}

	/**
	 * Vrati ID posledniho ukolu ktery byl delegovan na me
	 * 
	 * @param context
	 * @return
	 */
	public static Long getLastLoadedTaskIdWhitchIsDelegatedToMe(Context context) {
		FileInputStream inputStream = null;
		try {
			String fileName = LAST_LOADED_TASK_ID_FILE_NAME_PREFIX + "_" + LoginUtils.get().getLoggedUserName();
			inputStream = context.openFileInput(fileName);
			DataInputStream dis = new DataInputStream(inputStream);
			return dis.readLong();
		} catch (Exception e) {
			Log.e("StorageAndPreferencesUtils.getLastLoadedTaskIdWhitchIsDelegatedToMe", e.getMessage(), e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					Log.e("StorageAndPreferencesUtils.getLastLoadedTaskIdWhitchIsDelegatedToMe", e.getMessage(), e);
				}
			}
		}

		return null;
	}

	/**
	 * Ulozeni novych hodnot do datastorage
	 * 
	 * @param taskId
	 * @param context
	 * @return
	 */
	public static boolean setLastLoadedTaskIdWhitchIsDelegatedToMe(Long taskId, Context context) {
		FileOutputStream outputStream = null;
		try {
			String fileName = LAST_LOADED_TASK_ID_FILE_NAME_PREFIX + "_" + LoginUtils.get().getLoggedUserName();
			outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			DataOutputStream dos = new DataOutputStream(outputStream);
			dos.writeLong(taskId);
			return true;
		} catch (Exception e) {
			Log.e("StorageAndPreferencesUtils.getLastLoadedTaskIdWhitchIsDelegatedToMe", e.getMessage(), e);
			return false;
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					Log.e("StorageAndPreferencesUtils.getLastLoadedTaskIdWhitchIsDelegatedToMe", e.getMessage(), e);
				}
			}
		}
	}

}
