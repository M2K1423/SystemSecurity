package com.example.webbongden.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebFilter("/*")
public class CharsetFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Thiết lập mã hóa UTF-8 cho request và response
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        chain.doFilter(request, response); // Tiếp tục chain để xử lý các request tiếp theo
    }
}