package com.delightreading.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouterConfig {

    String apiBaseUri;
    String uiBaseUri;

    public RouterConfig(@Value("${router.uri.api:http://localhost:9090}") String apiBaseUri,
                        @Value("${router.uri.ui:http://localhost:8080}") String uiBaseUri) {
        this.apiBaseUri = apiBaseUri;
        this.uiBaseUri = uiBaseUri;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                .route("r1", r -> r.path("/api/**")
                        // .filters(f->f.rewritePath("/api/(?<segment>.*)","/browse/${segment}"))
                    .uri(this.apiBaseUri)
                )
                .route("r1", r -> r.path("/login/**")
                        .uri(this.apiBaseUri)
                )
                .route("r1", r -> r.path("/**")
                    .uri(this.uiBaseUri)
                ).build();
    }
}
