package ru.yarr;

import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.impl.BodyReadStream;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.streams.Pump;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class VertxToyApplication extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx(
                new VertxOptions()
                        .setWorkerPoolSize(100)
                        .setMetricsOptions(
                                new DropwizardMetricsOptions()
                                        .setJmxEnabled(true)
                                        .setEnabled(true)
                        )
        );

        vertx.createHttpServer()
                .requestHandler(
                        event -> event
                                .response()
                                .putHeader("Content-Type", "application/xml; charset=utf-8")
                                .putHeader("Content-Length", "42")
                                .end("<?xml version=\"1.0\"?><result>ok</result>"))
                .listen(8080);

        vertx.deployVerticle(
                "ru.yarr.HttpSenderVerticle",
                new DeploymentOptions().setInstances(50).setWorker(true)
        );

        vertx.deployVerticle(new VertxToyApplication(), event -> {
            if(event.succeeded()) {
                System.out.println("WEee!");
                vertx.executeBlocking(
                        event1 -> {
                            for (int i = 0; i <= 500000; i++) {
                                if (i % 100000 == 0) System.out.println(i);
                                vertx.eventBus().send("blah", "blah");

                            }
                        }, event1 -> System.out.println("2"));
            }
        });
    }
}
