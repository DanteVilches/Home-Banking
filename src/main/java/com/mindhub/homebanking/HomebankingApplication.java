package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
		return (args) -> {
			Client melba = new Client("Melba", "Morel", "melba@mindhub.com");
			clientRepository.save(melba);
			Client martin = new Client("Martin","Araolaza","martinaraolaza@minhub.com");
			clientRepository.save(martin);
			Client lucy = new Client("Lucía","Vidal","luciavidal@minhub.com");
			clientRepository.save(lucy);
			Account Vin001 = new Account("Vin001", LocalDateTime.now() , 5000D);
			melba.addAccount(Vin001);
			accountRepository.save(Vin001);
			Account Vin002 = new Account("Vin002", LocalDateTime.now().plusDays(1) , 7500D);
			melba.addAccount(Vin002);
			accountRepository.save(Vin002);
			Account Vin003 = new Account("Vin003", LocalDateTime.now(), -1D);
			martin.addAccount(Vin003);
			accountRepository.save(Vin003);
			Account Vin004 = new Account("Vin004", LocalDateTime.now() , 99999999999999D);
			lucy.addAccount(Vin004);
			accountRepository.save(Vin004);
		};
	}





}
