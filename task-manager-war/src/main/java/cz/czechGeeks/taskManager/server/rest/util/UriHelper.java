package cz.czechGeeks.taskManager.server.rest.util;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

public class UriHelper {

	private UriHelper() {
	}

	public static URI createUriWithId(URI fromUri, Long id) {
		UriBuilder builder = UriBuilder.fromUri(fromUri);
		builder.path("{id}");
		return builder.build(id);
	}
}
