package org.example.tracker.config;

import org.example.tracker.datamodel.Role;
import org.example.tracker.filter.SimpleCORSFilter;
import org.example.tracker.security.filter.JWTAuthenticationFilter;
import org.example.tracker.security.filter.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${service.password.secret}")
    private String secret;

    @Value("${service.password.expiration}")
    private Long expiration;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST,
                            "/api/account/register",
                            "/api/suggestion",
                            "/api/suggestion/{id}/upvote",
                            "/api/image").permitAll()
                    .antMatchers(HttpMethod.GET,
                            "/",
                            "/api/suggestion",
                            "/api/suggestion/{id}",
                            "/api/comment/suggestion/{id}",
                            "/v2/api-docs",
                            "/configuration/ui",
                            "/swagger-resources/**",
                            "/configuration/security",
                            "/swagger-ui.html",
                            "/webjars/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/suggestion/stage").hasAuthority(Role.ADMINISTRATOR.stringValue())
                    .anyRequest().authenticated()
                    .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), secret, expiration))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), secret))
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.addExposedHeader("Authorization");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod(HttpMethod.PUT);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
