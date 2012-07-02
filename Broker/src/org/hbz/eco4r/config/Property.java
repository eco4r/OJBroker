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
 * <b>Creator(s): @author publikationssysteme@hbz-nrw.de</b>
 *
 * @version 1.0
 */

import java.util.List;

/**
 * <b>Class Name</b>: Property</br>
 * <b>Class Definition</b>:
 * <p>Each instance of this class represent a single property Value. Each of which has a property Key
 * and a list of property values</p>
 *
 * @author publikationssysteme@hbz-nrw.de
 *
 */

public class Property {
	
	private String key;
	private List<String> values;
	
	public Property(String key, List<String> values) {
		this.key = key;
		this.values = values;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}
	
	@Override
	public String toString() {
		String propertyString = "";
		propertyString += "PROPERTY KEY: " + this.getKey() + "; ";
		propertyString += "PROPERTY VALUE(S): " + this.getValues();
		
		return propertyString;
	}
}
