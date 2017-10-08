package com.ggsg.linkauto;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LinkAutoProcessor {

	final static Logger logger = Logger.getLogger("LinkAutoProcessor");
	public Properties prop = null;
	private Set<LinkVO> setLinks = null;
	private MailSender sender = null;
	
	private List <String> sentList = null;

	public void processExcel() {
		LinkExcelReader reader = new LinkExcelReader();
		setLinks = reader.returnLinksData();
		sentList = new ArrayList<String> ();
		sender = new MailSender();
		if (setLinks != null && setLinks.size() > 0) {

			for (LinkVO linkVO : setLinks) {

				if (linkVO.getUrl() != null && !"".equals(linkVO.getUrl().trim())) {
					try {
						UrlReader urlReader = new UrlReader(linkVO.getUrl());
						Main.LOGGER.info("READING URL :- "+linkVO.getUrl());
						String content = urlReader.read();
						if (content != null && content.contains(linkVO.getDisrepectText().trim())) {
							Main.LOGGER.info("URL not corrected : - "+linkVO.getUrl() +" For disrespect text : "+linkVO.getDisrepectText());
							if(sender.sendMail(linkVO)) {
								sentList.add(linkVO.getUrl());
							}

						}
					} catch (Exception e) {
						e.printStackTrace();
						Main.LOGGER.log(Level.SEVERE, e.getMessage(), e);
					}

				}

			}

		}
		
		if(sentList != null ) {
			sender.sendReportMail(sentList);
		}

	}

}
