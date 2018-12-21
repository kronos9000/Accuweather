import java.awt.Image;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebData {
	
	private static String webpage = null; //Contains the URL data
	
	//BufferedReader - Returns HTML contents of URL. Parameter consists of the url. 
	public static BufferedReader read(String url) throws Exception {

			return new BufferedReader(new InputStreamReader(new URL(url).openStream()));

		} //End of BufferedReader
	
	public static void main (String[] args) throws Exception {
				
		String inputState = JOptionPane.showInputDialog(null, "Enter State in US"); //State must be entered in its abreviation. For example: New York -> NY
		String inputCity = JOptionPane.showInputDialog(null, "Enter City in US");
		
		//String replaceText = inputCity;
		//inputCity.replace(' ', '-');
		if (inputCity.split(" ").length >= 2) {
			inputCity = inputCity.replace(' ', '-');
		}
		
		Pattern pattern = Pattern.compile(inputCity+ "(.?)(\\w*)(.?)(\\d{5})(.?)(weather-forecast)(.?)(\\d{1,10})");
		
		String webpage1 = "https://www.accuweather.com/en/browse-locations/nam/us/" + inputState;
		
		
		BufferedReader b = read(webpage1);
		String line = "";
		String data = "";
		String webpage2 = "";
		String searchResultsDisplay = "";
		String zipCode = "";
		String keyIdentifier = "";
		
		while ((line = b.readLine())!=null) {
			
			data += line;
		}
		
		String[] split = data.split("<li class=\"drilldown cl\" data-href=");
		
		String URLArray[] = new String[split.length];
		
		//Stores every URL in to array
		for (int i =1; i<split.length; i++) {
			
			int from = 1;
			int to = split[i].indexOf("\">                                    <div class=\"info\">                                        <h6><a href="); //Ending position
			URLArray[i] = split[i].substring(from, to) + "\n"; //Stores the text located between starting and ending position
			int from2 = 35;
			Matcher matcher1 = pattern.matcher(split[i].substring(from2, to));
			if (matcher1.find()){
				webpage2 = URLArray[i];
				zipCode = matcher1.group(4);
				keyIdentifier = matcher1.group(8);
			}
			
		}
		
//		Matcher matcher2 = breakdown.matcher(webpage2);
//		while (matcher2.find()){
//				zipCode = matcher2.group(2);
//				keyIdentifier = matcher2.group(3);
//				System.out.println(zipCode);
//				System.out.println(keyIdentifier);
//		}
		//System.out.println(webpage2);
		
		//Prints every URL for given state and city
		//for (int i =1; i < URLArray.length; i++) {
			
			//System.out.println("URL " + i + ": " + URLArray[i] + "\n"); //Stores the text located between starting and ending position
			
		//}
		
		BufferedReader b2 = read(webpage2);
		String line2 = "";
		String data2 = "";
		searchResultsDisplay = "";
		
		
		while ((line2 = b2.readLine())!=null) {
			
			data2 += line2;
		}
		
		String[] split2 = data2.split("large-temp");
		
		searchResultsDisplay = "\n----------Today's Forecast---------- \n\n\n";
		
		for (int i = 1; i<=4; i++) {
			
			if (i ==1) searchResultsDisplay += "---Current--- \n";
			//if (i ==2) searchResultsDisplay += "---Later--- \n";
			//if (i ==3) searchResultsDisplay += "---Today--- \n";
			//if (i ==4) searchResultsDisplay += "---Tonight--- \n";
			
			int tempFrom = 2;
			int tempTo = split2[i].indexOf("&deg"); //Ending position
			searchResultsDisplay += ("Temperature: " + split2[i].substring(tempFrom, tempTo) + "\n"); //Stores the text located between starting and ending position

			tempFrom = split2[i].indexOf("RealFeel&#174; "); //Starting position
			tempTo = split2[i].indexOf("&#176"); //Ending position
			searchResultsDisplay += ("Real Feel: " + split2[i].substring(tempFrom+15, tempTo) + "\n"); //Stores the text located between starting and ending position


			if (i==1) {
			
				tempFrom = split2[i].indexOf("cond"); //Starting position
				tempTo = split2[i].indexOf("</span>                                    </div>"); //Ending position
				searchResultsDisplay += ("Condition: " + split2[i].substring(tempFrom+6, tempTo) + "\n"); //Stores the text located between starting and ending position

			}
		
			//else {
			
				//tempFrom = split2[i].indexOf("cond"); //Starting position
				//tempTo = split2[i].indexOf("</span>                                            </div>"); //Ending position
				//searchResultsDisplay += ("Condition: " + split2[i].substring(tempFrom+6, tempTo) + "\n"); //Stores the text located between starting and ending position

			//}
			
			searchResultsDisplay+= "\n";
		
		}
		
		inputCity = inputCity.replace('-', ' ');
		System.out.println("State: " + inputState);
		System.out.println("City: " + inputCity);
		System.out.println("Zip Code: " + zipCode);
		System.out.println("Location Key: " + keyIdentifier);
		System.out.println(searchResultsDisplay);

		

	}
}
