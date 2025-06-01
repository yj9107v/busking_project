package busking.busking_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class BuskingProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuskingProjectApplication.class, args);
	}

	@Scheduled(fixedRate = 5000)
	public void scheduledTask() {
		System.out.println("Task executed every 5 seconds");
		// pull request 테스트2.

	}
}


