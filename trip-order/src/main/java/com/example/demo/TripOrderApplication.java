package com.example.demo;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/*@SpringBootConfiguration
@EnableAutoConfiguration(exclude = {RabbitAutoConfiguration.class})
@ComponentScan(excludeFilters = { @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })*/
@SpringBootApplication
@EnableCaching
@EnableDubbo
@MapperScan(value = "com.example.demo.mapper")
public class TripOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripOrderApplication.class, args);
	}

}
