package de.neuefische.CapStone.backend.config;

import de.neuefische.CapStone.backend.rest.openHab.OpenHabAPI;
import de.neuefische.CapStone.backend.rest.openHab.OpenHabClient;
import feign.Feign;
import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenhabClientConfig {

    @Bean
    public OpenHabAPI getOpenhabAPI() {
        return Feign.builder()
                .client(new OkHttpClient())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(OpenHabClient.class))
                .logLevel(Logger.Level.FULL)
                .target(OpenHabAPI.class, "https://myopenhab.org");
    }

}

