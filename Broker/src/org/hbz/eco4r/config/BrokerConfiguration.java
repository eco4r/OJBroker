package org.hbz.eco4r.config;

import java.util.List;
import static org.hbz.eco4r.vocabulary.BrokerVocabulary.*;

public class BrokerConfiguration {

	private String dbURL;
	private String dbUser;
	private String dbPasswd;
	
	public BrokerConfiguration(String configFile) {
		Configuration configuration = new Configuration(configFile);
		this.initBrokerConfiguration(configuration);
	}

	private void initBrokerConfiguration(Configuration configuration) {
		List<Property> properties = configuration.getProperties();
		for (Property property : properties) {
			String key = property.getKey();
			List<String> values = property.getValues();
			
			if (key.compareTo(DATABASE_URL) == 0) {
				this.setDbURL(values.get(0).trim());
			}
			
			if (key.compareTo(DATABASE_USER) == 0) {
				this.setDbUser(values.get(0).trim());
			}
			
			if (key.compareTo(DATABASE_PASSWD) == 0) {
				this.setDbPasswd(values.get(0).trim());
			}
		}
	}

	public String getDbURL() {
		return dbURL;
	}

	public void setDbURL(String dbURL) {
		this.dbURL = dbURL;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPasswd() {
		return dbPasswd;
	}

	public void setDbPasswd(String dbPasswd) {
		this.dbPasswd = dbPasswd;
	}
}
