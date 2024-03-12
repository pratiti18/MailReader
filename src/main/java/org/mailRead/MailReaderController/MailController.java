package org.mailRead.MailReaderController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.mail.MessagingException;

import org.mailRead.MailReaderModel.EmailData;
import org.mailRead.MailReaderRepository.MailRepository;
import org.mailRead.MailReaderService.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/readMails")
public class MailController {

	@Autowired
	private MailService service;
	@Autowired
	private MailRepository mailRepo;
	private List<EmailData> dataToDisplay;

//	@PostMapping("/start")
//	public List<EmailData> startReading() throws Exception{
//		return service.configuration();
//	}
//	
	@Scheduled(fixedRate = 10000) // 10000 milliseconds = 10 seconds
	//@GetMapping("/start")
	public void scheduledStartReading() throws Exception {
		dataToDisplay = service.configuration();
	}

	@GetMapping("/start")
	public String yourMapping(Model model) {
		List<EmailData> list=mailRepo.findAll();
		model.addAttribute("data", list);
		return "home";
	}

	@GetMapping("/another")
	public String userData(Model model) {
		List<EmailData> list=mailRepo.findAll();
		model.addAttribute("data", list);
		model.addAttribute("selectedOption", new EmailData());
		return "index";
	}
	@PostMapping("")
	@GetMapping("/generate")
	public ResponseEntity<byte[]> generateCsv() {
	    try {
	        List<EmailData> mailList = mailRepo.findAll();

	        String csvData = service.convertToCsv(mailList);

	        byte[] csvBytes = csvData.getBytes(StandardCharsets.UTF_8);

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			//inline for showing in the browser anf attachment for downloading
	        headers.setContentDispositionFormData("attachment", "mailData.csv");

	        return ResponseEntity.ok()
	                .headers(headers)
	                .body(csvBytes);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
}
