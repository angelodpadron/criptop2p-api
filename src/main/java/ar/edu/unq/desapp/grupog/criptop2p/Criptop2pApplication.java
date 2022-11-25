package ar.edu.unq.desapp.grupog.criptop2p;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Criptop2pApplication {
	public static void main(String[] args) {
		SpringApplication.run(Criptop2pApplication.class, args);
	}

}
