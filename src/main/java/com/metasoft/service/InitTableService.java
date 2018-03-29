package com.metasoft.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.metasoft.model.Constant;
import com.metasoft.service.dao.ApplicationObjectDaoService;

@Service
@Order(1)
public class InitTableService {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private DaoHelperFactory daoHelperFactory;
	
	@Autowired
	private ApplicationObjectDaoService applicationObjectDaoService;
	
	private JdbcTemplate jdbcTemplate;
	
	private static Logger logger = LoggerFactory.getLogger(InitTableService.class);
	
	private static final String function_begin_define = "--create function begin";
	
	private static final String function_end_define = "--create function end";
	
	private static final String function_split = "--function split";
	
	@PostConstruct
	public void init() {
		jdbcTemplate = new JdbcTemplate(dataSource);
		List<String> sqls = getSqlList();
		for (String sql : sqls ) {
			try {
				jdbcTemplate.execute(sql);
			} catch (Exception e) {
				logger.debug("execute sql error ["+e.getMessage()+"]");
			}
		}
		//create sequence
		applicationObjectDaoService.init();
	}

	private List<String> getSqlList() {
		BufferedReader bufferedReader = null;
		try {
			String scriptPath = getScriptePathByDataType();
			InputStreamReader inputStreamReader = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(scriptPath));
			bufferedReader = new BufferedReader(inputStreamReader);
			String line;
			StringBuilder builder = new StringBuilder("");
			while ((line  = bufferedReader.readLine()) != null) {
				line = filter(line);
				builder.append(line);
			}
			List<String> functions = parseFunction(builder);
			List<String> sqls = new ArrayList<String>();
			sqls.addAll(functions);
			String sql =  builder.toString();
			for (String s : sql.split(";")) {
				sqls.add(s);
			}
			return sqls;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private List<String> parseFunction(StringBuilder builder) {
		List<String> funs = new ArrayList<String>();
		String sql = builder.toString();
		if (sql.indexOf(function_begin_define) != -1) {
			String function =  sql.substring(sql.lastIndexOf(function_begin_define)+function_begin_define.length(), sql.indexOf(function_end_define));
			String[] fs = function.split(function_split);
			for (String f : fs) {
				funs.add(f);
			}
			return funs;
		}
		return funs;
	}

	private String getScriptePathByDataType() {
		if (daoHelperFactory.getDBType().isDB2()) {
			return Constant.INI_DB2_TABLE_FILE;
		} else if (daoHelperFactory.getDBType().isMySql()) {
			return Constant.INI_MYSQL_TABLE_FILE;
		}
		return null;
	}

	private String filter(String line) {
		if (isFunctionDefine(line)) {
			return line;
		}else if (!line.trim().startsWith("--")) {
			if (line.indexOf("--") != -1 && !isFunctionDefine(line)) {
				line = line.substring(0,line.indexOf("--"));
			}
			return line;
		}
		return "";
	}
	
	private boolean isFunctionDefine(String line) {
		return line.indexOf(function_begin_define) !=-1 ||
			line.indexOf(function_split) !=-1 || line.indexOf(function_end_define) != -1;
	}
	
	public static void main(String[] args) {
		String xx = "--begin create function wo shi zhong guo ren --end create function";
		if (xx.indexOf("--begin create function") != -1) {
			System.out.println(xx.substring(xx.indexOf("--begin create function")+("--begin create function").length(), xx.indexOf("--end create function")));
		}
	}
	
}
