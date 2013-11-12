package cz.czechGeeks.taskManager.client.android.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import cz.czechGeeks.taskManager.client.android.R;

public class UserSettingsActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_screen);
	}
}
