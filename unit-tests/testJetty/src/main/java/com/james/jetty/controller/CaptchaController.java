package com.james.jetty.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.cage.Cage;
import com.github.cage.GCage;

/**
 * 产生一个图片验证码到输出流
 */
@SuppressWarnings("restriction")
@Controller
@RequestMapping("/captcha")
public class CaptchaController extends HttpServlet {

	private static final long serialVersionUID = -3718663907013663827L;

	private static final Cage cage = new GCage();

	@RequestMapping("/generate")
	protected void generate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		ServletOutputStream sos = response.getOutputStream();
		Random random = new Random();
		int i = random.nextInt(1000);
		i = i < 100 ? 100 + i : i;
		String token = String.valueOf(i);
		try {

			cage.draw(token, sos);// cage.getTokenGenerator().next()
		} finally {

			// 添加以下响应头字段：要求客户端不缓存响应消息体内容
			// 设置响应内容类型"image/" + cage.getFormat()
			response.setContentType("image/jpeg");
			response.setHeader("Cache-Control", "no-cache, no-store");
			response.setHeader("Pragma", "no-cache");
			long time = System.currentTimeMillis();
			response.setDateHeader("Last-Modified", time);
			response.setDateHeader("Date", time);
			response.setDateHeader("Expires", time);

			HttpSession session = request.getSession();
			session.setAttribute("captchaToken", token);
			session.setAttribute("captchaTokenUsed", true);

			sos.flush();
			sos.close();
		}
	}

	public static String getToken(HttpSession session) {
		Object val = session.getAttribute("captchaToken");
		return val != null ? val.toString() : null;
	}

	@RequestMapping(value = "/", method = { RequestMethod.GET,
			RequestMethod.POST })
	protected String get(HttpServletRequest request,
			HttpServletResponse response) {

		return "common/captcha";
	}
}