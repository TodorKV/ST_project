package com.stproject.st_be.security.config;

import com.stproject.st_be.security.filter.AuthenticationFilter;
import com.stproject.st_be.security.filter.AuthorizationFilter;
import com.stproject.st_be.services.base.UserService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userServiceImpl;
    private final BCryptPasswordEncoder passwordEncoder;

    public AppSecurityConfig(UserService userServiceImpl, BCryptPasswordEncoder passwordEncoder) {
        super();
        this.userServiceImpl = userServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new AuthenticationFilter(authenticationManager()))
                .addFilterAfter(new AuthorizationFilter(), AuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/login/**").permitAll()
                // .antMatchers("/order-websocket/**").permitAll()
                .antMatchers("/api/v1/user/**").permitAll()
                .antMatchers("/api/v1/comment/**").permitAll()
                .antMatchers("/api/v1/box/**").hasAnyAuthority("ADMIN")
                .antMatchers("/api/v2/users/**").hasAuthority("ADMIN")
                .antMatchers("/api/v3/order/**").hasAuthority("ADMIN")
                .anyRequest()
                .authenticated();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**")
                .regexMatchers("/\\d+.*/.*\\..*");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userServiceImpl);
        return provider;
    }

    // @Bean
    // public CorsConfigurationSource corsConfigurationSource() {
    // CorsConfiguration configuration = new CorsConfiguration();
    // configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200/",
    // "http://localhost:4200"));
    // configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH",
    // "DELETE", "OPTIONS"));
    // configuration.setAllowedHeaders(
    // Arrays.asList("authorization", "content-type", "x-auth-token",
    // "content-disposition"));
    // configuration.setExposedHeaders(Arrays.asList("authorization"));
    // UrlBasedCorsConfigurationSource source = new
    // UrlBasedCorsConfigurationSource();
    // source.registerCorsConfiguration("/**", configuration);
    // return source;
    // }

}
