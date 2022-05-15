package com.unionbankng.future.gateway.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Component
@Primary
@EnableAutoConfiguration
public class SwaggerRegister implements SwaggerResourcesProvider {

	@Override
	public List<SwaggerResource> get() {
		List<SwaggerResource> resources = new ArrayList<>();
		resources.add(swaggerResource("authorization-server", "/authserv/v2/api-docs", "0.0.1"));
		resources.add(swaggerResource("job-service", "/jobserv/v2/api-docs", "0.0.1"));
		resources.add(swaggerResource("learn-service", "/learnserv/v2/api-docs", "0.0.1"));
		resources.add(swaggerResource("payment-service", "/paymentserv/v2/api-docs", "0.0.1"));
		resources.add(swaggerResource("bank-service", "/bankserv/v2/api-docs", "0.0.1"));
		resources.add(swaggerResource("messaging-service", "/messageserv/v2/api-docs", "0.0.1"));
		resources.add(swaggerResource("wallet-service", "/walletserv/v2/api-docs", "0.0.1"));
		return resources;
	}

	private SwaggerResource swaggerResource(String name, String location, String version) {
		SwaggerResource swaggerResource = new SwaggerResource();
		swaggerResource.setName(name);
		swaggerResource.setLocation(location);
		swaggerResource.setSwaggerVersion(version);
		return swaggerResource;
	}

}