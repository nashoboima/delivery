package ru.ddd.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaRepositories(basePackages = "ru.ddd.delivery.adapters.out.postgres")
@EntityScan(basePackages = {
        "ru.ddd.delivery.core.domain.model",
		"ru.ddd.delivery.adapters.out.postgres.outbox"
})
@SpringBootApplication(scanBasePackages = "ru.ddd")
public class DeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryApplication.class, args);
	}

}
