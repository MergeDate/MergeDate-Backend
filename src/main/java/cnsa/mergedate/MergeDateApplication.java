package cnsa.mergedate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MergeDateApplication {

	public static void main(String[] args) {
		SpringApplication.run(MergeDateApplication.class, args);
	}

}
