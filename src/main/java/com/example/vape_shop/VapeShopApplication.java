package com.example.vape_shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
public class VapeShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(VapeShopApplication.class, args);
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
	MultipartConfigFactory factory = new MultipartConfigFactory();
//	// Самый большой отдельный файл
//	factory.setMaxFileSize(DataSize.parse("10240KB")); //KB,MB
//	// Устанавливаем общий размер всех загруженных данных
//	factory.setMaxRequestSize(DataSize.parse("102400KB"));
	return factory.createMultipartConfig();
	}

}
