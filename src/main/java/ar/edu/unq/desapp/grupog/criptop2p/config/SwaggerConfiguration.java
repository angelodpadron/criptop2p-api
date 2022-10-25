package ar.edu.unq.desapp.grupog.criptop2p.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("CriptoP2P")
                                .version("1.0")
                                .description("MVP for Peer-to-Peer (p2p) use of cryptocurrency.")
                                .contact(new Contact()
                                        .name("API Support")
                                        .email("padron891@gmail.com")));
    }
}
