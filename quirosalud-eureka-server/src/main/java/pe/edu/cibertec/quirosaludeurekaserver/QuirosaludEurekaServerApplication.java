package pe.edu.cibertec.quirosaludeurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class QuirosaludEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuirosaludEurekaServerApplication.class, args);
    }

}
