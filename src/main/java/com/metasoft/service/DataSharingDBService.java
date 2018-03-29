package com.metasoft.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.keymobile.DBServices.interfaces.DBService;

@Service
public class DataSharingDBService {
	
	@Autowired
	private DBService dbService;
	
	@Value("${isUsePortalUser:false}")
	private String isUsePortalUser;
	
	
	@Value("${dataSharingDB.jdbcURL}")
	private String dataSharingDbJdbcUrl;
	
	@Value("${restService.token:abc}")
	private String token;
	
    public List<List<String>> executeQuery(String sql, String userName, String password, int rowsLimit) throws SQLException {
    	return dbService.executeQuery(sql, dataSharingDbJdbcUrl, userName, password, rowsLimit);
    }
    
    public Map<String, Object> executeQueryByPagination(String sql, String userName, String password, int startIndex, int rowsLimit) throws SQLException {
    	return dbService.executeQueryByPagination(sql, dataSharingDbJdbcUrl, userName, password, startIndex, rowsLimit);
    }
    
    public Map<String, Object> executeQueryByPagination(String schema, String table, String userName, String password, int startIndex, int rowsLimit) throws SQLException {
    	return dbService.executeQueryByPagination(schema, table, dataSharingDbJdbcUrl, userName, password, startIndex, rowsLimit);
    }
    
    public String getIsUsePortalUser() {
		return isUsePortalUser;
	}
    
    public String getToken() {
		return token;
	}
    
}
