package cz.czechGeeks.taskManager.client.android.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.factory.LoginManagerFactory;
import cz.czechGeeks.taskManager.client.android.fragment.SignInDialogFragment;
import cz.czechGeeks.taskManager.client.android.fragment.SignInDialogFragment.SignInDialogFragmentCallBack;
import cz.czechGeeks.taskManager.client.android.fragment.TaskListFragment;
import cz.czechGeeks.taskManager.client.android.fragment.TaskListFragment.TaskListFragmentCallBack;
import cz.czechGeeks.taskManager.client.android.model.ErrorMessage;
import cz.czechGeeks.taskManager.client.android.model.LoginModel;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;
import cz.czechGeeks.taskManager.client.android.model.manager.AsyncTaskCallBack;
import cz.czechGeeks.taskManager.client.android.model.manager.LoginManager;
import cz.czechGeeks.taskManager.client.android.util.LoginUtil;

/**
 * Hlavni obrazovka reprezentovana tabem a seznamem
 * 
 * @author lukasb
 * 
 */
public class MainActivity extends FragmentActivity implements TabListener, TaskListFragmentCallBack, SignInDialogFragmentCallBack {

	/**
	 * Pager adapter obsahujuci fragmenty tasku pro jednotlive taby actionbaru
	 * 
	 * @author lukasb
	 * 
	 */
	private class MainActivityPagerAdapter extends FragmentPagerAdapter {

		public MainActivityPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return new TaskListFragment();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getResources().getString(R.string.main_tasks);
			case 1:
				return getResources().getString(R.string.toMe_tasks);
			case 2:
				return getResources().getString(R.string.toOthers_tasks);
			default:
				return "PAGE " + position;
			}
		}

		@Override
		public int getCount() {
			return 3;
		}

	}

	private static final int RESULT_SETTINGS = 1;
	private static final String LOG_TAG = "MainActivity";

	private MainActivityPagerAdapter pagerAdapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);

		pagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager());
		viewPager = (ViewPager) findViewById(R.id.pager);

		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		performSignIn();
	}

	/**
	 * Metoda pro prihlaseni. Pokud se prihlaseni nezdari tak se vyvola prihlasovaci dialog
	 */
	private void performSignIn() {
		// Nacteni hodnot pro prihlaseni z preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String userName = preferences.getString(getString(R.string.app_settings_userName_key), null);
		String password = preferences.getString(getString(R.string.app_settings_password_key), null);
		Log.i(LOG_TAG, "Pokus o prihlaseni uzivatele " + userName);

		LoginManager loginManager = LoginManagerFactory.get(this);
		loginManager.signIn(userName, password, new AsyncTaskCallBack<LoginModel>() {

			@Override
			public void onSuccess(LoginModel resumeObject) {
				String signedUserName = getResources().getString(R.string.signedUser) + resumeObject.getName();
				Log.i(LOG_TAG, "Podarilo se prihlasit. ID uzivatele:" + resumeObject.getId() + ", uzivatelske jmeno:" + resumeObject.getName());
				Toast.makeText(getApplicationContext(), signedUserName, Toast.LENGTH_SHORT).show();

				// Ulozeni prihlaseneho uzivatele do pameti
				LoginUtil.get().setLoggedUser(resumeObject);

				// Zobrazeni obsahu
				viewPager.setAdapter(pagerAdapter);
				viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						getActionBar().setSelectedNavigationItem(position);
					}
				});

				for (int i = 0; i < pagerAdapter.getCount(); i++) {
					getActionBar().addTab(getActionBar().newTab().setText(pagerAdapter.getPageTitle(i)).setTabListener(MainActivity.this));
				}
			}

			@Override
			public void onError(ErrorMessage message) {
				// Nepodarilo se prihlasit
				Log.e(LOG_TAG, "Nepodarilo se prihlasit");
				Toast.makeText(getApplicationContext(), message.getMessage(), Toast.LENGTH_SHORT).show();

				// Zobrazeni prihlasovaciho dialogu
				SignInDialogFragment newFragment = new SignInDialogFragment();
				newFragment.show(getSupportFragmentManager(), "signInDialog");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_settings:
			// Zobrazeni nastaveni
			Log.d(LOG_TAG, "Klik na polozku menu nastaveni");
			Intent i = new Intent(this, SettingsActivity.class);
			startActivityForResult(i, RESULT_SETTINGS);
			break;
		}

		return true;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
		// Zobrazeni odpovidajiciho fragmentu
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTaskListItemSelected(TaskModel model) {
		// Byla vybrana polozka ze seznamu
		Log.i(LOG_TAG, "Byla vybrana polozka ze seznamu:" + model.getId());
		Intent intent = new Intent(getApplicationContext(), TaskDetailActivity.class);
		intent.putExtra(TaskDetailActivity.TASK_MODEL, model);
		startActivity(intent);
	}

	@Override
	public void onSignInDialogResultOk() {
		Log.d(LOG_TAG, "Na prihlasovacim formulaci bylo stisknuto tlacitko prihlasit");
		performSignIn();
	}

	@Override
	public void onSignInDialogResulCancel() {
		Log.w(LOG_TAG, "Na prihlasovacim formulaci bylo stisknuto tlacitko storno. Ukoncuji aplikaci");
		finish();
	}

}
