package com.synergy_hub.synergyhub;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SynergyhubApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure()
			.directory("./")     // .env 파일 위치
			.ignoreIfMissing()
			.load();

		dotenv.entries().forEach(entry ->
			System.setProperty(entry.getKey(), entry.getValue())
		);

		SpringApplication.run(SynergyhubApplication.class, args);
	}

}
