package com.email.model;

import javax.validation.Valid;

import java.util.logging.Logger;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "api/email")
public class EmailController {
    
    @Autowired
    private EmailService emailService;

    @PostMapping(path="/send")
    public ResponseEntity<Email> sendEmail(@RequestBody @Valid Email email) throws Exception{
        Logger logger = Logger.getLogger("Email");
        logger.setLevel(Level.INFO);
		logger.info("Sending email pending");
        emailService.send(email);
        return new ResponseEntity<Email>(email,HttpStatus.ACCEPTED);
    }

}
