package org.chnu.confplatform.back.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class SwaggerConfig {
    private static final String SCHEME_NAME = "Bearer Authentication";
    private static final String SCHEME = "Bearer";

    @Bean
    @Profile({"swagger"})
    public GroupedOpenApi getBackofficeApi() {
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    @Profile({"swagger"})
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(getInfo())
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
                .components(new Components().addSecuritySchemes(SCHEME_NAME, getSecurityScheme()));
    }

    private Info getInfo() {
        return new Info()
                .title("Swagger API")
                .version("V1.0")
                .contact(new Contact()
                        .name("Yukon development team")
                        .email("office@yukon.cv.ua"));
    }

    private SecurityScheme getSecurityScheme() {
        return new SecurityScheme()
                .name(SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme(SCHEME)
                .bearerFormat("JWT");
    }

}
