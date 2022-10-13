package org.neat0n.licensingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@RefreshScope
public class LicensingServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(LicensingServiceApplication.class, args);
    }
    
}
