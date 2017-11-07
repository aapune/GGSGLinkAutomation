package com.ggsg.linkauto;

import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;

public class LinkAutoProcessor {

	private MailSender sender = null;
	private final static String PENDING_STAT = "PENDING";
	private final static String INPROGRESS_STAT = "INPROGRESS"; 
	private final static String DONE_STAT = "DONE"; 		

	public String checkFlag = Main.configProps.getProperty("CHECK_LINK_CORRECTED");
	
	public void processLinkDataFromExcel(Set<LinkVO> setLinks) {
		sender = new MailSender();
		if (setLinks != null && setLinks.size() > 0) {

			for (LinkVO linkVO : setLinks) {

				if (linkVO.getUrl() != null && !"".equals(linkVO.getUrl().trim())) {

					if (linkVO.getLinkStatus() != null && PENDING_STAT.equalsIgnoreCase(linkVO.getLinkStatus().trim())) {
						try {							
							if (checkFlag != null && !"".equals(checkFlag.trim())) {
								if (checkFlag.trim().equalsIgnoreCase("yes")) {
									checkLinkContentAndSendMail(linkVO);
								} else if (checkFlag.trim().equalsIgnoreCase("no")) {
									sender.sendMail(linkVO);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							Main.LOGGER.log(Level.SEVERE, e.getMessage(), e);
							if (Main.errorList != null) {
								Main.errorList.add(linkVO.getUrl());
							}
						}

					} else if (linkVO.getLinkStatus() != null && INPROGRESS_STAT.equalsIgnoreCase(linkVO.getLinkStatus().trim())) {
						if(Main.inProgressList != null) {
							Main.inProgressList.add(linkVO.getUrl());
						}
					}else if (linkVO.getLinkStatus() != null && DONE_STAT.equalsIgnoreCase(linkVO.getLinkStatus().trim())) {
						if(Main.urlCorrectedList != null) {
							Main.urlCorrectedList.add(linkVO.getUrl());
						}
					}
				}

			}

		}

	}

	private void checkLinkContentAndSendMail(LinkVO linkVO) {
		if (UrlAnalyzer.disrespectTextPresentInUrl(linkVO.getUrl(), linkVO.getDisrepectText())) {
			Main.LOGGER.info(
					"URL not corrected : - " + linkVO.getUrl() + " For disrespect text : " + linkVO.getDisrepectText());
			sender.sendMail(linkVO);

		} else {
			if (Main.urlCorrectedList != null) {
				Main.urlCorrectedList.add(linkVO.getUrl());
			}
		}
	}


	public void processLinkDataFromGoogleSearch(Set<LinkVO> setLinks) {
		sender = new MailSender();
		Main.LOGGER.info("Size of searched set links ===========>" + setLinks.size());

		for (LinkVO link : setLinks) {
			Main.LOGGER.info("$$$$$$$$ Set links :" + link.getUrl() + " , " + Arrays.asList(link.getEmailIds()) + " , "
					+ link.getDisrepectText());
		}

		if (setLinks != null && setLinks.size() > 0) {

			for (LinkVO linkVO : setLinks) {

				if (linkVO.getUrl() != null && !"".equals(linkVO.getUrl().trim())) {

					try {
						if (checkFlag != null && !"".equals(checkFlag.trim())) {
							if (checkFlag.trim().equalsIgnoreCase("yes")) {
								checkLinkContentAndSendMail(linkVO);
							} else if (checkFlag.trim().equalsIgnoreCase("no")) {
								sender.sendMail(linkVO);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						Main.LOGGER.log(Level.SEVERE, e.getMessage(), e);
					}

				}

			}

		}

	}

}