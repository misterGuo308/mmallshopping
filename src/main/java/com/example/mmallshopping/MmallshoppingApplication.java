package com.example.mmallshopping;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.mmallshopping.mapper")
public class MmallshoppingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MmallshoppingApplication.class, args);
	}

}
