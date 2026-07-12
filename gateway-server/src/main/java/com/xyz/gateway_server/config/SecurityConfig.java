package com.xyz.gateway_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.security.Security;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain SecurityWebFilterChain(ServerHttpSecurity http) throws Exception {
        http.authorizeExchange(
                exchange -> exchange
                        .pathMatchers("/**").permitAll()
                        .pathMatchers("/notifications/ws/**").permitAll()
                        .pathMatchers("/salons/**",
                                "/categories/**",
                                "/notifications/**",
                                "/booking/**",
                                "/payment/**",
                                "/service-offering/**",
                                "/users/**"
                                ,"/reviews/**"
                                )
                        .hasAnyRole("CUSTOMER","SALON_OWNER","ADMIN")
                        .pathMatchers("/categories/salon-owner/**",
                                "/notifications/salon-owner/**",
                                "/service-offering/salon-owner/**"
                                )
                        .hasAnyRole("SALON_OWNER")
        ).oauth2ResourceServer(oAuth2ResourceServerSpec ->oAuth2ResourceServerSpec
                .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantAuthoritiesExtractor()))
        );
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();

    }

    private Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> grantAuthoritiesExtractor() {

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                new KeyCloakConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
