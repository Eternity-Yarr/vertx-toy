package ru.yarr;

import io.vertx.core.*;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VertxToyApplication {

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

        vertx.deployVerticle(
                "ru.yarr.HttpSenderVerticle",
                new DeploymentOptions().setInstances(50).setWorker(true),
                v -> vertx.deployVerticle(new WorkCreator())
        );
    }
}
