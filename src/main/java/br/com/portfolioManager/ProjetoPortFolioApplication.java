package br.com.portfolioManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "br.com.portfolioManager")
@ComponentScan("br.com.portfolioManager")
public class ProjetoPortFolioApplication extends SpringBootServletInitializer{
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ProjetoPortFolioApplication.class);
    }


	public static void main(String[] args) {
		SpringApplication.run(ProjetoPortFolioApplication.class, args);
	}

}
