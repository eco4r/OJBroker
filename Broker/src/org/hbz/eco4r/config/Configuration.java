package org.hbz.eco4r.config;

/**
 * <b>Package Name: org.hbz.eco4r.config</b>
 * <b>Package Description: </b>
 * <p>This package is about divers configuration issues. It facilitates e.g. the access to properties 
 * in configuration files.</p>
 *
 * -----------------------------------------------------------------------------
 * 
 * This file is part of the eco4r-Project funded by the German Research Foundation - DFG. 
 * It is created by Library Service Center North Rhine Westfalia (Cologne) and the University of Bielefeld.

 * <b>License and Copyright:</b> </br>
 * <p>The contents of this file are subject to the
 * D-FSL License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License
 * at <a href="http://www.dipp.nrw.de/dfsl/">http://www.dipp.nrw.de/dfsl/.</a></p>
 *
 * <p>Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.</p>
 *
 * <p>Portions created for the Fedora Repository System are Copyright &copy; 2002-2005
 * by The Rector and Visitors of the University of Virginia and Cornell
 * University. All rights reserved."</p>
 *
 * -----------------------------------------------------------------------------
 *
 * <b>Creator(s): @author , publikationssysteme@hbz-nrw.de</b>
 *
 * @version 1.0
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.log4j.Logger;



/**
 * <b>Class Name</b>: Configuration</br>
 * <b>Class Definition</b>:
 * <p>A class that acts as a container for properties that can be loaded from a 
 * property file or provided directly as parameters</p>
 *
 * @author publikationssysteme@hbz-nrw.de
 *
 */
public class Configuration {	

	private static Logger logger = Logger.getLogger(Configuration.class);

	private List<Property> properties;
	private String suffix;
	private ResourceBundle resourceBundle;
	private String path;
	private String lineSeparator;
	
	/**
	 * Initializes a Configuration from a properties file.
	 * The location of the property file is given by the variable path
	 * @param path The path to the properties file
	 */
	public Configuration(String path) {
		this.path = path;
		
		if (this.suffix == "" || this.suffix == null)
			this.setSuffix(".properties");
		
		if (this.lineSeparator == "" || this.lineSeparator == null)
			this.lineSeparator = ",";
		
		if (path.startsWith ("/"))
            path = path.substring (1);
            
        if (path.endsWith (suffix))
        	path = path.substring (0, path.length () - suffix.length ());

        path = path.replace ('/', '.');
        
        logger.info("Loading properties from: " + path);
        
        this.resourceBundle = ResourceBundle.getBundle(path, Locale.getDefault());
      
        this.properties = this.loadProperties(this.resourceBundle, lineSeparator);
	}
	
	public Configuration (List<Property> properties){
		this.properties = properties;
	}
	
	/**
	 * Initializes a Configuration from keyValuesMap.
	 * 
	 * @param keyValuesMap A map containing the keys and their values
	 */
	public Configuration (Map<String, List<String>> keyValuesMap){
		List<Property> properties = new ArrayList<Property>();
		for (Map.Entry<String, List<String>> entry : keyValuesMap.entrySet()){
			String key = entry.getKey();
			List<String> values = entry.getValue();
			properties.add(new Property(key, values));
		}
		this.properties = properties;
	}
	
	private List<Property> loadProperties(ResourceBundle resourceBundle, String lineSeparator) {
		List<Property> properties = new ArrayList<Property>();
		if (resourceBundle != null){
			Set<String> keySet = resourceBundle.keySet();
			if (keySet.size() != 0){
				for (String key : keySet){
					List<String> values = this.getValuesByLineSeparator(resourceBundle.getString(key), lineSeparator);
					properties.add(new Property(key, values));
				}
			}
		}
		else
			logger.error("Resource Bundle is null.", new NullPointerException());
		
		return properties;
	}

	/**
	 * Returns the appropriate values embedded in a string 'valuesString'
	 *  
	 * @param valuesString A string where the values are embedded
	 * @param lineSeparator The line separator. Default = ','
	 * 
	 * @return A list of values
	 */
	private List<String> getValuesByLineSeparator(String valuesString,
			String lineSeparator) {
		
		List<String> values = null;
		
		String[] valuesArr = valuesString.split(lineSeparator, 0);
		
		values = Arrays.asList(valuesArr);
		
		return values;
	}

	
	/**
	 * Java Beans getter and setter methods
	 */
	
	public List<Property> getProperties() {
		return properties;
	}
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	public void setResourceBundle(ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getLineSeperator() {
		return lineSeparator;
	}
	public void setLineSeperator(String lineSeperator) {
		this.lineSeparator = lineSeperator;
	}

	@Override
	public String toString() {
		String string = "";
		int i = 0;
		for (Property property : this.properties){
			string += "Property " + ++i + ":  " + property + "\n";
		}
		
		return string;
	}
}
