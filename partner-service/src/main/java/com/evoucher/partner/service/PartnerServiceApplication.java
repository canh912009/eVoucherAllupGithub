
package com.evoucher.partner.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import com.example.consumingwebservice.wsdl.GetCountryResponse;

@SpringBootApplication
public class PartnerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartnerServiceApplication.class, args);
	}

//	@Bean
//	CommandLineRunner lookup(CountryClient countryClient) {
//		return args -> {
//			String country = "Spain";
//
//			if (args.length > 0) {
//				country = args[0];
//			}
//			GetCountryResponse response = countryClient.getCountry(country);
//			System.err.println(response.getCountry().getCurrency());
//		};
//	}

}
