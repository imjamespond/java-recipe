package com.pengpeng.stargame.servermanager.servlets;

import com.pengpeng.stargame.servermanager.ServerInstanceManager;
import com.pengpeng.stargame.util.SpringContextUtil;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ChenHonghong@gmail.com
 * @since 13-4-7 下午3:06
 */
public class ServletStatusServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        VelocityEngine velocityEngine = (VelocityEngine) SpringContextUtil.getBean("velocityEngine");
        Map<String,Object> variables = new HashMap<String,Object>();
        variables.put("nodes", ServerInstanceManager.getServerInstances());

        String html = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "velocity/server-status.vm", variables);
        resp.getWriter().println(html);
    }
}
