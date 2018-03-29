package com.metasoft.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.codec.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.keymobile.metadataServices.interfaces.Column;
import com.keymobile.metadataServices.interfaces.MDService;
import com.keymobile.metadataServices.interfaces.MDServiceError;
import com.metasoft.service.FileService;
import com.metasoft.util.PrivilegeCheckingHelper;

@Controller
@RequestMapping("/management/files")
public class FileController {
	
	@Autowired
	private FileService fileService;
	@Autowired
	private MDService mdService;
	
	public static final String templateFolder = "template";
	private static final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@RequestMapping(value="/subDirs.load", method = RequestMethod.GET)
	public @ResponseBody List<String> listSubDirs(HttpServletRequest request) {
		HttpSession session = request.getSession();		
		return fileService.listSubDirs(PrivilegeCheckingHelper.getTenantBaseDir(session));
	}
	
	@RequestMapping(value="/files.load", method = RequestMethod.GET)
	public @ResponseBody List<String> listFilesBySubDir(HttpServletRequest request, @RequestParam String subDirName) {
		HttpSession session = request.getSession();		
		List<String> files = fileService.listFiles(PrivilegeCheckingHelper.getTenantBaseDir(session) + File.separator + subDirName);
		
		return files.stream().filter(file -> file.endsWith(".xlsx")).collect(Collectors.toList());
	}
	
	@RequestMapping(value = "/templates.load", method = RequestMethod.GET)
	public @ResponseBody List<String> listTemplatesBySubDir(HttpServletRequest request, @RequestParam String subDirName) {
		HttpSession session = request.getSession();		
		List<String> templates = fileService.listFiles(PrivilegeCheckingHelper.getTenantBaseDir(session) + File.separator + subDirName + File.separator + templateFolder);
		
		return templates.stream().filter(file -> file.endsWith(".xlsx")).collect(Collectors.toList());
	}
	
	@RequestMapping(value="/removeFile.load", method = RequestMethod.GET) 
	public @ResponseBody String deleteFile(HttpServletRequest request, @RequestParam String subDirName, 
			@RequestParam String fileName) {
		HttpSession session = request.getSession();
		fileService.removeFile(PrivilegeCheckingHelper.getTenantBaseDir(session) 
				+ File.separator + subDirName + File.separator + fileName);
		return "ok";
	}
	
	@RequestMapping(value="/removeSubDir.load", method = RequestMethod.GET)
	public @ResponseBody String deleteSubDir(HttpServletRequest request, @RequestParam String subDirName) throws IOException {
		HttpSession session = request.getSession();
		fileService.removeDir(PrivilegeCheckingHelper.getTenantBaseDir(session) 
				+ File.separator + subDirName);
		return "ok";
	}
	
	@RequestMapping(value="/addSubDir.post", method = RequestMethod.POST)
	public @ResponseBody String addSubDir(HttpServletRequest request, @RequestParam String subDirName) {
		HttpSession session = request.getSession();
		fileService.addSubDir(PrivilegeCheckingHelper.getTenantBaseDir(session), subDirName);
		fileService.addSubDir(PrivilegeCheckingHelper.getTenantBaseDir(session) + File.separator + subDirName, templateFolder);
		return "ok";
	}
		
	@RequestMapping(value="/descSubDir.load", method = RequestMethod.GET)
	public @ResponseBody List<Column> descFile(HttpServletRequest request, @RequestParam String subDirName) 
			throws IOException, SQLException, MDServiceError {
//		File file 
//			= new File(PrivilegeCheckingHelper.getTenantBaseDir(request.getSession()) + File.separator + subDirName + File.separator + subDirName + ".json");
//		if (file.exists()) {
//			String jsonContent = fileService.readFile(PrivilegeCheckingHelper.getTenantBaseDir(request.getSession()) + File.separator + subDirName + File.separator + subDirName + ".json");
//			Gson gson = new Gson();
//			List<Column> columns = gson.fromJson(jsonContent, new TypeToken<ArrayList<Column>>(){}.getType());
//			return columns;
//		} else {
			String sql = "select * from dfs.dataSharing.`" 
					+ PrivilegeCheckingHelper.getTenantName(request.getSession()) + "/" + subDirName + "/" + templateFolder + "/*.xlsx`";
			return mdService.getRuntimeMetadata(fileService.getDrillJdbcURl(), "whatever", "whatever", sql);
//		}
	}
	
	@RequestMapping(value="/saveFileMetadata.post", method = RequestMethod.POST)
	public @ResponseBody String addOrUpdateFileMetadata(HttpServletRequest request, @RequestParam String subDirName, 
			@RequestParam String fileMetadataJson) throws IOException {
		Gson gson = new Gson();
		List<Column> columns = gson.fromJson(fileMetadataJson, new TypeToken<ArrayList<Column>>(){}.getType());
		for (Column column : columns) {
			if (column.getSqlDataTypeName().equals("INTEGER")) {
				column.setSize(10);
				column.setSqlDataType(4);
			}
			if (column.getSqlDataTypeName().equals("DOUBLE")) {
				column.setSize(40);
				column.setSqlDataType(8);
			}
		}
		
		fileService.writeFile(PrivilegeCheckingHelper.getTenantBaseDir(request.getSession()) + File.separator + subDirName + File.separator + subDirName + ".json", gson.toJson(columns));	
		return "ok";
	}
	
	@RequestMapping("/genUploadToken")
	public @ResponseBody String genTempUploadURL(HttpServletRequest request, @RequestParam String subDirName) {
		HttpSession session = request.getSession();
		Map<String, String> tokenInfo = new HashMap<>();
		
		String path = PrivilegeCheckingHelper.getTenantBaseDir(session) + File.separator + subDirName;
		tokenInfo.put("path", path);
		
		Calendar c = Calendar.getInstance(); 
		c.add(Calendar.DAY_OF_YEAR, 1);
		Date expireDate = c.getTime();
		tokenInfo.put("expireDate", Long.toString(expireDate.getTime()));
		
		return Base64.encodeToString(new Gson().toJson(tokenInfo).getBytes());
	}
	
	@RequestMapping("/uploadFile.post/{subDirName}")
	public @ResponseBody String saveFile(HttpServletRequest request, @RequestParam("file") MultipartFile file, 
			@PathVariable String subDirName) throws Exception {
		HttpSession session = request.getSession();
		
		byte[] bytes = file.getBytes();
		BufferedOutputStream outputStream = new BufferedOutputStream(
				new FileOutputStream(new File(PrivilegeCheckingHelper.getTenantBaseDir(session) 
						+ File.separator + subDirName + File.separator + file.getOriginalFilename())));
		outputStream.write(bytes);
		outputStream.close();
		return "ok";
	}
	
	@RequestMapping("/uploadTemplate.post/{subDirName}")
	public @ResponseBody String saveTemplate(HttpServletRequest request, @RequestParam("file") MultipartFile file, 
			@PathVariable String subDirName) throws Exception {
		HttpSession session = request.getSession();
		
		List<String> oldTemplate = fileService.listFiles(PrivilegeCheckingHelper.getTenantBaseDir(session) 
						+ File.separator + subDirName + File.separator + templateFolder);
		oldTemplate.forEach(o -> fileService.removeFile(PrivilegeCheckingHelper.getTenantBaseDir(session) 
						+ File.separator + subDirName + File.separator + templateFolder + File.separator + oldTemplate.get(0)));
		
		byte[] bytes = file.getBytes();
		BufferedOutputStream outputStream = new BufferedOutputStream(
				new FileOutputStream(new File(PrivilegeCheckingHelper.getTenantBaseDir(session) 
						+ File.separator + subDirName + File.separator + templateFolder + File.separator + file.getOriginalFilename())));
		outputStream.write(bytes);
		outputStream.close();
		return "ok";
	}
	
	@RequestMapping("/uploadFileByPub.post/{token}")
	public @ResponseBody String saveByToken(HttpServletRequest request, @RequestParam("file") MultipartFile file, 
			@PathVariable String token) throws Exception {
		Map<String, String> tokenInfo = new Gson().fromJson(Base64.decodeToString(token), new TypeToken<Map<String, String>>(){}.getType());
		
		long expireDate = Long.parseLong(tokenInfo.get("expireDate"));
		if (expireDate < System.currentTimeMillis())
			return "expired";
		
		HttpSession session = request.getSession();
		String savedFileName = (PrivilegeCheckingHelper.getUserName(session) == null ? "unknown" : PrivilegeCheckingHelper.getUserName(session)) 
				+ "-" + (PrivilegeCheckingHelper.getTenantName(session) == null ? "unknown" : PrivilegeCheckingHelper.getTenantName(session)) + "(" + dateformat.format(new Date()) + ").xlsx";
				
		byte[] bytes = file.getBytes();
		BufferedOutputStream outputStream = new BufferedOutputStream(
				new FileOutputStream(new File(tokenInfo.get("path") + File.separator + savedFileName)));
		outputStream.write(bytes);
		outputStream.close();
		
		return "ok";
	}
	
	@RequestMapping(value = "/downloadTemplateByPub/{templateFileName:.+}", method = RequestMethod.GET)
	public @ResponseBody void getTemplateByToken(HttpServletRequest request, HttpServletResponse response, @RequestParam String token, @PathVariable String templateFileName) throws Exception {
		Map<String, String> tokenInfo = new Gson().fromJson(Base64.decodeToString(token), new TypeToken<Map<String, String>>(){}.getType());
		
		InputStream input = new FileInputStream(new File(tokenInfo.get("path") + File.separator + templateFolder + File.separator + templateFileName));
		IOUtils.copy(input, response.getOutputStream());
		response.flushBuffer();
	}
	
	@RequestMapping(value = "/getTemplateNameByPub", method = RequestMethod.GET)
	public @ResponseBody String getTemplateNameByToken(HttpServletRequest request, HttpServletResponse response, @RequestParam String token) throws Exception {
		Map<String, String> tokenInfo = new Gson().fromJson(Base64.decodeToString(token), new TypeToken<Map<String, String>>(){}.getType());
		
		List<String> templates = fileService.listFiles(tokenInfo.get("path") + File.separator + templateFolder);
		if (templates.size() == 0)
			return "";
		else 
			return templates.get(0);
	}
	
	@RequestMapping(value = "/downloadFile/{subDirName}/{fileName:.+}", method = RequestMethod.GET)
	public void downloadFile(HttpServletRequest request, HttpServletResponse response, @PathVariable String subDirName, @PathVariable String fileName) throws IOException {
		HttpSession session = request.getSession();
		InputStream input = new FileInputStream(new File(PrivilegeCheckingHelper.getTenantBaseDir(session) 
						+ File.separator + subDirName + File.separator + fileName));
		IOUtils.copy(input, response.getOutputStream());
		response.flushBuffer();
	}
		
	@RequestMapping(value = "/downloadTemplate/{subDirName}/{templateFileName:.+}", method = RequestMethod.GET)
	public void downloadTemplate(HttpServletRequest request, HttpServletResponse response, @PathVariable String subDirName, @PathVariable String templateFileName)
			throws IOException {
		HttpSession session = request.getSession();
		InputStream input = new FileInputStream(new File(PrivilegeCheckingHelper.getTenantBaseDir(session) 
						+ File.separator + subDirName + File.separator + templateFolder + File.separator + templateFileName));
		IOUtils.copy(input, response.getOutputStream());
		response.flushBuffer();
	}

}
