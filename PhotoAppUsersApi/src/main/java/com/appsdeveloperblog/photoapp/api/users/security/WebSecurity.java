package com.appsdeveloperblog.photoapp.api.users.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.appsdeveloperblog.photoapp.api.users.io.repository.UserRepository;
import com.appsdeveloperblog.photoapp.api.users.service.UserService;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder,
	    UserRepository userRepository) {
	this.userDetailsService = userDetailsService;
	this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	this.userRepository = userRepository;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
	auth.userDetailsService(userDetailsService)
		.passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
	// Needed for h2
	http.authorizeRequests()
		.antMatchers("/")
		.permitAll()
		.antMatchers("/h2-console/**")
		.permitAll();
	// Needed for h2
	http.headers()
		.frameOptions()
		.disable();
	http.cors()
		.and()
		.csrf()
		.disable()
		.authorizeRequests()
		.antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
		.permitAll()
		.antMatchers(HttpMethod.GET, "/users/status/check")
		.permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.addFilter(getAuthenticationFilter("/users/login"))
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);// No session, no cookies

    }

    // Spring default login url is '/login'. You could change it by adding
    // custom
    // url
    public AuthenticationFilter getAuthenticationFilter(String url) throws Exception {
	final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
	if (url != null) {
	    filter.setFilterProcessesUrl(url);
	}

	return filter;
    }

    // Returns AuthenticationFilter with Spring default login url '/login'
    public AuthenticationFilter getDefaultAuthenticationFilter() throws Exception {
	return getAuthenticationFilter(null);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
	final CorsConfiguration configuration = new CorsConfiguration();

	configuration.setAllowedOrigins(Arrays.asList("*"));
	configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD"));
	configuration.setAllowCredentials(true);
	configuration.setAllowedHeaders(Arrays.asList("*"));

	final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	source.registerCorsConfiguration("/**", configuration);

	return source;
    }
}
