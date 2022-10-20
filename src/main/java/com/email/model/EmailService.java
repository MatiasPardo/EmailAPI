package com.email.model;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.email.model.Email.Attachment;

@Service
public class EmailService {
    
    @Async
    public void send(Email email) throws Exception{

        Attachment[] attachments = null;
        if(email.getFilename() != null && email.getSchema() != null){
            String path = "C:/MparDOS/" + email.getSchema() + "/" + email.getFilename();
            File file = new File(path);
            if(!file.exists()){
                Logger logger = Logger.getLogger("Email");
                logger.setLevel(Level.INFO);
                logger.info("No existe el archivo");
            }else{
                attachments = new Attachment[1];
                Attachment attach = new Attachment(file.getName(), file);
                attachments[0] = attach;
            }
        }

        Email.sendEmail(email.getSmtpHost(), email.getSmtpPort(), email.getMailUser(), email.getMailUserPassword(),
                        email.isSMTPStartTLSEnable(), email.isSMTPHostTrusted(), 
                        email.getFromEmail(), email.getToEmail(), email.getCcEmail(), email.getSubject(), 
                        email.getContent(), email.isHtmlContent() , attachments);
            
    }

}
