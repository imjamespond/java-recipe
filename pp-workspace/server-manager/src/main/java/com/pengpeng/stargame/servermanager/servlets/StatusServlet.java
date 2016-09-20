package com.pengpeng.stargame.servermanager.servlets;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ChenHonghong@gmail.com
 * @since 13-5-17 下午4:20
 */
public class StatusServlet extends HttpServlet {

    private static Logger logger = Logger.getLogger(ManageServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        doGet(req, response);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {

        req.getRequestDispatcher("/status.jsp").forward(req,response);

    }
}
