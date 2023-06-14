package br.com.portfolioManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("br.com.portfolioManager")
public class ProjetoPortFolioApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoPortFolioApplication.class, args);
	}

}
