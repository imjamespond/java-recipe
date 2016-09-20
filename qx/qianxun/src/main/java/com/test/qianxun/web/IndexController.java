package com.test.qianxun.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.test.qianxun.service.GameService;
import com.test.qianxun.service.RankService;
import com.test.qianxun.service.UserService;

@Controller
@RequestMapping(value = "/")
public class IndexController {
	@Autowired
	private RankService rankService;
	@Autowired
	private GameService gameService;
	@Autowired
	private UserService userService;

	@Value("#{properties['upload.folder']}")
	private String uploadFolder;
	
	@RequestMapping("")
	public String get(HttpServletRequest request) {
/*	public String get(Model model,HttpServletRequest request) {
		for(int j = 0;j < 3; j++){
			for (int i = 0; i < 8; i++) {
				model.addAttribute("rankList" + j + i,
						rankService.listByVotes(j, i, limit));
			}
		}
*/
		
//		User user = new User();
//		user.setUsername("admin");
//		user.setPassword("123456");
//		User u = userService.findByUsername(user.getUsername());
//		if(u == null){
//			String sid = request.getSession().getId();
//			userService.register(user, sid);
//		}
		return "index";
	}
	
	@RequestMapping("/mobilegame")
	public String mobilegame() {
		return "mobilegame";
	}
	
	@RequestMapping("/agreement")
	public String agreement() {
		return "user/agree";
	}
	
	@RequestMapping(value = "/download/{name}", method = RequestMethod.GET)
	public void download(@PathVariable String name, HttpServletResponse response) {
		try {
			name = name + ".apk";
			File file = new File(uploadFolder + "/apk", name);
			response.addHeader("Content-Type",
					"application/vnd.android.package-archive");
			response.setHeader("Content-Length", String.valueOf(file.length()));
			response.setHeader("Connection", "keep-alive");
			response.setHeader("Accept-Ranges", "bytes");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ file.getName());

			byte[] buf = new byte[2048];
			OutputStream output = response.getOutputStream();
			FileInputStream fis = new FileInputStream(file);
			int length = 0;
			while ((length = fis.read(buf)) != -1) {
				output.write(buf, 0, length);
			}
			fis.close();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
