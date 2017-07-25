package pt.tilt.sights.connector;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Valter Nepomuceno
 * @since 25th July 2017
 * @version 1.0
 */
public class MongoDbConnector {

	/** File path for config.properties */
	private static final String CONFIG_PATH = "./src/pt/tilt/sights/config.properties";

	/** Properties object loaded from config.properties file */
	private Properties properties = new Properties();

	/** Mongo DB hostname */
	private String hostname;

	/** Mongo DB port */
	private String port;

	/** Mongo DB username */
	private String username;

	/** Mongo DB password */
	private String password;

	/** Mongo DB database */
	private String database;

	/**
	 * Public constructor for Mongo DB connector.
	 */
	public MongoDbConnector() {
		loadProperties();
	}

	/**
	 * Loads Mongo DB configuration variables from 'config.properties' file.
	 */
	public void loadProperties() {
		try {
			InputStream input = new FileInputStream(CONFIG_PATH);
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}

		switch (properties.getProperty("environment")) {

			// SANDBOX Environment
			case "SBX":
				hostname = properties.getProperty("mongoDB_SBX_hostname");
				port = properties.getProperty("mongoDB_SBX_port");
				username = properties.getProperty("mongoDB_SBX_username");
				password = properties.getProperty("mongoDB_SBX_password");
				database = properties.getProperty("mongoDB_SBX_database");
				break;

			// PRODUCTION Environment
			case "PRD":
				hostname = properties.getProperty("mongoDB_PRD_hostname");
				port = properties.getProperty("mongoDB_PRD_port");
				username = properties.getProperty("mongoDB_PRD_username");
				password = properties.getProperty("mongoDB_PRD_password");
				database = properties.getProperty("mongoDB_PRD_database");
				break;

		}
	}

}
