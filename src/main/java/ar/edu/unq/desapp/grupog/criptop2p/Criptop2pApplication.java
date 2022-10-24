package ar.edu.unq.desapp.grupog.criptop2p;

import ar.edu.unq.desapp.grupog.criptop2p.dto.UserRequestBody;
import ar.edu.unq.desapp.grupog.criptop2p.model.Role;
import ar.edu.unq.desapp.grupog.criptop2p.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Criptop2pApplication {

	public static void main(String[] args) {
		SpringApplication.run(Criptop2pApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {

			String roleName = "ROLE_USER";

			userService.saveRole(new Role(null, roleName));

			userService.saveUser(new UserRequestBody(
					"Juan",
					"Nieve",
					"jnieve@gmail.com",
					"Password@1",
					"Av. Norte 1234",
					"4608738591410700747451",
					"45821674"));
			userService.addRoleToUser("jnieve@gmail.com", roleName);

			userService.saveUser(new UserRequestBody(
					"Rhaenyra",
					"Targaryen",
					"rtargaryen@gmail.com",
					"Password@1",
					"Av. Oeste 2200",
					"4608738591410700547451",
					"45821674"));
			userService.addRoleToUser("rtargaryen@gmail.com", roleName);

		};
	}


}
