package com.ggsg.linkauto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.whois.WhoisClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author Aniruddha
 *
 */

public class SearchLinks {

		public static Set <LinkVO> getGoogleSearchedData(String disrepectText, String correctedText){	
		
			Set<LinkVO> searchSet = new HashSet<LinkVO>();
			String google = "http://www.google.com/search?q=";
			String search = "\"Tantrik named Gorakh Nath\"";
			String numIndex = "";
			String userAgent = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
			String[] suffixArray = {"htm" ,"html","aspx","asp","jsp","jspx"};
			List <String> suffixList = Arrays.asList(suffixArray);
			int num = 0;
			int googleSearchPages = 3;
			for (int i = 0; i < googleSearchPages; i++) 
			{
				if (i != 0) {
					numIndex = "&start=" + i * 10;
				}
				Elements links = null;
				try {
					links = Jsoup.connect(google + search + numIndex).userAgent(userAgent).get().select("h3.r > a");
				} catch (Exception e1) {
					Main.LOGGER.log(Level.SEVERE, e1.getMessage(), e1);
					break;
				}
				for (Element link : links) 
				{
					LinkVO linkObject = new LinkVO();
					String url = link.absUrl("href"); 
					try {
						url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");
					} catch (Exception e) {
						e.printStackTrace();
						Main.LOGGER.log(Level.SEVERE, e.getMessage(), e);
						continue;
					}
					if (!url.startsWith("http")) {
						continue; 
					}
					String suffix = url.substring(url.lastIndexOf(".")+1 , url.length());
					Main.LOGGER.info("Suffix ------- :"+suffix);
					num++;
					
					linkObject.setUrl(url.trim());
					Main.LOGGER.info("URL: " + url);
					System.out.println("Num : " + num);
					
					Elements linksUrls = null;
					try {
						linksUrls = Jsoup.connect(url).get().select("a[href]");
					} catch (Exception e) {
						e.printStackTrace();
						Main.LOGGER.log(Level.SEVERE, e.getMessage(), e);
						continue;
					}
					for (Element linkObj : linksUrls) {
						String insideUrl = linkObj.absUrl("href");
						
						if( (insideUrl.contains("contact") || insideUrl.contains("contactus")
								|| insideUrl.contains("Contact") || insideUrl.contains("Contactus")) 
								&& suffixList.contains(suffix)) {							
							Set <String> emails = getEmailsByUrl(insideUrl);
							Main.LOGGER.info(" Email : "+ emails);
							if(emails != null) {
								linkObject.setEmailIds((String [])emails.toArray());
							}						

						}
		
					}
				
					linkObject.setDisrepectText(disrepectText);
					linkObject.setCorrectedText(correctedText);
					
					
					if(linkObject.getUrl() != null && linkObject.getEmailIds() != null) {
						searchSet.add(linkObject);
					}
				}

			}
			return searchSet;
	}
	
	public static Set<String> getEmailsByUrl(String url) {
	    Document doc;
	    Set<String> emailSet = new HashSet<>();

	    try { 
	        doc = Jsoup.connect(url)
	                .userAgent("Mozilla")
	                .get();

	        Pattern p = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
	        Matcher matcher = p.matcher(doc.body().html());
	        while (matcher.find()) {
	            emailSet.add(matcher.group());
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return emailSet;
	}
	
	public static String getWhois(String domainName) {

		StringBuilder result = new StringBuilder("");

		WhoisClient whois = new WhoisClient();
		try {

			whois.connect(WhoisClient.DEFAULT_HOST);
			String whoisData1 = whois.query("=" + domainName);
			result.append(whoisData1);
			whois.disconnect();

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result.toString();

	}
	
	public static void getWhoisOther(String hostname) throws Exception{
		int c;
		Socket s = new Socket("internic.net", 43);
		InputStream in= s.getInputStream();
		OutputStream out= s.getOutputStream();
		String str= ("GET /whois.html HTTP/1.0\r\n\r\n");
		  
		byte buf[]= str.getBytes();
		out.write(buf);
		                out.flush();   
		  
		while((c = in.read())!= -1)
		{
		System.out.print((char) c);
		}
		  
		s.close();
		}


}
