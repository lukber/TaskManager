package cz.czechGeeks.taskManager.client.android.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.fragment.TaskListFragment;
import cz.czechGeeks.taskManager.client.android.fragment.TaskListFragment.TaskListFragmentCallBack;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;

/**
 * Hlavni obrazovka reprezentovana tabem a seznamem
 * 
 * @author lukasb
 * 
 */
public class MainActivity extends FragmentActivity implements TabListener, TaskListFragmentCallBack {

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

	private MainActivityPagerAdapter pagerAdapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);

		pagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager());

		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When swiping between different app sections, select the corresponding tab.
				// We can also use ActionBar.Tab#select() to do this if we have a reference to the
				// Tab.
				actionBar.setSelectedNavigationItem(position);
			}
		});

		for (int i = 0; i < pagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(pagerAdapter.getPageTitle(i)).setTabListener(this));
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTaskListItemSelected(TaskModel model) {
		Intent intent = new Intent(getApplicationContext(), TaskPreviewDetailActivity.class);
		intent.putExtra(TaskPreviewDetailActivity.TASK_MODEL, model);
		startActivity(intent);
	}

}
