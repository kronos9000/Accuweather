import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable; //library for Hash Table
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

//Accuweather class - Main class for program which sets up global variables and initializes the hash table. 
public class Accuweather {
	
	//Global Variables 
	public String zipCode=""; //Variable for zip code
	public String City=""; //Variable for City 
	public String State=""; //Variable for State
	public String Key=""; //Variable for location Key
	public String CurrentTemp=""; //Variable for current temperature
	public String RealFeel=""; //Variable for real feel
	public String cond=""; //Variable for condition
	
	public String toString = ""; //Variable that holds information of every item in the hash table to display it.
	public String toStringRaw = ""; //Variable that holds raw data of every item without specifying its attribute. 
	public String transactionLogData = ""; //Variable that contains all data for all transactions including the date/time.
	public boolean privilege = false; //Variable used as a key to determine the privileges of user depending on whether they're an administrator or not.
	
	public ArrayList <String> hashKeys = new ArrayList<String>(); //Keeps track of which keys in the hash table are filled
	public AccuweatherItem currentItem = new AccuweatherItem (zipCode, City, State, Key, CurrentTemp, RealFeel, cond); //Object that will be used to temporarily hold data for each item. This object then gets added to the hash table.
	public Hashtable <String, AccuweatherItem> itemList = new Hashtable <String, AccuweatherItem>(); //Hash table to store information about each item
	
	public boolean administratorPrivilege = false; //Key used to determine that the administrator is logged in so that they can use their additional privileges that involve modifying data. 
	public boolean userPrivilege = false; //Key used to determine that the user is logged in so that they can only perform a limited amount of activities and cannot modify data. 
	
	public String[][] rowString; //2-dimensional array to hold item data and display it in the table for the GUI.
	
	//readBackUp Method - Opens text file, reads, and stores data of a particular item in to the hash table. 
	void readBackUp() {

		//Try block - Opens, reads, and stores data of text file
		try {
			
			FileReader f = new FileReader("backUpData.txt"); //Text file is opened and stored in to f
			BufferedReader b = new BufferedReader(f); 
			String line = "";
			
			//while loop - Goes through each line of text file with the condition that there are more lines
			while ((line = b.readLine())!=null) { 
				
				StringTokenizer tokens = new StringTokenizer(line,","); //Each line is tokenized by a comma
				
				currentItem= new AccuweatherItem("","","","","","","");
				if (tokens.hasMoreTokens()) currentItem.setZip(tokens.nextToken()); //Inserts item # in to currentItem object
				if (itemList.get(currentItem.getZip())!=null) continue;
				
				if (tokens.hasMoreTokens()) currentItem.setCity(tokens.nextToken()); //Sets City 
				if (tokens.hasMoreTokens()) currentItem.setState(tokens.nextToken()); //Sets State
				if (tokens.hasMoreTokens()) currentItem.setKey(tokens.nextToken()); //Sets Key 
				if (tokens.hasMoreTokens()) currentItem.setCurrentTemp(tokens.nextToken()); //Sets Current Temp
				if (tokens.hasMoreTokens()) currentItem.setRealFeel(tokens.nextToken());//Sets Real feel
				if (tokens.hasMoreTokens()) currentItem.setCond(tokens.nextToken());//Sets weather conditions
				
				itemList.put(currentItem.getZip(), currentItem); //Inserts current item and data for its attributes in to hash table
				hashKeys.add(currentItem.getZip()); //Inserts hash key in to array to keep track. 
				
				timeStamp("Inserting Backup Data ", currentItem); //This transaction gets recorded with a time stamp.
//				backUpData();
				
			} //End of while loop
			b.close(); //Closes buffer
		} //End of try block
		
		//Catch block for FileNotFoundException
		catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //End of Catch block
		
	} //End of readFile Method
	
	//offlineQuery Method - Allows users/administrators to view data without being connected to the internet. 
	String offlineQuery(String zipCodeIn) {
		
		//Prevents users from entering blank
		if (zipCodeIn.equals("")) {
				JOptionPane.showMessageDialog(null, "You must enter the Zip Code in its corresponding field.");
				return null;
		}//End of if statement
		
		//If item number doesn't exist, user will be notified.
		else if (itemList.get(zipCodeIn)==null) {
			JOptionPane.showMessageDialog(null, "No search result for item you've entered.");
			return null;
		}//End of if statement
		
		return itemList.get(zipCodeIn).toString(); //Returns information about item if the number exists in the hashtable.
		
	}
	
	 //addItem Method - Allows administrators to add items in to hash table. 
	 void addItem(String zipCodeIn, String CityIn, String StateIn, String KeyIn, String CurrentTempIn, String RealFeelIn, String CondIn) {
		
		//No field can be left blank in order to add an item
		if (zipCodeIn.equals("")||CityIn.equals("")||StateIn.equals("")||KeyIn.equals("")||CurrentTempIn.equals("")||RealFeelIn.equals("")||CondIn.equals("")) {
			
			JOptionPane.showMessageDialog(null, "Error! Fill in all fields.");
			return;
			
		} //End of if statement
		
		//
//	    for (String i : itemList.keySet()) {
//			
//			if (zipCodeIn.equals(itemList.get(i).getZip())) {
//				JOptionPane.showMessageDialog(null, "Error! Item with number " + zipCodeIn + " already exists.");
//				return;
//			}
//			
//		}
		
		//If the item number already exists, administrator will be notified.
		if (itemList.get(zipCodeIn)!=null) {
			JOptionPane.showMessageDialog(null, "Error! Item with number " + zipCodeIn + " already exists.");
			return;
		} //End of if statement
		
		currentItem = new AccuweatherItem (null, null, null, null, null, null, null); //Object to temporarily hold data of item to be added
		
		currentItem.setZip(zipCodeIn); //Stores Zip Code
		currentItem.setCity(CityIn); //Stores City
		currentItem.setState(StateIn); //Stores State
		currentItem.setKey(KeyIn); //Stores Key
		currentItem.setCurrentTemp(CurrentTempIn); //Stores Current Temp
		currentItem.setRealFeel(RealFeelIn); //Stores Real Feel
		currentItem.setCond(CondIn); //Stores Weather Condition
		
		itemList.put(currentItem.getZip(), currentItem); //Inserts data in to hash table
//		hashKeys.add(currentItem.getZip()); //Inserts key in to array to keep track
		
		timeStamp("Item Added By Admin", currentItem); //Creates time stamp of activity
		JOptionPane.showMessageDialog(null, "Item with number " + currentItem.getZip() + " succesfully added.");
		backUpData(); //Backs up data for data persistence 
				
	} //End of addItem Method
	
	//editItem method - Allows administrators to edit items. 
	public void editItem(String zipCodeIn, String CityIn, String StateIn, String KeyIn, String CurrentTempIn, String RealFeelIn, String CondIn) {
		
		//Zip Code field cannot be left blank
		if (zipCodeIn.equals("")) {
			JOptionPane.showMessageDialog(null, "You must enter the zip code for the region you wish to edit!");
			return;
		} //End of if statement
		
		//Zipcode desired to be edited must already exist in the hash table
		if (itemList.get(zipCodeIn)==null) {
			JOptionPane.showMessageDialog(null, "Error! Zip Code you wish to modify doesn't exist!");
			return;
		}// End of if statement 
		
		//At least one of these fields need to be filled in
		if (CityIn.equals("")&&StateIn.equals("")&&KeyIn.equals("")&&CurrentTempIn.equals("")&&RealFeelIn.equals("")&&CondIn.equals("")) { 
			JOptionPane.showMessageDialog(null, "You must fill in at least one more of the above fields to edit an region!");
			return;
		} //End of if statement
		
		//If any of tabs are not empty, then the hash table will store these new values for the attributes of the item desired to be modified.
		if (!CityIn.equals(""))
			itemList.get(zipCodeIn).setCity(CityIn);
		if (!StateIn.equals(""))
			itemList.get(zipCodeIn).setState(StateIn);
		if (!KeyIn.equals(""))
			itemList.get(zipCodeIn).setKey(KeyIn);
		if (!CurrentTempIn.equals(""))
			itemList.get(zipCodeIn).setCurrentTemp(CurrentTempIn);
		if (!RealFeelIn.equals(""))
			itemList.get(zipCodeIn).setRealFeel(RealFeelIn);
		if (!CondIn.equals(""))
			itemList.get(zipCodeIn).setCond(CondIn);
		
		timeStamp("Item Edited by Admin", itemList.get(zipCodeIn)); //Creates time stamp of Editing activity
		JOptionPane.showMessageDialog(null, "Item " + zipCodeIn + " has succesfully been edited");
		backUpData(); //Data backed up
	
	}

	//deleteItem Method - Allows administrators to delete items from hash table. Parameter consists of item number for item that is desired to be deleted.
	public void deleteItem(String delete) {
		
		boolean success = false; //Indicates if an item was successfully deleted
		
		if (itemList.get(delete)!=null) {
			
			timeStamp("Item Removed by Admin", itemList.get(delete)); //Time stamp of activity is recorded
			itemList.remove(delete); // Item is removed from hash table
			JOptionPane.showMessageDialog(null, "Regional Weather Forecast for " + delete + " has succesfully been deleted.");
			success = true; //sets variable to true since it has been deleted
			backUpData();
		}
		
		if (success==false) 	JOptionPane.showMessageDialog(null, "Error! Regional Weather Forecast for " + delete + " does not exist!");	
		
					
//		boolean success = false; //Indicates if an item was successfully deleted
//
//		//for loop - goes through hash table to find specified item in order to remove it
//		for (int k=0;k<hashKeys.size();k++) {
//			
//			//if statement - if hash key for specified item is found, item will be deleted
//			if(hashKeys.get(k).equals(delete)) { 
//				timeStamp("Item Removed ", itemList.get(hashKeys.get(k))); //Time stamp of activity is recorded
//				itemList.remove(hashKeys.get(k)); // Item is removed from hash table
//				hashKeys.remove(k); //Key is removed from array
//				JOptionPane.showMessageDialog(null, "Item with number " + delete + " has succesfully been deleted");
//				success = true; //sets variable to true since it has been deleted
//				break;				
//
//			} //End of if statement
//		} //End of for loop
//		
//		if (success==false) 	JOptionPane.showMessageDialog(null, "Error! Item with number " + delete + " does not exist!");	//If item doesn't exist, user will be notified
		
	} //End of deleteItem Method

	//toStringM Method - Stores data for every item in to a variable that will be used to display it. 
	public void toStringM() {
		toString = "";
		//Prints each item and its information
	    for (String i : itemList.keySet()) 
			toString += itemList.get(i).toString() + "\n";
		
//		JOptionPane.showMessageDialog(null, "Display Items: \n\n" + toString);
	} //End of Display Method
	
	//toStringRawM Method - Stores raw data of each item in to a variable 
	public void toStringRawM() {
		
		toStringRaw = ""; //Variable that stores raw data
		//Prints each item and its information
	    for (String i : itemList.keySet()) 
			toStringRaw += itemList.get(i).toStringRaw() + "\n";
		
//		JOptionPane.showMessageDialog(null, "Display Items: \n\n" + toString);
	} //End of displayRaw Method
	
	//timeStamp Method - Creates time stamp for an activity when it's called on and stores the time and activity data in to the transaction log. Parameters include City of activity and item data.
	public void timeStamp(String activity, AccuweatherItem item) {		
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); //Specifies the format of the data for an activity. 
		Date date = new Date();
		
	    BufferedWriter writer = null;
		
		try {
			
	        writer = new BufferedWriter(new FileWriter("transactionLog.txt", true)); //Text file to store back up data is created and opened
		
			if (item == null) {
				transactionLogData += dateFormat.format(date) + "	 All Data From System Deleted." + "\n";
				writer.write(dateFormat.format(date) + "	All Data From System Deleted." + "\n"); //Backed up data is written in to text file.
		
			}
		
			else {
				transactionLogData += dateFormat.format(date) + "      Activity: " + activity + " | " + item.toStringTransactionLog() + "\n"; //Stores the time stamp and activity data in to the transactionLog variable. 
				writer.write(dateFormat.format(date) + "      Activity: " + activity + " | " + item.toStringTransactionLog() + "\n");
			}
		
			writer.close();  //Text file is closed
		}
		
	    catch (IOException e) {
	        System.err.println(e);
	    } 
		
	} //End of timeStamp method.
	
	//backUpData Method - Stores data in to a text file so that even when the system is terminated, data will not be lost and will be stored back in to the hash table.
	public void backUpData() {

		toStringRaw = "";
		
		//for loop - Stores data for each item in to toString variable
	    for (String i : itemList.keySet()) 
			toStringRaw += itemList.get(i).toStringRaw() + "\n";
		
	    BufferedWriter writer = null;
	    
	    //Writes data to text file
	    try {
	        writer = new BufferedWriter(new FileWriter("backUpData.txt")); //Text file to store back up data is created and opened
	        writer.write(toStringRaw); //Backed up data is written in to text file.
	        writer.close();  //Text file is closed
	        
	    } //End of try block
	    
	    catch (IOException e) {
	        System.err.println(e);
	    } 
	    
	} //End of backUpData Method
	
	//insertBackUpData Method - Gets data from text file and stores it in to the hash table when the system first starts running for persistence of data.
	public void insertBackUpData() {
		
		try {
			
			FileReader f = new FileReader("backUpData.txt"); //Text file is opened and stored in to f
			BufferedReader b = new BufferedReader(f); 
			String line = "";
			
			// while loop - Goes through each line of text file with the condition that there are more lines
			while ((line = b.readLine())!=null) { 
				
				
				StringTokenizer tokens = new StringTokenizer(line,","); //Each line is tokenized by a comma
				
				currentItem= new AccuweatherItem("","","","","","","");
				if (tokens.hasMoreTokens()) currentItem.setZip(tokens.nextToken()); //Stores Zip
				if (tokens.hasMoreTokens()) currentItem.setCity(tokens.nextToken()); //Stores City 
				if (tokens.hasMoreTokens()) currentItem.setState(tokens.nextToken()); //Stores State
				if (tokens.hasMoreTokens()) currentItem.setKey(tokens.nextToken()); //Stores Key 
				if (tokens.hasMoreTokens()) currentItem.setCurrentTemp(tokens.nextToken()); //Stores Current Temp
				if (tokens.hasMoreTokens()) currentItem.setRealFeel(tokens.nextToken());//Store Real Feel
				if (tokens.hasMoreTokens()) currentItem.setCond(tokens.nextToken()); //Store Condition
				
				itemList.put(currentItem.getZip(), currentItem); //Inserts current item and data for its attributes in to hash table
				
				timeStamp("Backup Data Inserted: ", currentItem);  //This activity is recorded along with its date/time.
//				backUpData();
				
			} // End of while loop
			b.close(); //Closes buffer
		} //End of try block
		
		//Catch block for FileNotFoundException
		catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //End of Catch block	
	}
	
	public void insertSearchedData(String in, String searched, String searched1) {
		
//		StringTokenizer text = new StringTokenizer(in, "\n");
//		
//		while(text.hasMoreTokens()) {
//			
//			String line = text.nextToken();
//			
//			StringTokenizer tokens = new StringTokenizer(line, ",");
//			
//			currentItem= new AccuweatherItem("","","","","");
//			currentItem.setNumber(tokens.nextToken()); //Stores item number in to currentItem object
//			currentItem.setCity(tokens.nextToken()); //Stores City of item
//			currentItem.setWear(tokens.nextToken()); //Stores type of wear of item
//			currentItem.setKey(tokens.nextToken()); //Stores Key 
//			currentItem.setCurrentTemp(tokens.nextToken()); //Stores current temp
//			currentItem.setImageURL(tokens.nextToken()); //Stores image URL
//			
//			itemList.put(currentItem.getZip(), currentItem); //Inserts current item and data for its attributes in to hash table
//			
//			timeStamp("Searched Data Inserted: ", currentItem);  //This activity is recorded along with its date/time.
//			backUpData();
//			
//		}
		
		//--------------------------------------
		
	    BufferedWriter writer = null;
	    String line = "";
	    
	    //Writes data to text file
	    try {
	        writer = new BufferedWriter(new FileWriter("searchedData.txt")); //Text file to store back up data is created and opened
	        writer.write(in); //Backed up data is written in to text file.
	        writer.close();  //Text file is closed
	        
	    } //End of try block
	    
	    catch (IOException e) {
	        System.err.println(e);
	    } 
	    
		try {
			
			FileReader f = new FileReader("searchedData.txt"); //Text file is opened and stored in to f
			BufferedReader b = new BufferedReader(f); 
			
			// while loop - Goes through each line of text file with the condition that there are more lines
			while ((line = b.readLine())!=null) { 
				
				StringTokenizer tokens = new StringTokenizer(line,","); //Each line is tokenized by a comma
				
				currentItem= new AccuweatherItem("","","","","","","");
				if (tokens.hasMoreTokens()) currentItem.setZip(tokens.nextToken()); //Stores Zip
//				if (itemList.get(currentItem.getZip())!=null) continue;
				
				
				if (tokens.hasMoreTokens()) currentItem.setCity(tokens.nextToken()); //Stores City 
				if (tokens.hasMoreTokens()) currentItem.setState(tokens.nextToken()); //Stores State
				if (tokens.hasMoreTokens()) currentItem.setKey(tokens.nextToken()); //Stores Key 
				if (tokens.hasMoreTokens()) currentItem.setCurrentTemp(tokens.nextToken()); //Stores Current Temp
				if (tokens.hasMoreTokens()) currentItem.setRealFeel(tokens.nextToken()); //Stores Real Feel
				if (tokens.hasMoreTokens()) currentItem.setCond(tokens.nextToken()); //Stores Condition
				
//				System.out.print("Here: " + currentItem.getImageURL() + "\n");
						
				itemList.put(currentItem.getZip(), currentItem); //Inserts current item and its attribute data in to hash table
				
				timeStamp("Searched Results for \"" + searched + "\" Inserted", currentItem);  //This activity is recorded along with its date/time.
				backUpData(); //Data backed up for persistence 
				
			} // End of while loop
			b.close(); //Closes buffer
		} //End of try block
		
		//Catch block for FileNotFoundException
		catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //End of Catch block	
		
	}
	
	//Login Method - Checks to see if users/administrators entered to correct login name with its matching password. This allows users to proceed with the system turns on certain features/privileges depending on the type of user. Parameters are the username and password entered.
	public void Login(String username, String Password ){
		
			//Sets userPrivilege variable to true if a user successfully logs in with correct login City/password
			if (username.equals("user") && Password.equals("0000")) {
				userPrivilege = true;
			}
		
			//Sets administratorPrivilege variable to true if an administrator successfully logs in with correct login City/password
			else if (username.equals("administrator") && Password.equals("1111")) {
				administratorPrivilege = true;
			}
		
//			else JOptionPane.showMessageDialog(null, "Error! Login City does not match with password.");
		
	}//End of Login Method
	
	public void deleteAllSystemData() {
		
	    BufferedWriter writer = null;
	    
	    //Writes data to text file
	    try {
	        writer = new BufferedWriter(new FileWriter("searchedData.txt")); //Text file to store back up data is created and opened
	        writer.write(""); //Backed up data is written in to text file.
	        writer.close();  //Text file is closed
	        
	    } //End of try block
	    
	    catch (IOException e) {
	        System.err.println(e);
	    } 
	    
	    //Writes data to text file
	    try {
	    	
	        writer = new BufferedWriter(new FileWriter("backUpData.txt")); //Text file to store back up data is created and opened
	        writer.write(""); //Backed up data is written in to text file.
	        writer.close();  //Text file is closed
	        
	    } //End of try block
	    
	    catch (IOException e) {
	        System.err.println(e);
	    } 
		
	    itemList = new Hashtable <String, AccuweatherItem>();
		timeStamp("All Data Deleted from System ", null);  //This activity is recorded along with its date/time.
			
	}
	
	//createRows Method - Stores data for each item from hash table in to a 2-dimensional array to display it in the table of the GUI.
	public void createRows() {
		
		String[][] rowString = new String[itemList.keySet().size()][7]; //2-dimensional array to hold item data. Each attribute will be stored in a separate cell
		
		//for loop - goes through each item in hash table then extracts and stores each items attributes in to a 2-dimensional array. 
		 for (String i : itemList.keySet())  { int j=0;
				rowString[j][0] = itemList.get(i).getZip(); //First cell holds zip code
				rowString[j][1] = itemList.get(i).getCity(); //Second cell holds City
				rowString[j][2] = itemList.get(i).getState(); //Third cell holds State
				rowString[j][3] = "$" + itemList.get(i).getKey(); //Fourth cell holds Location Key
				rowString[j][4] = itemList.get(i).getCurrentTemp(); //fifth cell holds current temp
				rowString[j][5] = itemList.get(i).getRealFeel();//sixth cell holds real feel
				rowString[j][6] = itemList.get(i).getCond();//seventh cell holds conditions
				j++;
				
		} //End of for loop	
	} //End of creatRows Method
	
}