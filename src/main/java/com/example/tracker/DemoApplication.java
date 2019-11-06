package com.example.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

//	@Autowired
//	static IObjectDaoImpl IObjectDaoImpl;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
//		IObjectDaoImpl.connect();
	}

}