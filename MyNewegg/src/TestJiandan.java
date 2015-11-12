

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

public class TestJiandan implements PageProcessor{
	 
	   private Site site = Site
	        .me().setUserAgent("Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:10.0) Gecko/20100101 Firefox/10.0 ")
	        .setDomain("http://jandan.net/ooxx/page-1590#comments").setTimeOut(30000);
	   
	   private GetImage getImage;
	   
	   Integer count = 0;
	        

	   String LIST = "http://jandan\\.net/ooxx/page-[0-9]+#comments";
	   String PHOTO = "http://ww[0-9]\\.sinaimg\\.cn/mw600/[a-z0-9A-Z]+\\.jpg";
	  
	 

	 
	  public void process(Page page) {	    
	     Selectable selectUrl = page.getUrl();
	    
		 
	     if(selectUrl.regex(LIST).match()){
	     
		  Pattern pattern = Pattern.compile("http://ww[0-9]\\.sinaimg\\.cn/mw600/[a-z0-9A-Z]+\\.jpg");
	    	 Matcher matcher = pattern.matcher(page.getHtml().toString());
	    	 
	    	 List<String> list = new ArrayList<String>();
	    	 
	    	 while(matcher.find()) {
	    		   
	    		   //System.out.println(matcher.group());
	    		   
	    		   list.add(matcher.group());
	    		   
	    		  }
	    	 
	    	 for(int i=0;i<list.size();i++){
	    		 
	    		 System.out.println(list.get(i));
	    		 
	    	 }
	    	 
	    	 page.addTargetRequests(page.getHtml().links().regex(LIST).all());
	    	 
	    	 page.addTargetRequests(list);
		  
	  }

	  
	  else if(selectUrl.regex(PHOTO).match()){
		  
		  byte[] btImg = getImage.getImageFromNetByUrl(page.getUrl().toString());  
	         if(null != btImg && btImg.length > 0){  
	             System.out.println("读取到：" + btImg.length + " 字节");  
	             String fileName = count.toString()+".gif";
	             count++;
	             getImage.writeImageToDisk(btImg, fileName);  
	         }else{  
	             System.out.println("没有从该连接获得内容");  
	         }  
		  
		  
	  }
	  
	  }    
	    public Site getSite() {
		return site;
	    }//http://blog.sina.com.cn/lm/index.html
	    
	    public static void main(String[] args) {
	    	Spider.create(new TestJiandan())
            .addUrl("http://jandan.net/ooxx/page-1590#comments").thread(200)
            .pipeline(new ConsolePipeline())
            .run();
	    }
}