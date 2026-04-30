package it.gov.pagopa.payhub.pdnd;

import it.gov.pagopa.payhub.pdnd.utils.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.webmvc.autoconfigure.error.ErrorMvcAutoConfiguration;

import java.util.TimeZone;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class PayhubPdndApplication {

	public static void main(String[] args) {
        TimeZone.setDefault(Constants.DEFAULT_TIMEZONE);
		SpringApplication.run(PayhubPdndApplication.class, args);
	}

}
