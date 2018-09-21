package com.delightreading.config;

import com.delightreading.authsupport.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthSuccessHandler authSuccessHandler;

    @Autowired
    TokenBasedUserDetailsService tokenBasedUserDetailsService;

    @Autowired
    JwtService jwtService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.cors().and().csrf().disable().authorizeRequests()
//                .antMatchers(HttpMethod.GET, /login).permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
//                // this disables session creation on Spring Security
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.cors().and().csrf().disable()
                .authorizeRequests().antMatchers("/api/users/v1/login", "/api/users/v1/register").permitAll()
                .anyRequest().authenticated()
                .and()
                    .addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtService))
                    .addFilterAfter(new CookieAuthorizationFilter(authenticationManager(), jwtService), BasicAuthenticationFilter.class)
                    .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and()
                    // this disables session creation on Spring Security
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .oauth2Login()
                .successHandler(authSuccessHandler);

//        http.headers()
//                .xssProtection().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.tokenBasedUserDetailsService(tokenBasedUserDetailsService); // .passwordEncoder(bCryptPasswordEncoder);
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    // @see: https://docs.spring.io/spring-security/site/docs/current/reference/html/cors.html
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "DELETE", "POST", "PUT", "OPTIONS"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
                = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(tokenBasedUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new AccessDenied403Handler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
