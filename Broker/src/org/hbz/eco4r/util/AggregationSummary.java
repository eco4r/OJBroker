package org.hbz.eco4r.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AggregationSummary {

	
	private String ddc;
	private String queryFrom;
	private String queryUntil;
	private String queryRepositoryStr;
	private String queryPubtypesStr;
	private Map<String, List<List<String>>> summary;
	private Connection conn;
	
	public AggregationSummary(String ddc, String from, 
			String until, String repositories, String pubtypes, Connection conn) {
		this.ddc = ddc;
		this.queryFrom = from;
		this.queryUntil = until;
		this.queryRepositoryStr = repositories;
		this.queryPubtypesStr = pubtypes;
		this.conn = conn;
		this.setSummary(this.initSummaries());
	}
	
	public Map<String, List<List<String>>> initSummaries() {
		
		String query = this.toSQLQuery(this.ddc, this.queryFrom, 
				this.queryUntil, this.queryRepositoryStr, this.queryPubtypesStr);
		
		Map<String, List<List<String>>> summary = new HashMap<String, List<List<String>>>();
		
		try {
			Statement stmt = this.conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				String rem_uri = rs.getString("rem_uri");
				String title = rs.getString("title");
				String creator = rs.getString("creator");
				String pubtype = rs.getString("pubtype");
				String repository = rs.getString("repository");
				
				if (summary.containsKey(rem_uri)) {
					List<List<String>> values = summary.get(rem_uri);
					List<String> titles = values.get(0);
					List<String> creators = values.get(1);
					List<String> pubtypes = values.get(2);
					List<String> repositories = values.get(3);
					
					if (!titles.contains(title)) {
						titles.add(title);
					}
					
					if (!creators.contains(creator)) {
						creators.add(creator);
					}
					
					if (!pubtypes.contains(pubtype)) {
						pubtypes.add(pubtype);
					}
					
					if (!repositories.contains(repository)) {
						repositories.add(repository);
					}
				}
				else {
					List<List<String>> values = new ArrayList<List<String>>();
					
					List<String> titles = new ArrayList<String>();
					List<String> creators = new ArrayList<String>();
					List<String> pubtypes = new ArrayList<String>();
					List<String> repositories = new ArrayList<String>();
					
					values.add(titles);
					values.add(creators);
					values.add(pubtypes);
					values.add(repositories);
					
					titles.add(title);
					creators.add(creator);
					pubtypes.add(pubtype);
					repositories.add(repository);
					
					summary.put(rem_uri, values);
				}
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return summary;
	}
	
	private String toSQLQuery(String ddc, String from, 
			String until, String repositories, String pubtypes) {
		
		String query = "SELECT DISTINCT * FROM " +
						"`agg_main` " +
							"JOIN `subject` " +
						    "JOIN `creator` " +
						    "JOIN `rems` " +
						    "JOIN `pub_type` " +
						    	"ON `agg_main`.`id` = `subject`.`agg_id` " +
						        "AND `agg_main`.`id` = `creator`.`agg_id` " +
						        "AND `agg_main`.`id` = `rems`.`agg_id` " + 
						        "AND `agg_main`.`id` = `pub_type`.`agg_id` " + 
						        "WHERE ";
		
		
		// subjects
				if (ddc.compareTo("*") == 0) {
					query += " `subject`.`subj` LIKE '%'";
				}
				else {
					String[] ddcArr = ddc.split(",");
					int i = 1;
					if (ddcArr.length > 1) {
						query += " ( ";
						for (String subj : ddcArr) {
							if (i < ddcArr.length) {
								System.out.println(i);
								query += " `subject`.`subj` = '" + subj + "' OR ";
							}
							else {
								query += " `subject`.`subj` = '" + subj + "'";
							}
							i++;
						}
						query += " )";
					}
					else {
//						query += " AND ";
						query += " `subject`.`subj` = '" + ddcArr[0] + "'";
					}
				}
		
		// from
		if (from.compareTo("*") == 0) {
			query += " AND `agg_main`.`accept_date` LIKE '%'";
		}
		else {
			query += " AND `agg_main`.`accept_date` >= '" + from + "'";
		}
		
		// until
		if (until.compareTo("*") == 0) {
			query += " AND `agg_main`.`accept_date` LIKE '%'";
		}
		else {
			query += " AND `agg_main`.`accept_date` <= '" + until + "'";
		}
		
		
		// repository
		if (repositories.compareTo("*") == 0) {
			query += " AND `agg_main`.`repository` LIKE '%'";
		}
		else {
			String[] reps = repositories.split(",");
			
			int i = 1;
			if (reps.length > 1) {
				query += " AND ( ";
				for (String rep : reps) {
					if (i < reps.length) {
						System.out.println(i);
						query += " `agg_main`.`repository` = '" + rep + "' OR ";
					}
					else {
						query += " `agg_main`.`repository` = '" + rep + "'";
					}
					i++;
				}
				query += " )";
			}
			else {
				query += " AND ";
				query += " `agg_main`.`repository` = '" + reps[0] + "'";
			}
		}
		
		
		// pub types
		if (pubtypes.compareTo("*") == 0) {
			query += " AND `pub_type`.`pubtype` LIKE '%'";
		}
		else {
			String[] pubs = pubtypes.split(",");
			int i = 1;
			if (pubs.length > 1) {
				query += " AND ( ";
				for (String pub : pubs) {
					if (i < pubs.length) {
						System.out.println(i);
						query += " `pub_type`.`pubtype` = '" + pub + "' OR ";
					}
					else {
						query += " `pub_type`.`pubtype` = '" + pub + "'";
					}
					i++;
				}
				query += " )";
			}
			else {
				query += " AND ";
				query += " `pub_type`.`pubtype` = '" + pubs[0] + "'";
			}
		}

		return query;
	}

	public String getQuerySubject() {
		return ddc;
	}

	public void setQuerySubject(String querySubject) {
		this.ddc = querySubject;
	}

	public String getQueryFrom() {
		return queryFrom;
	}

	public void setQueryFrom(String queryFrom) {
		this.queryFrom = queryFrom;
	}

	public String getQueryUntil() {
		return queryUntil;
	}

	public void setQueryUntil(String queryUntil) {
		this.queryUntil = queryUntil;
	}

	public String getQueryRepositoryStr() {
		return queryRepositoryStr;
	}

	public void setQueryRepositoryStr(String queryRepositoryStr) {
		this.queryRepositoryStr = queryRepositoryStr;
	}

	public String getQueryPubtypesStr() {
		return queryPubtypesStr;
	}

	public void setQueryPubtypesStr(String queryPubtypesStr) {
		this.queryPubtypesStr = queryPubtypesStr;
	}

	public String getDdc() {
		return ddc;
	}

	public void setDdc(String ddc) {
		this.ddc = ddc;
	}

	public Map<String, List<List<String>>> getSummary() {
		return summary;
	}

	public void setSummary(Map<String, List<List<String>>> summary) {
		this.summary = summary;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}
}
