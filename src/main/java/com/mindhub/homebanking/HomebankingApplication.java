package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return (args) -> {
			Client melba = new Client("Melba", "Morel", "melba@mindhub.com");
			clientRepository.save(melba);
			Client martin = new Client("Martin","Araolaza","martinaraolaza@minhub.com");
			clientRepository.save(martin);
			Client lucy = new Client("Lucía","Vidal","luciavidal@minhub.com");
			clientRepository.save(lucy);
			Account Vin001 = new Account("VIN001", LocalDateTime.now() , 5000D);
			melba.addAccount(Vin001);
			accountRepository.save(Vin001);
			Account Vin002 = new Account("VIN002", LocalDateTime.now().plusDays(1) , 7500D);
			melba.addAccount(Vin002);
			accountRepository.save(Vin002);
			Account Vin003 = new Account("VIN003", LocalDateTime.now(), -1D);
			martin.addAccount(Vin003);
			accountRepository.save(Vin003);
			Account Vin004 = new Account("VIN004", LocalDateTime.now() , 99999999999999D);
			lucy.addAccount(Vin004);
			accountRepository.save(Vin004);

			Transaction T001 = new Transaction( TransactionType.CREDIT, 25000D, "test", LocalDateTime.now());
			Transaction T002 = new Transaction( TransactionType.DEBIT, 1000D, "test1", LocalDateTime.now());
			Transaction T003 = new Transaction( TransactionType.CREDIT, 23000D, "test2", LocalDateTime.now());
			Transaction T004 = new Transaction( TransactionType.DEBIT,  2500D, "test3", LocalDateTime.now());
			Vin001.addTransaction(T001);
			Vin001.addTransaction(T002);
			Vin002.addTransaction(T003);
			Vin003.addTransaction(T004);

			transactionRepository.save(T001);
			transactionRepository.save(T002);
			transactionRepository.save(T003);
			transactionRepository.save(T004);

		};
	}





}
