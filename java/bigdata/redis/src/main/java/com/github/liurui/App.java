package com.github.liurui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.UUID;

/**
 * Hello world!
 *
 * @author liurui
 */
@SpringBootApplication
public class App {
    public static void main(String[] args) throws InterruptedException, IOException {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);

        for (int i = 0; i < 100; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    CacheRepository cacheRepository = context.getBean(CacheRepository.class);

                    while (true) {
                        cacheRepository.test(UUID.randomUUID().toString());
                    }
                }
            });

            t.setDaemon(true);
            t.run();
        }
        System.in.read();
    }
}
