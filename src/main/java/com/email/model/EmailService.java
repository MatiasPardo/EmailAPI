package com.email.model;

import java.io.*;
import org.springframework.stereotype.Service;

import com.email.model.Email.Attachment;

@Service
public class EmailService {
    
    public void send(Email email) throws Exception{

        String path = "C:/ConfigFEArg/" + email.getSchema() + "/" + email.getFilename();
        File file = new File(path); 
        Attachment[] attachments = new Attachment[1];
        Attachment attach = new Attachment(file.getName(), file);
        attachments[0] = attach; 	

        Email.sendEmail(email.getSmtpHost(), email.getSmtpPort(), email.getMailUser(), email.getMailUserPassword(),
                        email.isSMTPStartTLSEnable(), email.isSMTPHostTrusted(), 
                        email.getFromEmail(), email.getToEmail(), email.getCcEmail(), email.getSubject(), 
                        email.getContent(), email.isHtmlContent() , attachments);
        
        //System.out.println(email.getDestinationEmail());
    }

}
