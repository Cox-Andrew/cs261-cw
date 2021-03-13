package com.moodlysis.moodbe;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;


@WebServlet("/v0/server-time")
public class ServerTimeRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ServerTimeRequest() {
        super();
    }


	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		JSONObject obj = new JSONObject();
		obj.put("server-time", LocalDateTime.now().toString());
		response.getWriter().print(obj.toJSONString());
	}



}
