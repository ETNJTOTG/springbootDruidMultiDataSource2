package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @title App.java
 * @description TODO
 * @time 2019/07/18 16:34:24
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class App {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(App.class, args);
	}

}
