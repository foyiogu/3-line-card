package com.unionbankng.future.futurejobservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {                                    
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.OAS_30)
          .select()
          .paths(PathSelectors.any())
          .apis(RequestHandlerSelectors.basePackage("com.unionbankng.future.futurejobservice.controllers"))
          .build().apiInfo(metaData());                                           
    }
    
     private ApiInfo metaData() {
        return new ApiInfoBuilder()
                 .title("Job Service")
                .description("This service handles job creations and freelancing as well as payment for jobs.")
                .version("0.0.1")
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
                .contact(new Contact("Rabiu Aliyu", "https://www.linkedin.com", "net.rabiualiyu@gmail.com"))
                .build();
    }

}