package cz.czechGeeks.taskManager.client.android.service;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.to.TaskCategTO;

public class TaskCategService {

	private final Context context;
	private String url;
	private String userName;
	private String password;

	public TaskCategService(Context context) {
		this.context = context;
		intialize();
	}

	private void intialize() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		url = preferences.getString(context.getString(R.string.settings_screen_serverCateg_url_key), null);
		userName = preferences.getString(context.getString(R.string.settings_screen_loginCateg_usrName_key), null);
		password = preferences.getString(context.getString(R.string.settings_screen_loginCateg_usrPass_key), null);
	}

	public TaskCategTO getCateg(long categId) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
		return restTemplate.getForObject(url + "TaskCateg/{id}", TaskCategTO.class, categId);
	}

}
