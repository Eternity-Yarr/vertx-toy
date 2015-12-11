package ru.yarr;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;

public class HttpSenderVerticle extends AbstractVerticle {
	private HttpClient httpClient;

	private void sendRequest(Message<Object> body) {
		httpClient.get((String)body.body()).handler((x) -> {
			x.bodyHandler(
					System.out::print
			);
		}).end();
	}

	@Override
	public void start() throws Exception {
		httpClient = vertx.createHttpClient(
				new HttpClientOptions()
						.setMaxPoolSize(100)
						.setDefaultHost("192.168.1.131")
						.setDefaultPort(8080)
						.setKeepAlive(true)
		);
		vertx.eventBus().localConsumer("send").handler(this::sendRequest);
	}
}
