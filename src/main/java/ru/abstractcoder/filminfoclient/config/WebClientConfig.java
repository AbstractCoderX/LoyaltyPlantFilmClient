package ru.abstractcoder.filminfoclient.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClientCustomizer webClientCustomizer(Jackson2JsonEncoder encoder, Jackson2JsonDecoder decoder) {
        return webClientBuilder -> webClientBuilder
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> {
                            configurer.defaultCodecs().jackson2JsonEncoder(encoder);
                            configurer.defaultCodecs().jackson2JsonDecoder(decoder);
                        })
                        .build()
                );
    }

    @Bean
    @Qualifier("loyaltyPlant")
    public WebClient loyaltyplantWebClient(
            WebClient.Builder webClientBuilder,
            @Value("${loyalty-plant.api-key}") String apiKey) {
        return webClientBuilder
                .baseUrl("https://easy.test-assignment-a.loyaltyplant.net")
                .defaultUriVariables(Map.of(
                        "api_key", apiKey
                ))
                .build();
    }

}