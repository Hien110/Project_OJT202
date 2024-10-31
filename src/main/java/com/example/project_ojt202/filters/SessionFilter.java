// package com.example.project_ojt202.filters;

// import jakarta.servlet.Filter;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.FilterConfig;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.ServletRequest;
// import jakarta.servlet.ServletResponse;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// import java.io.IOException;
// import java.util.regex.Pattern;

// public class SessionFilter implements Filter {

//     // Sử dụng regex để phát hiện đường dẫn tĩnh
//     private static final Pattern STATIC_RESOURCE_PATTERN = Pattern.compile(".*(\\.(css|js|png|jpg|jpeg|gif|woff|ttf|svg))$");

//     @Override
//     public void init(FilterConfig filterConfig) throws ServletException {
//         // Khởi tạo nếu cần
//     }

//     @Override
//     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//             throws IOException, ServletException {
//         HttpServletRequest httpRequest = (HttpServletRequest) request;
//         HttpServletResponse httpResponse = (HttpServletResponse) response;

//         String requestURI = httpRequest.getRequestURI();

//         // Kiểm tra nếu là tài nguyên tĩnh bằng cách kiểm tra đuôi file
//         if (STATIC_RESOURCE_PATTERN.matcher(requestURI).matches() || requestURI.equals("/login") || requestURI.equals("/")) {
//             chain.doFilter(request, response);
//             return;
//         }

//         // Kiểm tra session
//         if (httpRequest.getSession().getAttribute("account") == null) {
//             httpResponse.sendRedirect("/login");
//             return;
//         }

//         chain.doFilter(request, response);  // Cho phép yêu cầu tiếp tục nếu có session
//     }

//     @Override
//     public void destroy() {
//         // Dọn dẹp nếu cần
//     }
// }
