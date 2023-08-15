package telran.spring.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.spring.model.EmailMessage;
import telran.spring.model.Message;

@Service
@Slf4j

public class EmailSender implements Sender {


public String send(Message message) {
		log.debug("email service received message {}", message);
		String res = errorMessage;
		if (message instanceof EmailMessage) {
			EmailMessage emailMessage = (EmailMessage) message;
			res = String.format("email sender text: %s has been sent to email %s", emailMessage.text, emailMessage.emailAddress);
		} else {
			throw new IllegalArgumentException(res);
		}
		return res;
	}


public String getMessageTypeString() {
	return "email";
}


public Class<? extends Message> getMessageTypeObject() {
	return EmailMessage.class;
}



}



