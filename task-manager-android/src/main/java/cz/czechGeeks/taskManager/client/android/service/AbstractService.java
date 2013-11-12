package cz.czechGeeks.taskManager.client.android.service;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import cz.czechGeeks.taskManager.client.android.exception.UnauthorizedException;

abstract class AbstractService {

	private static String url = "http://10.0.2.2:8080/task-manager-war/rest";
	private static String userName;
	private static String password;

	public static void setUrl(String url) {
		AbstractService.url = url;
	}

	public static String getUrl() {
		return url;
	}

	public static void setUserName(String userName) {
		AbstractService.userName = userName;
	}

	public static String getUserName() {
		return userName;
	}

	public static void setPassword(String password) {
		AbstractService.password = password;
	}

	public static String getPassword() {
		return password;
	}

	// private void intialize() throws ConnectionSettingsNotDefined {
	// SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
	// url = preferences.getString(context.getString(R.string.settings_screen_serverCateg_url_key), null);
	// userName = preferences.getString(context.getString(R.string.settings_screen_loginCateg_usrName_key), null);
	// password = preferences.getString(context.getString(R.string.settings_screen_loginCateg_usrPass_key), null);
	//
	// if (url == null || userName == null || password == null) {
	// throw new ConnectionSettingsNotDefined();
	// }
	// }

	protected static Object callRestService(String urlPostFix, HttpMethod httpMetod, Class<?> responseType, Object... uriVariables) throws UnauthorizedException {
		final String URL = url + urlPostFix;

		HttpAuthentication authHeader = new HttpBasicAuthentication(userName, password);
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAuthorization(authHeader);
		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

		try {
			ResponseEntity<?> response = restTemplate.exchange(URL, httpMetod, requestEntity, responseType, uriVariables);
			return response.getBody();
		} catch (HttpClientErrorException e) {
			throw new UnauthorizedException();
		}
	}

}
