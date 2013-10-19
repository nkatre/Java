package edu.sjsu.cmpe295b.planhercareer.util;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLParser {

	private static Set<String> storiesLink = new HashSet<String>();

	public static Set<String> extractLeanInStoryLink() {

		String baseURL =  "http://leanin.org/stories/page/";
		String url;

		for (int i = 1; i <= 46; i++) {
			
			url = baseURL+i;

			try {
				Document doc = Jsoup.connect(url).get();

				// get page title
				String title = doc.title();
				//System.out.println("title : " + title);

				// get all links
				Elements links = doc.select("a[href]");
				for (Element link : links) {

					// get the value from href attribute
					//System.out.println("\nlink : " + link.attr("href"));
					if ((link.attr("href").contains("http://leanin.org/stories/")) 
							&& (!link.attr("href").contains("catid"))
							&& (!link.attr("href").endsWith("stories/"))) {
						storiesLink.add(link.attr("href"));
					}
					//System.out.println("text : " + link.text());

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return storiesLink;
	}
	
	public static void main(String[] args) {
		System.out.println(extractLeanInStoryLink());
	}


}
