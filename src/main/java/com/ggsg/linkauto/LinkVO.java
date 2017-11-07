package com.ggsg.linkauto;

import java.io.Serializable;

public class LinkVO implements Serializable {
	
	/**
	 * serial verion uid
	 */
	private static final long serialVersionUID = 3576583324230427433L;
	private String url;
	private String [] emailIds;
	private String disrepectText;
	private String correctedText;
	private String linkStatus;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String[] getEmailIds() {
		return emailIds;
	}
	public void setEmailIds(String[] emailIds) {
		this.emailIds = emailIds;
	}
	public String getDisrepectText() {
		return disrepectText;
	}
	public void setDisrepectText(String disrepectText) {
		this.disrepectText = disrepectText;
	}
	public String getCorrectedText() {
		return correctedText;
	}
	public void setCorrectedText(String correctedText) {
		this.correctedText = correctedText;
	}
	public String getLinkStatus() {
		return linkStatus;
	}
	public void setLinkStatus(String linkStatus) {
		this.linkStatus = linkStatus;
	}
	
}
