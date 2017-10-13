package com.ggsg.linkauto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Main {

	public static Logger LOGGER = Logger.getLogger("LALA");
	public static Properties configProps = null;

	public static void main(String[] args) {
		if (args != null && args[0] != null) {
			configProps = new Properties();
			configureLogger();
			try {
				InputStream is = new FileInputStream(new File(args[0]));
				configProps.load(is);
				LOGGER.config("CONFIGURATION FILE LOADED.");
				//LinkAutoProcessor processor = new LinkAutoProcessor();
				//rocessor.processExcel();
				
			} catch (IOException e) {
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


		} catch (IOException exception) {

			LOGGER.log(Level.SEVERE, "Error occur in FileHandler.", exception);
		}

	}

}
