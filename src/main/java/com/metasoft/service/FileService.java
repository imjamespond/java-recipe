package com.metasoft.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileDeleteStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileService {

	@Value("${upload.baseDir}")
	private String uploadBaseDir;
	
	@Value("${drill.jdbcURL}")
	private String drillJdbcURl;
	
	public String getBaseDir() {
		return uploadBaseDir;
	}
	
	public String getDrillJdbcURl() {
		return drillJdbcURl;
	}
	
	public List<String> listSubDirs(String filePath) {
		File dir = new File(filePath);
		if (!dir.exists())
			dir.mkdir();
		List<String> subDirs = new ArrayList<>();
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) 
				subDirs.add(file.getName());
		}
		
		return subDirs;
	}
	
	public String readFile(String filePath) throws IOException {
		StringBuilder sb = new StringBuilder();
		Files.lines(Paths.get(filePath)).forEach(l -> sb.append(l + System.getProperty("line.separator")));
		return sb.toString();
	}
	
	public void writeFile(String filePath, String content) throws IOException {
		Files.write(Paths.get(filePath), content.getBytes(), StandardOpenOption.CREATE);
	}
	
	public List<String> listFiles(String filePath) {
		File dir = new File(filePath);
		List<String> files = new ArrayList<>();
		for (File file : dir.listFiles()) {
			if (file.isFile()) 
				files.add(file.getName());
		}
		
		return files;
	}
	
	public void removeFile(String filePath) {
		File file = new File(filePath);
		if (file.isFile() && file.exists()) 
			file.delete();
	}
	
	public void removeDir(String filePath) throws IOException {
		File file = new File(filePath);
		if (file.isDirectory()) 
			FileDeleteStrategy.FORCE.delete(file);
	}
	
	public void addSubDir(String path, String subDirName) {
		File subDir = new File(path + File.separator + subDirName);
		if (!subDir.exists())
			subDir.mkdir();
	}
	
}
