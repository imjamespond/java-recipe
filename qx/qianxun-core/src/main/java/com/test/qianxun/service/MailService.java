package com.test.qianxun.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.test.qianxun.service.MailService;

@Service
public class MailService {
	private static Logger logger = LoggerFactory.getLogger(MailService.class);
	@Autowired
	private JavaMailSender mailSender;
	@Value("#{properties['mail.from']}")
	private String from;

	public void send(String username, String to, String subject, String text) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true,
					"UTF-8");
			helper.setFrom(username + "<" + from + ">");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text, true);
			mailSender.send(message);
		} catch (MessagingException e) {
			logger.error("send mail failure", e);
		}
	}
}