package com.ggsg.linkauto;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {

	public static Logger LOGGER = Logger.getLogger(Main.class.getName());

	public static Properties configProps = null;

	public static Set<String> urlCorrectedList = null;
	public static Set<String> emailNotFoundList = null;
	public static Set<String> errorList = null;
	public static Set<String> mailSentList = null;
	public static Set<String> inProgressList = null;

	public static int NUM_OF_LINKS_FOUND = 0;

	public static boolean excelFlg = false;

	private static LinkAutoProcessor processor = null;

	public static void main(String[] args) {
		if (args != null && args[0] != null) {
			configProps = new Properties();
			configureLogger();
			urlCorrectedList = new HashSet<String>();
			emailNotFoundList = new HashSet<String>();
			errorList = new HashSet<String>();
			mailSentList = new LinkedHashSet<String>();
			inProgressList = new HashSet<String>();
			try {
				InputStream is = new FileInputStream(new File(args[0]));
				configProps.load(is);
				LOGGER.config("CONFIGURATION FILE LOADED.");

				if (args[1] != null && !"".equals(args[1].trim()) && "1".equalsIgnoreCase(args[1].trim())) {
					excelFlg = true;
					processor = new LinkAutoProcessor();
					processor.processLinkDataFromExcel(new LinkExcelReader().returnLinksData());

				} else if (args[1] != null && !"".equals(args[1].trim()) && "2".equalsIgnoreCase(args[1].trim())) {
					processor = new LinkAutoProcessor();
					processor.processLinkDataFromGoogleSearch(SearchLinks.getGoogleSearchedData(
							configProps.getProperty("SEARCH_TEXT1"), configProps.getProperty("CORRECT_TEXT1")));
				}
				ReportMailSender repMailer = new ReportMailSender();
				repMailer.sendReportMailByGmail();
				repMailer.sendReportMailByYahoo();

				LOGGER.info("Report : ");
				if (!excelFlg) {
					LOGGER.info("Google Search String : " + configProps.getProperty("SEARCH_TEXT1"));
					LOGGER.info("Number of pages searched : " + configProps.getProperty("GOOGLE_SEARCH_PAGES"));
					LOGGER.info("Number of links found : " + NUM_OF_LINKS_FOUND);
				}
				LOGGER.info(
						"LALA REPORT MAIL sent mail list size:" + Main.mailSentList.size() + " :" + Main.mailSentList);
				LOGGER.info("LALA REPORT MAIL  url corrected list size:" + Main.urlCorrectedList.size() + " :"
						+ Main.urlCorrectedList);
				LOGGER.info("LALA REPORT MAIL  email not found list size:" + Main.emailNotFoundList.size() + " :"
						+ Main.emailNotFoundList);
				LOGGER.info("LALA REPORT MAIL  error list size:" + Main.errorList.size() + " :" + Main.errorList);
				LOGGER.info("LALA REPORT MAIL  inprogress list size:" + Main.inProgressList.size() + " :"
						+ Main.inProgressList);

			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.log(Level.SEVERE, e.getMessage(), e);
			}

		}

	}

	private static void configureLogger() {
		Handler consoleHandler = null;
		Handler fileHandler = null;

		try {
			consoleHandler = new ConsoleHandler();
			fileHandler = new FileHandler("./linkautomation.log");
			LOGGER.addHandler(consoleHandler);
			LOGGER.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();
			consoleHandler.setFormatter(formatter);
			fileHandler.setFormatter(formatter);
			consoleHandler.setLevel(Level.ALL);
			fileHandler.setLevel(Level.ALL);
			LOGGER.setLevel(Level.ALL);
			LOGGER.config("LOGGER CONFIGURATION LOADED.");
			LOGGER.removeHandler(consoleHandler);

		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, "Error occur in FileHandler.", exception);
		}

	}

}
