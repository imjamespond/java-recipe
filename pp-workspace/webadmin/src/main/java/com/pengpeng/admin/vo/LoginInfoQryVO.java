package com.pengpeng.admin.vo;

/**
 * 用户登录信息
 *
 * @author kenny
 */

public class LoginInfoQryVO {
// ------------------------------ 属性 ------------------------------

    private String username;

    private String password;

    private String validateCode;

// --------------------- GETTER / SETTER 方法 ---------------------

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }
}
