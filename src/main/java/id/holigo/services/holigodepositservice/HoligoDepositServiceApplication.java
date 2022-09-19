package id.holigo.services.holigodepositservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class HoligoDepositServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HoligoDepositServiceApplication.class, args);
	}

}
