package cz.czechGeeks.taskManager.client.android.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.adapter.TaskListAdapter;
import cz.czechGeeks.taskManager.client.android.factory.LoginManagerFactory;
import cz.czechGeeks.taskManager.client.android.factory.TaskManagerFactory;
import cz.czechGeeks.taskManager.client.android.fragment.SignInDialogFragment;
import cz.czechGeeks.taskManager.client.android.fragment.SignInDialogFragment.SignInDialogFragmentCallBack;
import cz.czechGeeks.taskManager.client.android.fragment.TaskListFragment;
import cz.czechGeeks.taskManager.client.android.fragment.TaskListFragment.TaskListFragmentCallBack;
import cz.czechGeeks.taskManager.client.android.model.ErrorMessage;
import cz.czechGeeks.taskManager.client.android.model.LoginModel;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;
import cz.czechGeeks.taskManager.client.android.model.manager.AsyncTaskCallBack;
import cz.czechGeeks.taskManager.client.android.model.manager.LoginManager;
import cz.czechGeeks.taskManager.client.android.model.manager.TaskManager;
import cz.czechGeeks.taskManager.client.android.util.LoginUtils;
import cz.czechGeeks.taskManager.client.android.util.ModelActionType;
import cz.czechGeeks.taskManager.client.android.util.StorageAndPreferencesUtils;
import cz.czechGeeks.taskManager.client.android.util.StorageAndPreferencesUtils.ConnectionItems;
import cz.czechGeeks.taskManager.client.android.util.TaskType;

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

		private TaskListFragment mainFragment;
		private TaskListFragment toMeFragment;
		private TaskListFragment toOthersFragment;

		public MainActivityPagerAdapter(FragmentManager fm) {
			super(fm);

			mainFragment = new TaskListFragment();
			mainFragment.setTaskType(TaskType.MAIN);

			toMeFragment = new TaskListFragment();
			toMeFragment.setTaskType(TaskType.DELEGATED_TO_ME);

			toOthersFragment = new TaskListFragment();
			toOthersFragment.setTaskType(TaskType.DELEGATED_TO_OTHERS);
		}

		@Override
		public TaskListFragment getItem(int position) {
			switch (position) {
			case 0:
				return mainFragment;
			case 1:
				return toMeFragment;
			case 2:
				return toOthersFragment;
			default:
				throw new IllegalStateException("Nedefinovany fragment");
			}
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
	private static final int RESULT_TASK_DETAIL = 2;
	private static final int RESULT_TASKCATEG_LIST = 3;

	private static final String LOG_TAG = "MainActivity";
	private static final int NOTIFY_ID = 0;

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
		ConnectionItems connectionItems = StorageAndPreferencesUtils.getConnectionItems(this);
		String userName = connectionItems.USER_NAME;
		String password = connectionItems.PASSWORD;
		Log.i(LOG_TAG, "Pokus o prihlaseni uzivatele " + userName);

		if (userName == null || userName.isEmpty() || password == null || password.isEmpty()) {
			SignInDialogFragment newFragment = new SignInDialogFragment();
			newFragment.show(getSupportFragmentManager(), "signInDialog");
			return;
		}

		LoginManager loginManager = LoginManagerFactory.get(this);
		loginManager.signIn(userName, password, new AsyncTaskCallBack<LoginModel>() {

			@Override
			public void onSuccess(LoginModel resumeObject) {
				String signedUserName = getResources().getString(R.string.signedUser) + resumeObject.getName();
				Log.i(LOG_TAG, "Podarilo se prihlasit. ID uzivatele:" + resumeObject.getId() + ", uzivatelske jmeno:" + resumeObject.getName());
				Toast.makeText(getApplicationContext(), signedUserName, Toast.LENGTH_SHORT).show();

				// Ulozeni prihlaseneho uzivatele do pameti
				LoginUtils.get().setLoggedUser(resumeObject);

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

				// kontrola jestli uzivatel ma nejake nove zalozene aktivity
				checkNewDelegatedTasksToMe();
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
		getMenuInflater().inflate(R.menu.main, menu);
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_settings:
			// Zobrazeni nastaveni
			Log.d(LOG_TAG, "Klik na polozku menu nastaveni");
			startActivityForResult(new Intent(this, SettingsActivity.class), RESULT_SETTINGS);
			break;

		case R.id.menu_newTask:
			// zalozeni noveho tasku
			Log.d(LOG_TAG, "Klik na polozku pridani ukolu");
			TaskType taskType = pagerAdapter.getItem(viewPager.getCurrentItem()).getTaskType();

			if (taskType == TaskType.DELEGATED_TO_ME) {
				// jsem na zalozce tasku ktere jsou delegovane pro me - zde nemohu zakladat
				Toast.makeText(getApplicationContext(), R.string.error_taskCantCreateTaskDelegatedToMe, Toast.LENGTH_SHORT).show();
				return true;
			}

			TaskModel model = new TaskModel();
			Intent intent = new Intent(this, TaskDetailActivity.class);
			intent.putExtra(TaskDetailActivity.TASK_TYPE, taskType);
			intent.putExtra(TaskDetailActivity.TASK_MODEL, model);
			startActivityForResult(intent, RESULT_TASK_DETAIL);
			break;

		case R.id.menu_taskCategList:
			// zobrazeni seznamu kategorii
			startActivityForResult(new Intent(this, TaskCategListActivity.class), RESULT_TASKCATEG_LIST);
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
		TaskType taskType = pagerAdapter.getItem(viewPager.getCurrentItem()).getTaskType();

		Intent intent = new Intent(getApplicationContext(), TaskDetailActivity.class);
		intent.putExtra(TaskDetailActivity.TASK_TYPE, taskType);
		intent.putExtra(TaskDetailActivity.TASK_MODEL, model);
		startActivityForResult(intent, RESULT_TASK_DETAIL);// zobrazim detail
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// doslo k ukonceni nejake aktivity ktera byla vyvolana touto aktivitou
		// jako napriklad uzavreni nastavani, uzavreni detailu nebo seznamu kategorii
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_TASK_DETAIL) {
			if (resultCode == RESULT_OK) {
				// doslo k uzavreni detailu tasku

				// typ akce ktera byla provedena nad ukolem
				ModelActionType actionType = (ModelActionType) data.getExtras().getSerializable(TaskDetailActivity.TASK_MODEL_ACTION_TYPE);

				// akce se vztahuje k modelu
				TaskModel model = (TaskModel) data.getExtras().getSerializable(TaskDetailActivity.TASK_MODEL);

				// provedeni uprav v seznamu
				TaskListFragment listFragmen = pagerAdapter.getItem(viewPager.getCurrentItem());
				TaskListAdapter taskListAdapter = (TaskListAdapter) listFragmen.getListAdapter();
				if (actionType == ModelActionType.UPDATE) {
					// doslo k aktualizaci
					int position = taskListAdapter.getPosition(model);
					taskListAdapter.remove(model);// Odstrani ho na zaklade ID
					taskListAdapter.insert(model, position < 0 ? 0 : position);
					taskListAdapter.notifyDataSetChanged();
				} else if (actionType == ModelActionType.DELETE) {
					// task byl odstranen
					taskListAdapter.remove(model);// Odstrani ho na zaklade ID
					taskListAdapter.notifyDataSetChanged();
				}
			}
		}
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

	/**
	 * Kontrola jestli uzivatel nema nejake nove delegovane tasky. ID posledniho tasku se nacita z privatni datastorage
	 */
	private void checkNewDelegatedTasksToMe() {
		// nacteni posledniho delegovaneho ID tasku
		Long loadedTaskIdWhitchIsDelegatedToMe = StorageAndPreferencesUtils.getLastLoadedTaskIdWhitchIsDelegatedToMe(getApplication());

		// nacteni tasku zalozenych od tohoto
		TaskManager taskManager = TaskManagerFactory.get(this);
		taskManager.getAllDelegatedToMe(loadedTaskIdWhitchIsDelegatedToMe, new AsyncTaskCallBack<TaskModel[]>() {

			@Override
			public void onSuccess(TaskModel[] resumeObject) {
				long lastId = 0;
				for (TaskModel item : resumeObject) {
					if (lastId < item.getId().longValue()) {
						lastId = item.getId().longValue();
					}
				}

				// ulozeni noveho posledniho delegovaneho tasku ID
				StorageAndPreferencesUtils.setLastLoadedTaskIdWhitchIsDelegatedToMe(lastId, getApplicationContext());

				if (resumeObject.length > 0) {
					// mam nejake nove tasky - zobrazeni notifikace
					createNotification(resumeObject);
				}
			}

			@Override
			public void onError(ErrorMessage message) {
				Log.e(LOG_TAG, "Nacitani novych delegovanych ukolu se nezdarilo:" + message.getMessage());
				Toast.makeText(getApplicationContext(), R.string.error_taskLastLoadedWhitchIsDelegatedToMe, Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * Zobrazeni notifikace pro nove delegovane tasky
	 * 
	 * @param resumeObject
	 */
	private void createNotification(TaskModel[] resumeObject) {
		StringBuilder contentText = new StringBuilder();// seznam nazvu novych tasku
		for (TaskModel taskModel : resumeObject) {
			if (contentText.length() > 0) {
				contentText.append(", ");
			}
			contentText.append(taskModel.getName());
		}

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setAutoCancel(true);
		mBuilder.setDefaults(Notification.DEFAULT_ALL);
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setContentTitle(getString(R.string.task_newDelegatedToMe));
		mBuilder.setContentText(contentText.toString());
		mBuilder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0));

		NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNotifyMgr.notify(NOTIFY_ID, mBuilder.getNotification());
	}

}
