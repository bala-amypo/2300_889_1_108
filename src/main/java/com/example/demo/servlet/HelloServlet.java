package com.example.demo.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(urlPatterns = "/hello-servlet")
public class HelloServlet extends HttpServlet {

 @Override
 protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
  resp.getWriter().write("Hello from Dynamic Event Ticket Pricing Servlet");
 }
}
