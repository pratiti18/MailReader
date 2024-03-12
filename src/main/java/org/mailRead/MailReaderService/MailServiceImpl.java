package org.mailRead.MailReaderService;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;

import org.mailRead.MailReaderConfig.MailConfig;
import org.mailRead.MailReaderModel.EmailData;
import org.mailRead.MailReaderRepository.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

	@Autowired
	private MailRepository mailRepo;

	@Autowired
	private MailConfig mailConfig;

	@Override
	public void saveMail(EmailData emailData) {
		try {
			EmailData newData = mailRepo.findBySubject(emailData.getSubject());
			if (newData == null || !newData.equals(emailData)) {
				EmailData newEmailData = new EmailData();
				newEmailData.setId(emailData.getId());
				newEmailData.setUsermail(emailData.getUsermail());

				newEmailData.setSubject(emailData.getSubject());
				mailRepo.save(emailData);
			}
		} catch (Exception e) {
			System.out.println("it already exists");

		}
	}

	@Override
	public List<EmailData> configuration() throws Exception {
		Folder inbox = mailConfig.getFolder();
		System.out.println("Hitting");
		System.out.println("Connection done");
		inbox.open(Folder.READ_WRITE);
		Flags seen = new Flags(Flags.Flag.SEEN);
		SearchTerm search = new AndTerm(new FlagTerm(new Flags(Flags.Flag.SEEN), false), new SubjectTerm("DSR"));
		Message[] messages = inbox.search(search);

		List<EmailData> emailList = Arrays.stream(messages).map(message -> {
			try {
				String sub = message.getSubject();
				if (sub != null) {
					EmailData emailData = new EmailData();
					message.setFlag(Flags.Flag.SEEN, true);

					emailData.setSubject(message.getSubject());

					System.out.println(message.getReceivedDate());

					String content = getContent(message);
					emailData.setUsermail(message.getFrom()[0].toString());
					emailData.setDate(message.getReceivedDate());

					emailData.setProjectName(projectName(sub));

					emailData.setTasksDone(extractContent(content)[0]);
					emailData.setTaskToDo(extractContent(content)[1]);
					emailData.setTaskOnHold(extractContent(content)[2]);

					// Save the email
					saveMail(emailData);

					return emailData;
				}
			} catch (Exception e) {
				System.out.println("Some error occurred");
			}
			return null;
		}).filter(emailData -> emailData != null)
				.collect(Collectors.toList());

		inbox.close(true);
		return emailList;
	}

	private String getContent(Message message) throws Exception {
		Object content = message.getContent();
		if (content instanceof String) {
			return (String) content;
		} else if (content instanceof MimeMultipart) {
			StringBuilder text = new StringBuilder();
			MimeMultipart mimeMultipart = (MimeMultipart) content;
			for (int i = 0; i < mimeMultipart.getCount(); i++) {
				BodyPart bodyPart = mimeMultipart.getBodyPart(i);
				if (bodyPart.isMimeType("text/plain")) {
					text.append(bodyPart.getContent());
				}
			}
			return text.toString().toLowerCase();
		}
		return "";
	}

	private String[] extractContent(String str) {
		String[] lines = str.split("\n");
		String[] content = new String[3];
		boolean isTasksDoneSection = false;
		boolean isTasksTodoSection = false;
		boolean isTasksOnHoldSection = false;

		for (String line : lines) {
			line = line.trim();
			if (line.toLowerCase().startsWith("tasks done") || line.toLowerCase().startsWith("tasks completed")
					|| line.toLowerCase().startsWith("tasks finished") || line.toLowerCase().startsWith("task finished") || line.toLowerCase().startsWith("task done") || line.toLowerCase().startsWith("task completed")) {
				isTasksDoneSection = true;
				continue;
			} else if (line.toLowerCase().startsWith("tasks todo") || line.toLowerCase().startsWith("tasks to do")
					|| line.toLowerCase().startsWith("tasks planned") || line.toLowerCase().startsWith("task todo") || line.toLowerCase().startsWith("task to do")
					|| line.toLowerCase().startsWith("task planned")) {
				isTasksTodoSection = true;
				isTasksDoneSection = false;
				continue;
			} else if (line.toLowerCase().startsWith("tasks onhold")
					|| line.toLowerCase().startsWith("tasks on hold") || line.toLowerCase().startsWith("task onhold")
					|| line.toLowerCase().startsWith("task on hold")) {
				isTasksOnHoldSection = true;
				isTasksTodoSection = false;
				isTasksDoneSection = false;
				continue;
			}

			if (isTasksDoneSection) {
				if (content[0] == null) {
					content[0] = line;
				} else {
					content[0] += " " + line;
				}
			} else if (isTasksTodoSection) {
				if (content[1] == null) {
					content[1] = line;
				} else {
					content[1] += " " + line;
				}
			} else if (isTasksOnHoldSection) {
				if (content[2] == null) {
					content[2] = line;
				} else {
					content[2] += " " + line;
				}
			}
		}
		return content;
	}

	private String projectName(String str) {
		str = str.toLowerCase();
	    String patternString = "project(.*)dsr";
	    Pattern pattern = Pattern.compile(patternString);
	    Matcher matcher = pattern.matcher(str);
	    if(matcher.find()){
	        String extractedText = matcher.group(1);
	        
	        extractedText = extractedText.replaceAll("[^a-zA-Z0-9]", " ");
	        return extractedText.toUpperCase().trim();
	    }
	    else{
	        return "NA";
	    }
	}
	@Override
	public String convertToCsv(List<EmailData> mailList) {
	    System.out.println("Inside the Convert to CSV File Section");
	    StringBuilder csvData = new StringBuilder();

	    csvData.append("Sender Email,Subject, Project Done, Sent Time, Status Done,Status Planned,Status On Hold\n");

	    for (EmailData mail : mailList) {
	        StringJoiner csvLine = new StringJoiner(",");
	        csvLine.add(mail.getUsermail());
	        csvLine.add(mail.getSubject());
	        csvLine.add(mail.getProjectName());
	        csvLine.add(mail.getDate().toString());
	        csvLine.add(mail.getTasksDone());
	        csvLine.add(mail.getTaskToDo());
	        csvLine.add(mail.getTaskOnHold());

	        csvData.append(csvLine.toString()).append("\n");
	    }
	    System.out.println("Converted to CSV File Successfully");
	    return csvData.toString();
	}

	@Override
	public EmailData findById(Long id) {
		Optional<EmailData> optionalEmailData =mailRepo.findById(id);
		EmailData emailData=null;
		if(optionalEmailData.isPresent()){
			 emailData = optionalEmailData.get();
		}
		return emailData;
	}

//	@Override
//	public void writeCsvToFile(String csvData, String filePath) throws IOException {
//	    try (FileWriter writer = new FileWriter(filePath)) {
//	        System.out.println("Writing to CSV File...");
//	        writer.write(csvData);
//	        System.out.println("Written to CSV File Successfully!!!");
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	        throw new IOException("Error occurred while writing CSV data to file: " + filePath, e);
//	    }
//	}

}
