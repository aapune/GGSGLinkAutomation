package com.ggsg.linkauto;

import java.util.Properties;
import java.util.logging.Level;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {
	
	
		private final static String MAIL_SUB_TEXT = "MAIL_SUBJECT_TEXT";
		private final static String NEWLINE = "<BR>";
	
	
		public boolean sendMail(LinkVO linkVO) {
			
			for(int i = 1; i<100 ; i++) {
				String mailId = "LINK_MAIL_ID"+i;
				String password = "LINK_MAIL_PASSWORD"+i;
				Main.LOGGER.info(" Gmail id:"+Main.configProps.getProperty(mailId)+" , Password:"+Main.configProps.getProperty(password));
				if(Main.configProps.getProperty(mailId) != null && Main.configProps.getProperty(password) != null) 
				{
					Session session = prepareConfiguration(Main.configProps.getProperty(mailId), Main.configProps.getProperty(password));
					Main.LOGGER.info(" Gmail id:"+Main.configProps.getProperty(mailId)+" SESSION CONFIGURED");
					boolean retVal = sendMailIndividually(Main.configProps.getProperty(mailId), session, linkVO);	
					if(retVal) {
						Main.mailSentList.add(linkVO.getUrl());
					}
				}else {
					break;
				}
			}
			return true;
		}


		private boolean sendMailIndividually(String username, Session session,LinkVO linkVO) {
			try {
				int index  = username.indexOf(".");
				Main.LOGGER.info("User Name : "+username);
				String name = username.substring(0, index);
				Main.LOGGER.info("Name extracted: "+name);
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(username));
				
				InternetAddress [] addresses = null;
				if(linkVO.getEmailIds() == null) {
					Main.LOGGER.info(" Email id is null so not sending mail for link : "+linkVO.getUrl());
					return false;
				}else {
					addresses = new InternetAddress[linkVO.getEmailIds().length];
					int i = 0;
	 				for(String toMail :linkVO.getEmailIds()) {
						if(toMail != null && !"".equals(toMail.trim())) {
							addresses[i] = new InternetAddress(toMail.trim());
							i++;
						}else {
							Main.LOGGER.info(" Email id is null so not sending mail for link  : "+linkVO.getUrl());
							return false;
						}
					}
				}
				
				message.setRecipients(Message.RecipientType.TO, addresses);
				message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(Main.configProps.getProperty("CC_TO_EMAIL1")));
				message.setSubject(Main.configProps.getProperty(MAIL_SUB_TEXT));
				StringBuffer text = new StringBuffer();
				text.append("Hello,"+ NEWLINE);
				text.append(NEWLINE);
				text.append("My name is "+name+" and "+Main.configProps.getProperty("MAIL_TEXT1")+ NEWLINE);
				text.append(NEWLINE);
				text.append("<B>"+Main.configProps.getProperty("MAIL_TEXT2")+"</B>"+NEWLINE );
				text.append(NEWLINE+Main.configProps.getProperty("MAIL_TEXT3")+ NEWLINE);
				text.append(linkVO.getUrl()+ NEWLINE);
				text.append(NEWLINE+Main.configProps.getProperty("MAIL_TEXT4")+ NEWLINE);
				text.append(NEWLINE);
				text.append("<B>"+Main.configProps.getProperty("MAIL_TEXT_DISRESPECT")+"</B>"+"&nbsp;"+ linkVO.getDisrepectText()+NEWLINE );
				text.append("<B>"+Main.configProps.getProperty("MAIL_TEXT_CORRECTION")+"</B>"+ "&nbsp;"+linkVO.getCorrectedText()+NEWLINE );	
				text.append(NEWLINE);
				text.append("<font color=red>"+Main.configProps.getProperty("MAIL_TEXT5")+"</font>"+ NEWLINE);
				text.append(NEWLINE);
				text.append(NEWLINE);
				text.append(Main.configProps.getProperty("MAIL_TEXT6")+ NEWLINE);
				text.append(NEWLINE);
				text.append(NEWLINE);
				text.append(Main.configProps.getProperty("MAIL_TEXT7")+ NEWLINE);
				text.append(NEWLINE);
				text.append(Main.configProps.getProperty("MAIL_TEXT8")+ NEWLINE);
				text.append(Main.configProps.getProperty("MAIL_TEXT9")+ NEWLINE);
				Main.LOGGER.info("MAIL CONTENT :- "+text.toString());
				message.setContent(text.toString(), "text/html");			
				Transport.send(message);
				Main.LOGGER.info("MAIL SENT SUCCESSFULLY  :- "+linkVO.getUrl());
				return true;

			} catch (Exception e) {
				e.printStackTrace();
				Main.LOGGER.log(Level.SEVERE, e.getMessage(), e);
				return false;
			}
		}


		private static Session prepareConfiguration(final String username, final String password) {
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");

			Session session = Session.getInstance(props,
			  new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			  });
			return session;
		}
		
		

	
	
}
