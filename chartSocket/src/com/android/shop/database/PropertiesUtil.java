package com.android.shop.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesUtil {
	Logger log = Logger.getLogger(this.getClass().getName());
	private String fileName;
	private Properties pro;
	private FileInputStream in;
	private String path = getClass().getResource("/").getPath();

	// private FileOutputStream out;

	public PropertiesUtil(String filename) {
		this.fileName = filename;
		File file = new File(path + fileName);
		try {
			in = new FileInputStream(file);
			pro = new Properties();
			pro.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			log.info(e.getStackTrace() + ":" + e.getMessage());
		} catch (IOException e) {
			log.info(e.getStackTrace() + ":" + e.getMessage());
		}
	}

	public String getValues(String itemName) {
		return pro.getProperty(itemName);

	}
}
