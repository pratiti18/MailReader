package org.mailRead.MailReaderConfig;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
	
	@Value("${spring.mail.host}")
	private String mailHost;
	
	@Value("${spring.mail.port}")
	private String mailPort;
	
	@Value("${spring.mail.username}")
	private String mail;
	
	@Value("${spring.mail.password}")
	private String password;
	
	@Bean
	public Folder getFolder() throws MessagingException {
		Properties props=new Properties();
		props.put("mail.store.protocol", "imaps");
		props.put("mail.imaps.host", mailHost);
		props.put("mail.imaps.port", mailPort);
		props.put("mail.imaps.ssl.enable", "true");
		Session session = Session.getDefaultInstance(props);
		Store store = session.getStore("imaps");
		store.connect(mailHost,mail,password);
		Folder inbox = store.getFolder("INBOX");
		
		return inbox;
	}

}
