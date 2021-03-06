package cz.czechGeeks.taskManager.client.android.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import cz.czechGeeks.taskManager.client.android.R;

/**
 * Reprezentuje akci pro nastaveni
 * 
 * @author lukasb
 * 
 */
public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
	}

	public static class MyPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
		}
	}
}
