package org.mailRead.MailReaderService;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.mailRead.MailReaderModel.EmailData;
import org.springframework.stereotype.Service;


@Service
public interface MailService {
	
	public void saveMail(EmailData emailData);
	public List<EmailData> configuration() throws Exception ;
	public String convertToCsv(List<EmailData> mailList);

	public EmailData findById(Long id);
//	public void writeCsvToFile(String csvData, String filePath)throws IOException;

}
