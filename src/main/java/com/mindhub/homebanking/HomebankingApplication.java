package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository,
									  CardRepository cardRepository) {
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
			Account Vin003 = new Account("VIN003", LocalDateTime.now(), 24000.12D);
			melba.addAccount(Vin003);
			accountRepository.save(Vin003);
			Account Vin004 = new Account("VIN004", LocalDateTime.now(), -1D);
			martin.addAccount(Vin004);
			accountRepository.save(Vin004);
			Account Vin005 = new Account("VIN004", LocalDateTime.now() , 99999999999999D);
			lucy.addAccount(Vin005);
			accountRepository.save(Vin005);

			Transaction T001 = new Transaction( TransactionType.CREDIT, 25000D, "test", LocalDateTime.now());
			Transaction T002 = new Transaction( TransactionType.DEBIT, 1000D, "test1", LocalDateTime.now());
			Transaction T003 = new Transaction( TransactionType.CREDIT, 23000D, "test2", LocalDateTime.now());
			Transaction T004 = new Transaction( TransactionType.DEBIT,  2500D, "test3", LocalDateTime.now());
			Vin001.addTransaction(T001);
			Vin001.addTransaction(T002);
			Vin002.addTransaction(T003);
			Vin005.addTransaction(T004);

			transactionRepository.save(T001);
			transactionRepository.save(T002);
			transactionRepository.save(T003);
			transactionRepository.save(T004);
			List<Integer> mortgagePayment = List.of(12,24,36,48,60);
			List<Integer> personalPayment = List.of(6,12,24);
			List<Integer> carLoanPayment = List.of(6,12,24,36);
			Loan Mortgage = new Loan("Mortgage",500000.00D, mortgagePayment);
			Loan Personal = new Loan("Personal",100000.00D, personalPayment);
			Loan carLoan = new Loan("Car Loan",300000.00D, carLoanPayment);

			ClientLoan mortgageMelba = new ClientLoan(400000.20D, mortgagePayment.get(4), new Date());
			ClientLoan personalMelba = new ClientLoan(50000.10D, personalPayment.get(1), new Date());
			ClientLoan personalMartin = new ClientLoan(100000.70D,personalPayment.get(2),new Date());
			ClientLoan carLoanMartin = new ClientLoan(200000.50D,carLoanPayment.get(3),new Date());
			melba.addClientLoan(mortgageMelba);
			melba.addClientLoan(personalMelba);
			martin.addClientLoan(personalMartin);
			martin.addClientLoan(carLoanMartin);

			Personal.addClientLoan(personalMelba);
			Mortgage.addClientLoan(mortgageMelba);
			Personal.addClientLoan(personalMartin);
			carLoan.addClientLoan(carLoanMartin);

			loanRepository.save(Mortgage);
			loanRepository.save(Personal);
			loanRepository.save(carLoan);

			clientLoanRepository.save(mortgageMelba);
			clientLoanRepository.save(personalMelba);
			clientLoanRepository.save(personalMartin);
			clientLoanRepository.save(carLoanMartin);

			Card goldDebitCardMelba = new Card("Melba Morel", CardType.DEBIT,CardColor.GOLD,"4254 3219 3240 8359", 399, LocalDate.now(), LocalDate.now().minusDays(25));
			Card titaniumDebitCardMelba= new Card("Melba Morel", CardType.CREDIT,CardColor.TITANIUM,"2576 0378 1257 6487", 274, LocalDate.now(), LocalDate.now().minusDays(25));
			Card silverCreditCardMartin = new Card("Martin Araolaza",CardType.CREDIT,CardColor.SILVER,"8763 2673 1823 2434",421, LocalDate.now(),LocalDate.now().minusDays(12).minusYears(3));


			melba.addCard(goldDebitCardMelba);
			melba.addCard(titaniumDebitCardMelba);
			martin.addCard(silverCreditCardMartin);


			cardRepository.save(goldDebitCardMelba);
			cardRepository.save(titaniumDebitCardMelba);
			cardRepository.save(silverCreditCardMartin);





		};
	}





}
