package com.qianxun.web;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.qianxun.model.Manager;

@Component("userValidator")
public class UserValidator{
	private String emailRegex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
	private String passwdRegex = "^[A-Za-z0-9]+$";
	
	public String validateRegister(String use_name,String email,String password){
		String error = "";
		if(validateUsername(use_name) != ""){
			error = error + validateUsername(use_name)+",";
		}
		if(validateEmail(email) != ""){
			error = error + validateEmail(email)+",";
		}
		if(validatePassword(password) != ""){
			error = error + validatePassword(password)+",";
		}
		if(error!=""){
			return error.substring(0, error.lastIndexOf(","));
		}else{
			return error;
		}
	}
	
	
	
	public String validateLogin(String email,String password){
		String error = "";
		if(validateEmail(email) != ""){
			error = error + validateEmail(email)+",";
		}
		if(validatePassword(password) != ""){
			error = error + validatePassword(password)+",";
		}
		if(error!=""){
			return error.substring(0, error.lastIndexOf(","));
		}else{
			return error;
		}
	}
	
	public String validateChange(String email,String newPassword,String oldPassword){
		String error = "";
		if(validateEmail(email) != ""){
			error = error + validateEmail(email)+",";
		}
		if(validatePassword(newPassword) != ""){
			error = error + validatePassword(newPassword)+",";
		}
		if(validatePassword(oldPassword) != ""){
			error = error + validatePassword(oldPassword)+",";
		}
		if(error!=""){
			return error.substring(0, error.lastIndexOf(","));
		}else{
			return error;
		}
	}
	
	public String validateEmail(String email) {
		String error = "";
		if (email.isEmpty()) {
			error = "邮箱名不能为空";
		} else if (!Pattern.matches(emailRegex, email)) {
			error = "邮箱格式不正确";
		}
		return error;
	}
	
	public String validatePassword(String password) {
		String error = "";
		if (password.isEmpty()) {
			error = "密码不能为空";
		} else if (password.length() < 6) {
			error = "密码长度不能小于6位";
		} else if (password.length() > 16) {
			error = "密码长度不能超过16位";
		} else if (!Pattern.matches(passwdRegex, password)){
			error = "密码只能由数字和字母组成";
		}
		return error;
	}
	
	public String validateUsername(String use_name){
		String error = "";
		if(use_name.isEmpty()){
			error = "用户名不能为空";
		}else if (use_name.length() < 4) {
			error = "用户名长度不能小于4位";
		} else if (use_name.length() > 12) {
			error = "用户名长度不能超过12位";
		}
		return error;
	}
	
	public void validateManagerLogin(Manager manager, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "email", "manager.email.required");
		ValidationUtils.rejectIfEmpty(errors, "password","manager.password.required");
	}
}