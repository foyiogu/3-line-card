package com.threeline.authorizationserver;

//import net.devh.boot.grpc.server.security.authentication.BasicGrpcAuthenticationReader;
//import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableAuthorizationServer
@EnableResourceServer
public class AuthorizationServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServerApplication.class, args);
	}


//	@Bean
//	public GrpcAuthenticationReader grpcAuthenticationReader() {
//		return new BasicGrpcAuthenticationReader();
//	}
}
