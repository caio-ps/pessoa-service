package br.com.caiosousa.pessoa.service.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = { "br.com.caiosousa.pessoa.service", "br.com.caiosousa.spring" })
@EnableAutoConfiguration
public class Launcher {
	public static void main(String[] args) {
        SpringApplication.run(Launcher.class, args);
    }
}
