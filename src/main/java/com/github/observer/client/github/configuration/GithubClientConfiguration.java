package com.github.observer.client.github.configuration;

import com.github.observer.client.github.GithubClient;
import feign.Feign;
import feign.Request;
import feign.RequestInterceptor;
import feign.Response;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(GithubClientProperties.class)
public class GithubClientConfiguration {

    @Bean
    public GithubClient githubClient(GithubClientProperties clientProperties) {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .requestInterceptor(getRequestInterceptor(clientProperties))
                .options(getOptions(clientProperties))
                .target(GithubClient.class, clientProperties.getUrl());
    }

    private RequestInterceptor getRequestInterceptor(GithubClientProperties clientProperties) {
        return requestTemplate ->
                requestTemplate.header(HttpHeaders.ACCEPT, clientProperties.getAcceptVersion());
    }

    private Request.Options getOptions(GithubClientProperties clientProperties) {
        return new Request.Options(
                clientProperties.getConnectTimeout(), TimeUnit.SECONDS,
                clientProperties.getReadTimeout(), TimeUnit.SECONDS,
                clientProperties.isFollowRedirects());
    }

    static class GithubErrorDecoded implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, Response response) {
            return null;
        }
    }

}
