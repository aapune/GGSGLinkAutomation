package com.ggsg.linkauto;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ReportMailSender {

	private final static String NEWLINE = "<BR>";

	public boolean sendReportMailByGmail() {
		String lalamailId = "LALA_MAIL_ID1";
		String lalapassword = "LALA_MAIL_PASSWORD2";
		Session session = prepareConfiguration(Main.configProps.getProperty(lalamailId).trim(),
				Main.configProps.getProperty(lalapassword).trim());
		try {

			StringBuffer text = getMailContent();

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(lalamailId.trim()));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(Main.configProps.getProperty("CC_TO_EMAIL1").trim()));
			message.setSubject("Link status report from LALA The Link Bot,  Date : " + new Date());
			message.setContent(text.toString(), "text/html");
			Transport.send(message);
			Main.LOGGER.info(
					"LALA REPORT MAIL sent mail list size:" + Main.mailSentList.size() + " :" + Main.mailSentList);
			Main.LOGGER.info("LALA REPORT MAIL  url corrected list size:" + Main.urlCorrectedList.size() + " :"
					+ Main.urlCorrectedList);
			Main.LOGGER.info("LALA REPORT MAIL  email not found list size:" + Main.emailNotFoundList.size() + " :"
					+ Main.emailNotFoundList);
			Main.LOGGER.info("LALA REPORT MAIL  error list size:" + Main.errorList.size() + " :" + Main.errorList);
			return true;

		} catch (Exception e) {
			Main.LOGGER.log(Level.SEVERE, e.getMessage(), e);
			e.printStackTrace();
			return false;
		}

	}

	private StringBuffer getMailContent() {
		StringBuffer text = new StringBuffer();
		text.append("Report Mail : " + NEWLINE);
		text.append(NEWLINE);
		
		if(!Main.excelFlg) {
			text.append("Google Search String : " + Main.configProps.getProperty("SEARCH_TEXT1") + NEWLINE);
			text.append(NEWLINE);
			text.append("Number of pages searched : " + Main.configProps.getProperty("GOOGLE_SEARCH_PAGES") + NEWLINE);
			text.append(NEWLINE);
			text.append("Number of links found : " + Main.NUM_OF_LINKS_FOUND + NEWLINE);
		}
		text.append(NEWLINE);
		if (Main.mailSentList != null && Main.mailSentList.size() > 0) {
			text.append("For below listed url's mail sent to concerned mail id's : " + NEWLINE);
			text.append(NEWLINE);
			int i = 0;
			for (String url : Main.mailSentList) {
				i++;
				text.append(i + ")" + url + NEWLINE);
				text.append(NEWLINE);

			}
			text.append(NEWLINE);
			text.append(NEWLINE);

		}

		if (Main.urlCorrectedList != null && Main.urlCorrectedList.size() > 0) {
			text.append("Corrected URL list : " + NEWLINE);
			text.append(NEWLINE);
			text.append(
					"For below listed url's disrespect text is not found ,please check manually and if disrespect present add from excel: "
							+ NEWLINE);
			text.append(NEWLINE);
			int i = 0;
			for (String urlO : Main.urlCorrectedList) {
				i++;
				text.append(i + ")" + urlO + NEWLINE);
				text.append(NEWLINE);

			}
			text.append(NEWLINE);
			text.append(NEWLINE);
		}
		
		if (Main.inProgressList != null && Main.inProgressList.size() > 0) {
			text.append("Inprogress URL list : " + NEWLINE);
			text.append(NEWLINE);
			text.append(
					"For below listed url's link resolution is in progress: "
							+ NEWLINE);
			text.append(NEWLINE);
			int i = 0;
			for (String urlO : Main.inProgressList) {
				i++;
				text.append(i + ")" + urlO + NEWLINE);
				text.append(NEWLINE);

			}
			text.append(NEWLINE);
			text.append(NEWLINE);
		}

		if (Main.emailNotFoundList != null && Main.emailNotFoundList.size() > 0) {
			text.append("EMAIL not found for below URL list : " + NEWLINE);
			text.append(NEWLINE);
			text.append(
					"For below listed url's email not found ,please check manually and if disrespect present add from excel: " + NEWLINE);
			text.append(NEWLINE);
			int i = 0;
			for (String urlE : Main.emailNotFoundList) {
				i++;
				text.append(i + ")" + urlE + NEWLINE);
				text.append(NEWLINE);

			}
			text.append(NEWLINE);
			text.append(NEWLINE);
		}

		if (Main.errorList != null && Main.errorList.size() > 0) {
			text.append("Error occured while checking below URL list : " + NEWLINE);
			text.append(NEWLINE);
			text.append(
					"For below listed url's error occured while check ,please check manually and if disrespect present add from excel: "
							+ NEWLINE);
			text.append(NEWLINE);
			int i = 0;
			for (String urlEr : Main.errorList) {
				i++;
				text.append(i + ")" + urlEr + NEWLINE);
				text.append(NEWLINE);

			}
			text.append(NEWLINE);
			text.append(NEWLINE);
		}

		text.append(NEWLINE);
		text.append(NEWLINE);
		text.append("Regards," + NEWLINE);
		text.append("Your LALA - The Link Bot");
		return text;
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

	public void sendReportMailByYahoo() {
		String lalamailId = "LALA_MAIL_ID2";
		String lalapassword = "LALA_MAIL_PASSWORD2";
		String from = Main.configProps.getProperty(lalamailId);
		String pass = Main.configProps.getProperty(lalapassword);
		String to = Main.configProps.getProperty("CC_TO_EMAIL1");

		String host = "smtp.mail.yahoo.com";

		Properties properties = System.getProperties();
		// Setup mail server
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.user", from.trim());
		properties.put("mail.smtp.password", pass.trim());
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(properties);

		try {
			MimeMessage message = new MimeMessage(session);

			message.setFrom(new InternetAddress(from));

			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			message.setSubject("Link status report from LALA The Link Bot,  Date : " + new Date());

			message.setContent(getMailContent().toString(), "text/html");

			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			Main.LOGGER.info(
					"LALA REPORT MAIL sent mail list size:" + Main.mailSentList.size() + " :" + Main.mailSentList);
			Main.LOGGER.info("LALA REPORT MAIL  url corrected list size:" + Main.urlCorrectedList.size() + " :"
					+ Main.urlCorrectedList);
			Main.LOGGER.info("LALA REPORT MAIL  email not found list size:" + Main.emailNotFoundList.size() + " :"
					+ Main.emailNotFoundList);
			Main.LOGGER.info("LALA REPORT MAIL  error list size:" + Main.errorList.size() + " :" + Main.errorList);
		} catch (Exception mex) {
			mex.printStackTrace();
		}
	}

}
