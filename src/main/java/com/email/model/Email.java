package com.email.model;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.stereotype.Component;

import jakarta.activation.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class Email {
	@Positive
	private int smtpPort;

	@NotBlank
	private String smtpHost;
	@NotBlank
	private String mailUser;
	@NotBlank
	private String mailUserPassword;
	@NotBlank 
    private String fromEmail;
	@NotBlank
    private String toEmail;
    private String ccEmail;
	@NotBlank
	private String subject;
	@NotBlank
	private String content;
	private String schema;
	private String filename;
	
	@NotNull
	private boolean SMTPStartTLSEnable;
	@NotNull
	private boolean SMTPHostTrusted;
	@NotNull
	private boolean htmlContent;

	//@Scheduled(cron = "*/5 * * * * *")
	public void automaticMailSending(){
		Logger logger = Logger.getLogger("Job");
        logger.setLevel(Level.INFO);
		logger.info("Email sent!");

	}

	public static void sendEmail(String smtpHost, int smtpPort, String mailUser, String mailUserPassword,
									boolean isSMTPHostTrusted, boolean isSMTPStartTLSEnable,    							
									String fromEmail, String toEmail, String ccEmail,
									String subject, String content, boolean htmlContent, Attachment... attachments)
									throws Exception {

		// Create a mail session
		Session session = getMailSession(smtpHost, smtpPort, mailUser, mailUserPassword, true, true);

		// Construct the message
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(fromEmail));
		msg = setTORecipients(msg, toEmail);
		if (!ccEmail.isEmpty()){
			msg = setCCRecipients(msg, ccEmail);
		}
		msg.setSubject(subject);
		if (attachments != null && attachments.length > 0 ) {
			addContentAndAttachments(msg, content, htmlContent, attachments);
		}
		else{
			msg.setContent(content, "text/html");
		}
				
		// Send the message
		Transport.send(msg);
		Logger logger = Logger.getLogger("Email");
        logger.setLevel(Level.INFO);
		logger.info("Email sent!");

}

	private static Session getMailSession(String smtpHost, int smtpPort, String mailUser, String mailUserPassword,
		boolean isSMTPStartTLSEnable, boolean isSMTPHostTrusted) {
		Session session = null;   

		java.util.Properties props = new java.util.Properties();
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", "" + smtpPort);
		//int timeout = 60000;
		//props.put("mail.smtp.timeout", timeout);
		if(isSMTPStartTLSEnable){
			props.put("mail.smtp.starttls.enable", "true");
		}

		if (isSMTPHostTrusted) {
			props.put("mail.smtp.ssl.trust", smtpHost);
		}

		if(!mailUser.isEmpty() || !mailUserPassword.isEmpty()){
			props.put("mail.smtp.user", mailUser);
			props.put("mail.smtp.auth", "true");
			Authenticator auth = new SMTPAuthenticator(mailUser, mailUserPassword);
			//session = Session.getDefaultInstance(props, auth);
			session = Session.getInstance(props, auth);
		} else {
			//session = Session.getDefaultInstance(props);
			session = Session.getInstance(props);
		}

		return session;
	}    

	private static class SMTPAuthenticator extends jakarta.mail.Authenticator {
	    private String fUser;
	    private String fPassword;

	    public SMTPAuthenticator(String user, String password) {
	        fUser = user;
	        fPassword = password;
	    }

	    public PasswordAuthentication getPasswordAuthentication() {
	        return new PasswordAuthentication(fUser, fPassword);
	    }
	}

	public static class Attachment {
		private String name;
		private File file;
		
		public Attachment(String name, File file){
			this.name = name;
			this.file = file;
		}
	}

	private static Message setTORecipients(Message msg, String emails) throws MessagingException {

        int countEmails;
        StringTokenizer emailList = new StringTokenizer(emails, ",");
        countEmails = emailList.countTokens();

        InternetAddress[] address = new InternetAddress[countEmails];
        for (int i = 0; i < countEmails; i ++) {
            address[i] = new InternetAddress(emailList.nextToken());
        }
        msg.setRecipients(Message.RecipientType.TO, address);
        return msg;
    }

	private static Message setCCRecipients(Message msg, String emails) throws MessagingException {
        int countEmails;
        StringTokenizer emailList = new StringTokenizer(emails, ",");
        countEmails = emailList.countTokens();

        InternetAddress[] address = new InternetAddress[countEmails];
        for (int i = 0; i < countEmails; i ++) {
            address[i] = new InternetAddress(emailList.nextToken());
        }
        msg.setRecipients(Message.RecipientType.CC, address);
        return msg;
    }
	
    private static void addContentAndAttachments(Message msg, String content, boolean htmlContent, Attachment... attachments) 
			throws MessagingException{
		
		Multipart multipart = new MimeMultipart();
		// content
		MimeBodyPart messagePart = new MimeBodyPart();				
		if (htmlContent){
			messagePart.setContent(content, "text/html"); //MESSAGE_CONTENT_TYPE
		}
		else{
			messagePart.setText(content);
		}
		multipart.addBodyPart(messagePart);
		// attachments
		for (int i = 0; i < attachments.length; i++){
			MimeBodyPart attachmentPart = new MimeBodyPart();
			FileDataSource fileData = new FileDataSource(attachments[i].file);
			DataHandler dh = new DataHandler(fileData);
			attachmentPart.setDataHandler(dh);
			attachmentPart.setFileName(attachments[i].name);
			
			multipart.addBodyPart(attachmentPart);	
		}
		//
		msg.setContent(multipart);
    }    
    
}