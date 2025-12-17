package it.gov.pagopa.payhub.pdnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.webmvc.autoconfigure.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class PayhubPdndApplication {

	public static void main(String[] args) {
		SpringApplication.run(PayhubPdndApplication.class, args);
	}

}
