package cz.czechGeeks.taskManager.client.android.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import cz.czechGeeks.taskManager.client.android.R;

/**
 * Reprezentuje akci listu kategorii
 * 
 * @author lukasb
 * 
 */
public class TaskCategListActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle args) {
		super.onCreate(args);
		setContentView(R.layout.activity_task_categ_list);

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);// zobrazeni tlacitka zpet
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			// stiskl jsem tlacitko zpet na hlavnim panelu
			setResult(RESULT_OK);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}