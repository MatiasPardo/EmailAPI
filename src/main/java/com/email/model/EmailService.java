package com.email.model;

import java.io.*;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.email.model.Email.Attachment;

@Service
public class EmailService {
    
    @Async
    public void send(Email email) throws Exception{

        Attachment[] attachments = null;
        if(email.getFilename() != null && email.getSchema() != null){
            String path = "C:/ConfigFEArg/" + email.getSchema() + "/Email/" + email.getFilename();
            File file = new File(path);
            if(!file.exists()) 
                throw new Exception("No existe el archivo");

            attachments = new Attachment[1];
            Attachment attach = new Attachment(file.getName(), file);
            attachments[0] = attach; 	
        }

        Email.sendEmail(email.getSmtpHost(), email.getSmtpPort(), email.getMailUser(), email.getMailUserPassword(),
                        email.isSMTPStartTLSEnable(), email.isSMTPHostTrusted(), 
                        email.getFromEmail(), email.getToEmail(), email.getCcEmail(), email.getSubject(), 
                        email.getContent(), email.isHtmlContent() , attachments);
            
    }

}
