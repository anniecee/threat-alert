package cmpt276.project.threatalert.security;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig fConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String loginURI = req.getContextPath() + "/login.html";
        String homeURI = req.getContextPath() + "/";

        boolean loggedIn = session != null && session.getAttribute("user") != null;
        boolean loginRequest = req.getRequestURI().equals(loginURI);
        boolean homeRequest = req.getRequestURI().equals(homeURI);

        if (loggedIn || loginRequest || homeRequest) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(loginURI);
        }
    }

    @Override
    public void destroy() {}
}