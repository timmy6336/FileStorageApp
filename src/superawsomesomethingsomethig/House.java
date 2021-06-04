package superawsomesomethingsomethig;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

//@author Timmy Roma
/*
 * class that saves the state of the application
 * is used to transition between ui's and is what is 
 * saved to save everthing as a whole 
 */
public class House implements Serializable
{
	public static final String DEFAULT_FILENAME = "houseFile.hf";

	/**
	 * Default Serial UID for House
	 */
	private static final long serialVersionUID = 1L;
	private List<Room> roomList;
	private int level;
	private Room currentRoom;
	private Appliance currentAppliance;
	private Document currentDocument;
	private Room_UI roomUI;
	private Appliance_UI applianceUI;
	private Document_UI documentUI;
	private String filename;
	
	/**
	 * constructor
	 */
	public House() 
	{
		roomUI = null;
		applianceUI = null;
		documentUI = null;
		currentRoom = null;
		currentAppliance = null;
		currentDocument = null;
		roomList = new LinkedList<Room>();
		level = 0;
		filename = DEFAULT_FILENAME;
	}
	/**
	 * starts the room ui 
	 */
	public void start()
	{
		level = 0;
		roomUI = new Room_UI(roomList,this);
	}
	/**
	 * methos used to generate the appropriate ui for the object given
	 * based on which ui it is comeing from 
	 * @param newObject
	 */
	public void generateUI(Object newObject)
	{
		if(level == 0)
		{
			level = 1;
			currentRoom = (Room) newObject;
			applianceUI = new Appliance_UI(currentRoom,this);
			roomUI.setVisible(false);
		}
		else if(level == 1)
		{
			level = 2;
			currentAppliance = (Appliance) newObject;
			documentUI = new Document_UI(currentAppliance,this);
			applianceUI.setVisible(false);
		}
	}
	/**
	 * method to return to the previous ui
	 */
	public void back()
	{
		if(level == 2)
		{
			level = 1;
			applianceUI.setVisible(true);
			documentUI.setVisible(false);
		}
		else if(level == 1)
		{
			level = 0;
			roomUI.setVisible(true);
			applianceUI.setVisible(false);
		}
	}
	/**
	 * a getter for the list of rooms
	 * @return
	 */
	public List<Room> getList() 
	{
		return new LinkedList<Room>(roomList);  // List is copied to avoid editing errors
	}
	/**
	 * method to create a new room
	 * @param roomName  name of new room
	 * @return the created room
	 */
	public Room create(String roomName) 
	{
		Room room = new Room(roomName);
		roomList.add(room);
		getList();
		try {
			House.saveHouse(this, filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return room;
	}
	/**
	 * method to remnove a room from the list
	 * @param name the name of the room to be removed
	 */
	public void destroy(String name) 
	{
		int index = 0;
		boolean removed = false;
		for(Iterator<Room> listIterator = roomList.iterator(); listIterator.hasNext();)
		{
			Room temp = listIterator.next();
			if(temp.getName().equals(name))
			{
				removed = true;
				roomList.remove(index);
				break;
			}
			index++;
		}
		if(!removed)
		{
			Room_UI.errorMessage();
		}
		try {
			House.saveHouse(this, filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * getter for a specific room from the list 
	 * @param name the name of the desired room
	 * @return the desired room
	 */
	public Room getRoom(String name)
	{				
		for(Iterator<Room> listIterator = roomList.iterator(); listIterator.hasNext();)
		{
			Room temp = listIterator.next();
			if(temp.getName() .equals(name))
			{
				return temp;
			}
		}
		return null;
		
	}
	
	public String getFilename() {
		return filename;
	}
/**
 * method to save the house object 
 * @param house the house we are saving 
 * @param fileName the name of the save file 
 * @throws IOException incase the file dose not exist 
 */
	
	public static void saveHouse(House house, String fileName) throws IOException {
		FileOutputStream fout = new FileOutputStream(fileName);
		ObjectOutputStream oout = new ObjectOutputStream(fout);
		oout.writeObject(house);
		oout.close();
	}
	/**
	 * loads a previosly saved house 
	 * @param fileName name of file being loaded
	 * @return the house object created from loading 
	 * @throws IOException incase the file is not found 
	 * @throws ClassNotFoundException incase a house object dose not exist....
	 */
	public static House loadHouse(String fileName) throws IOException, ClassNotFoundException {
		FileInputStream in = new FileInputStream(fileName);
		ObjectInputStream oin = new ObjectInputStream(in);
		House house = (House) oin.readObject();
		oin.close();
		return house;
	}
}
