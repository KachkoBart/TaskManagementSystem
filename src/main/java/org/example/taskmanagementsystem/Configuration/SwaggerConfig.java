package org.example.taskmanagementsystem.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {
    private final String DOC_VERSION = "1.0.0";

    @Bean
    public OpenAPI baseOpenApi() {
        String DOC_TITLE = "Api documentation";
        return new OpenAPI()
                .info(
                        new Info()
                                .title(DOC_TITLE)
                                .version(DOC_VERSION)
                );
    }
}