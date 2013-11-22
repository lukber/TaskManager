package cz.czechGeeks.taskManager.client.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import cz.czechGeeks.taskManager.client.android.R;

public class PreferencesUtils {

	public static class ConnectionItems {
		public String BASE_URL;
		public String USER_NAME;
		public String PASSWORD;
	}

	public static ConnectionItems getConnectionItems(Context context) {
		ConnectionItems connectionItems = new ConnectionItems();

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		connectionItems.BASE_URL = preferences.getString(context.getString(R.string.app_settings_rest_server_url_key), null);
		connectionItems.USER_NAME = preferences.getString(context.getString(R.string.app_settings_userName_key), null);
		connectionItems.PASSWORD = preferences.getString(context.getString(R.string.app_settings_password_key), null);

		return connectionItems;
	}

	public static void saveConnectionItems(ConnectionItems connectionItems, Context context) {
		Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

		editor.putString(context.getString(R.string.app_settings_rest_server_url_key), connectionItems.BASE_URL);
		editor.putString(context.getString(R.string.app_settings_userName_key), connectionItems.USER_NAME);
		editor.putString(context.getString(R.string.app_settings_password_key), connectionItems.PASSWORD);

		editor.commit();
	}

}
