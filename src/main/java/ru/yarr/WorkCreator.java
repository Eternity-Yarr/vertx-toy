package ru.yarr;

import io.vertx.core.AbstractVerticle;

public class WorkCreator extends AbstractVerticle {

	@Override
	public void start() throws Exception {
		super.start();
		for (int i =0; i< 50000; i++)
			vertx.eventBus().send("send", "blah");
	}
}
