package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class TaskTimeApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(TaskTimeApplication.class, args);

	}
	
	/*
	 * Servletコンテナ(＝AppachTomcat)によって実行されるアプリケーションを設定
	 * 要「extends SpringBootServletInitializer」
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(TaskTimeApplication.class);
	}
	
}
