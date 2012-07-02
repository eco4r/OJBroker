package org.hbz.eco4r.resource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.hbz.eco4r.config.BrokerConfiguration;
import org.hbz.eco4r.mysql.MySQLConnection;
import org.hbz.eco4r.util.AggregationSummary;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import static org.hbz.eco4r.vocabulary.BrokerVocabulary.*;

public class AggSummaryResource extends ServerResource{
	
	private Connection mysqlConn;
	private Map<String, List<List<String>>> summary;
	private String dbURL;
	private String dbUser;
	private String dbPasswd;
	
	
	@Override
	protected void doInit() throws ResourceException {
		BrokerConfiguration config = new BrokerConfiguration("broker");
		this.setDbURL(config.getDbURL());
		this.setDbUser(config.getDbUser());
		this.setDbPasswd(config.getDbPasswd());
		
		Request request = getRequest();
		Form form = request.getResourceRef().getQueryAsForm();
		
		String queryDDC = form.getFirstValue(DDC_SUBJECT, true, "*");
		String queryFrom = form.getFirstValue(FROM, true, "*");
		String queryUntil = form.getFirstValue(UNTIL, true, "*");
		String queryReposStr = form.getFirstValue(REPOSITORIES, true, "*");
		String queryPubtypeStr = form.getFirstValue(PUBTYPE, true, "*");

		MySQLConnection conn = new MySQLConnection(this.getDbURL(), this.getDbUser(), this.getDbPasswd());
		this.mysqlConn = conn.getConnection();
		AggregationSummary aggSumm = new AggregationSummary(queryDDC, 
				queryFrom, queryUntil, queryReposStr, queryPubtypeStr, this.mysqlConn);
		this.summary = aggSumm.getSummary();
	}

	@Get()
	public Representation getAggSummary() {
		Representation jsonrepresentation = null;
		
		JSONObject mainJsonObject = new JSONObject();
		
		try {
			for (Map.Entry<String, List<List<String>>> entry : this.summary.entrySet()) {
				String remURI = entry.getKey();
				List<List<String>> values = entry.getValue();
				
				String title = values.get(0).get(0);
				List<String> creators = values.get(1);
				String pubtype = values.get(2).get(0);
				String repository = values.get(3).get(0);
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(AGG_ID, this.getAggId(remURI));
				jsonObject.put(TYPE, "publication");
				jsonObject.put(REM_URI, remURI);
				jsonObject.put(TITLE, title);
				jsonObject.put(CREATORS, creators);
				jsonObject.put(PUBLICATION_TYPE, pubtype);
				jsonObject.put(REPOSITORY, repository);
				
				mainJsonObject.accumulate("items", jsonObject);
			}
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		jsonrepresentation = new JsonRepresentation(mainJsonObject);
		
		return jsonrepresentation;
	}

	private String getAggId(String remURI) {
		String aggId = "";
		
		try {
			String query = "SELECT DISTINCT " +
					"`agg_id` " +
					"from `rems` WHERE `rems`.`rem_uri` = '" + remURI + "'";
		
			Statement stmt = this.mysqlConn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				aggId = rs.getString("agg_id");
				if (aggId != null && !aggId.isEmpty()) 
					break;
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return aggId;
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
