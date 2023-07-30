package telran.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.spring.model.EmailMessage;
import telran.spring.model.Message;
import telran.spring.model.SmsMessage;

@SpringBootApplication
public class SpringIntroductionApplication {

	public static void main(String[] args) throws JsonProcessingException {
		
		
		 SpringApplication.run(SpringIntroductionApplication.class, args);

	       
	    }


	}
			
	