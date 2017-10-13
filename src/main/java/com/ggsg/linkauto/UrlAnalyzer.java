package com.ggsg.linkauto;

import java.util.logging.Level;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;



public class UrlAnalyzer {


	public static boolean disrespectTextPresentInUrl(String urlStr, String disText) {
        try
        {
            UrlReader url = new UrlReader(urlStr);
            Main.LOGGER.info("Starting analysis for url:  "+urlStr);
            String contents = url.read();    
            Document doc = null;
            if(contents != null) {
            	 doc = Jsoup.parse(contents);
            }else {
            	doc = Jsoup.connect(urlStr).get();
            }
            doc = new Cleaner(Whitelist.simpleText()).clean(doc);
            doc.outputSettings().escapeMode(EscapeMode.xhtml);            
            String cleanStr = doc.body().html();          
            Main.LOGGER.info("Cleaned content : "+cleanStr);   
            if(cleanStr.contains(disText.trim())){
            	 return true;
            }
        }
        catch (Exception e)
        {
           Main.LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
       return false;     
	}
	
	
	
	
	
	


}
