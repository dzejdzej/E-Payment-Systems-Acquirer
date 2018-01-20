package acquirer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AcquirerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcquirerApplication.class, args);
	}
}
