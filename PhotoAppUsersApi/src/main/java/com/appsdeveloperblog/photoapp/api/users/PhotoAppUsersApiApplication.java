package com.appsdeveloperblog.photoapp.api.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
public class PhotoAppUsersApiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
	SpringApplication.run(PhotoAppUsersApiApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	return application.sources(PhotoAppUsersApiApplication.class);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPosswordEncoder() {
	return new BCryptPasswordEncoder();
    }

    /*
     * @Bean public SpringApplicationContext getSpringApplicationContext() { return
     * new SpringApplicationContext(); }
     * 
     * @Bean(name = "AppProperties") public AppProperties getAppProperties() {
     * return new AppProperties(); }
     */

}
