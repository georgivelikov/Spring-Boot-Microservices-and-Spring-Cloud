package com.appsdeveloperblog.photoapp.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PhotoAppAccountsApiApplication {

    public static void main(String[] args) {
	SpringApplication.run(PhotoAppAccountsApiApplication.class, args);
    }

}
