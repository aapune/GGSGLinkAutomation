package com.ggsg.linkauto;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SearchLinks {

	public static void main(String args[]) {
	
		try {
			String google = "http://www.google.com/search?q=";
			String search = "\"Tantrik named Gorakh Nath\"";	
			String charset = "UTF-8";
			String userAgent = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";


			//Elements links = Jsoup.connect(google + URLEncoder.encode(search, charset)).userAgent(userAgent).get().select(".g>.r>a");
			Elements links = Jsoup.connect(google + URLEncoder.encode(search, charset)).userAgent(userAgent).get().select("a[href]");
			int num= 0 ;
			for (Element link : links) {
				
			    String title = link.text();
			    String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
			    url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");

			    if (!url.startsWith("http")) {
			        continue; // Ads/news/etc.
			    }
			    
			    num++;
			    System.out.println("Title: " + title);
			    System.out.println("URL: " + url);
			    System.out.println(" Num : "+num);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
	
	
}
