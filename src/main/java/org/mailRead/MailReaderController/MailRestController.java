package org.mailRead.MailReaderController;

import org.mailRead.MailReaderModel.EmailData;
import org.mailRead.MailReaderService.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restController")
public class MailRestController {

    @Autowired
    public MailService mailService;
    @PostMapping("/getUser")
    public EmailData getUserData(@RequestParam Long id ){
        System.out.println("Inside");
         return mailService.findById(id);
    }
}
