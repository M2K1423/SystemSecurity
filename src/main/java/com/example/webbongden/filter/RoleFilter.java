package com.example.webbongden.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class RoleFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        HttpSession session = httpRequest.getSession(false);
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        // Kiểm tra quyền truy cập
        if (requestURI.startsWith("/admin")) {
            // Chỉ cho phép admin truy cập
            if (role == null || !"admin".equals(role)) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
                return;
            }
        } else if (requestURI.startsWith("/user")) {
            // Chỉ cho phép user hoặc admin truy cập
            if (role == null || (!"user".equals(role) && !"admin".equals(role))) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
                return;
            }
        }

        // Tiếp tục chuỗi filter nếu quyền hợp lệ
        chain.doFilter(request, response);
    }
}