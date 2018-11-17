package main.misc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Configuration {
	
	public static int WORLD_HEIGHT;
	public static int WORLD_LENGTH;
	public static int NUMBER_OF_ANTS;
	
	public static String NEST;
	public static String FEED;
	
	public static String WALLS;
	public static int PATCH_SIZE;
	public static int DEBUG;
	
	public static void init() {
		
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			
			input = new FileInputStream("ants.properties");
			
		} catch (FileNotFoundException e) {
			
			OutputStream output = null;
			
			try {
				
				output = new FileOutputStream("ants.properties");
				
				prop.setProperty("WORLD_HEIGHT", "200");
				prop.setProperty("WORLD_LENGTH", "200");
				prop.setProperty("NUMBER_OF_ANTS", "10");
				
				prop.setProperty("NEST", "20,20");
				prop.setProperty("FEED", "80,80,82,82");
				
				prop.setProperty("WALLS", "");
				prop.setProperty("PATCH_SIZE", "8");
				prop.setProperty("DEBUG", "0");
				
				prop.store(output, null);
				
				input = new FileInputStream("ants.properties");
				
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				
				if (output != null) {
					try {
						output.close();
					} catch (IOException e2) {
						e.printStackTrace();
					}
				}
			}
			
		} finally {
			
			try {
				
				prop.load(input);
				
				Configuration.WORLD_HEIGHT = Integer.parseInt(prop.getProperty("WORLD_HEIGHT"));
				Configuration.WORLD_LENGTH = Integer.parseInt(prop.getProperty("WORLD_LENGTH"));
				Configuration.NUMBER_OF_ANTS = Integer.parseInt(prop.getProperty("NUMBER_OF_ANTS"));
				
				Configuration.NEST = prop.getProperty("NEST");
				Configuration.FEED = prop.getProperty("FEED");
				
				Configuration.WALLS = prop.getProperty("WALLS");
				Configuration.PATCH_SIZE = Integer.parseInt(prop.getProperty("PATCH_SIZE"));
				Configuration.DEBUG = Integer.parseInt(prop.getProperty("DEBUG"));
				System.out.println(Configuration.DEBUG);
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				
				if (input != null) {
					try {
						input.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				}
			}
		}
	}

}
