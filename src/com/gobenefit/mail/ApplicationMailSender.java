package com.gobenefit.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMailSender {

	@Autowired
	MailSender mail;

	public void sendMail(SimpleMailMessage mailMessage) {
		mail.send(mailMessage);
	}

}
