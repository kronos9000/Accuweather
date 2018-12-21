import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

//Accuweather Class - Stores data for temperatures of regions
public class AccuweatherItem {
	
	
	public String State = ""; //Stores State
	public String City = ""; //Stores City
	public String Zip = ""; //Stores ZipCode
	public String Key = ""; //Stores Unique Identifier
	public String CurrentTemp = ""; //Stores CurrentTemp
	public String RealFeel =""; //Stores RealFeel
	public String Cond =""; //Stores Current Condition
	public static BufferedImage image = null; //Stores the image of weather condition
	public String imageURL = " "; //Stores the image URL
	public String URL = ""; //Stores the URL
	
	/**
	 * Stores values of particular Accuweather item
	 * 
	 * @param State - state item to be stored
	 * @param City - city item to be stored
	 * @param Zip - zipcode item to be stored
	 * @param Key - zipcode item to be stored
	 * @param CurrentTemp - current temperature to be stored
	 * @param RealFeel - real feel of temperature to be stored
	 * @param Cond - condition to be stored 
	 * @param Image - image of item to be stored
	 */
	 
	 public AccuweatherItem(String Zip, String City, String State, String Key, String CurrentTemp, String RealFeel, String Cond) {
		this.Zip = Zip; //Stores ZipCode
		this.City = City; //Stores City
		this.State = State; //Stores State
		this.Key = Key; //Stores Key
		this.CurrentTemp = CurrentTemp; //Stores CurrentTemp
		this.RealFeel = RealFeel; //Stores RealFeel
		this.Cond = Cond; //Stores Cond
	}
	
	//Get & Set Methods
	public String getState() {
		return this.State;	
	}
	
	public void setState(String State) {
		this.State = State;	
	}
	
	public String getCity() {
		return this.City;	
	}
	
	public void setCity(String City) {
		this.City = City;	
	}
	
	public String getZip() {
		return this.Zip;	
	}
	
	public void setZip(String Zip) {
		this.Zip = Zip;	
	}
	
	public String getKey() {
		return this.Key;	
	}
	
	public void setKey(String Key) {
		this.Key = Key;	
	}
	
	public String getCurrentTemp() {
		return this.CurrentTemp;	
	}
	
	public void setCurrentTemp(String CurrentTemp) {
		this.CurrentTemp = CurrentTemp;	
	}
	
	public String getRealFeel() {
		return this.RealFeel;	
	}
	
	public void setRealFeel(String RealFeel) {
		this.RealFeel = RealFeel ;	
	}
	
	public String getCond() {
		return this.Cond;	
	}
	
	public void setCond(String Cond) {
		this.Cond = Cond;	
	}
	
	public String getImageURL() {
		return this.imageURL;
	}
	
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	public String getURL() {
		return this.URL;
	}
	
	public void setURL(String URL) {
		this.URL = URL;
	}
	
	//To String Methods
	public String toString() {
		return ("ZipCode: " + Zip + "\n" + "City: " + City + "\n"+ "State: " + State + "\n" + "Location Key: " + Key + "\n" +  "Current Temp: " + CurrentTemp + "\n" + "Real Feel: " + RealFeel + "\n" + "Condition: " + Cond + "\n");		
	}
	
	public String toStringRaw() {
		return (Zip + "," + City + "," + State  + "," + Key + "," +CurrentTemp + "," + RealFeel + "," + Cond + "," + imageURL);		
	}
	
	public String toStringTransactionLog() {
		return ("ZipCode: " + Zip + "|" + "City: " + City + "|"+ "State: " + State + "|" + "Location Key: " + Key + "|" +  "Current Temp: " + CurrentTemp + "|" + "Real Feel: " + RealFeel + "|" + "Condition: " + Cond );		
	}

}
