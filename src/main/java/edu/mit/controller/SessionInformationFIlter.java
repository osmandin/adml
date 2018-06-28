package edu.mit.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.FilterChain;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Example filter, used to set session variables
 */

//@Component
@Order(1)
@WebFilter(urlPatterns = "/")
@Deprecated
public class SessionInformationFIlter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(SessionInformationFIlter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("filter:" + ((HttpServletRequest) servletRequest).getRequestURL());

        //logger.info("info:{}" , ((HttpServletRequest) servletRequest).getRequestURL());
        //logger.info("secure info:{}" , ((HttpServletRequest) servletRequest).isSecure());

        Enumeration<String> headerNames = ((HttpServletRequest) servletRequest).getHeaderNames();

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        HttpSession session = httpServletRequest.getSession(false);

        if (session == null) {
            logger.info("Session null");
            session = httpServletRequest.getSession();
        }


        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String s = headerNames.nextElement();
                String v = ((HttpServletRequest) servletRequest).getHeader(s);
                logger.info("Header: {} Value:{}", s, v);
                if (v.equals("http://iasc.mit.edu/adml/login")) {
                    //final String modified = s.replace("http://", "https://");
                    //((HttpServletResponse)servletResponse).setHeader(s, modified);
                    //logger.info("Header modified:{}", ((HttpServletResponse) servletResponse).getHeader(s));
                    session.setAttribute("safe", "false");
                    //logger.info("Setting up redirect");
                    //httpResponse.sendRedirect("http://www.mit.edu");
                }

                if (v.equals("https://iasc.mit.edu/adml/login")) {
                    session.setAttribute("safe", "true");
                }

                if (httpServletRequest.getRequestURL().toString().contains("https://")) { // To get around the referrer problem
                    session.setAttribute("safe", "true");

                } else {
                    logger.info("request url:{}", httpServletRequest.getRequestURL().toString());
                }
            }
        }

        Enumeration<String> sessionNames = session.getAttributeNames();

        while (sessionNames.hasMoreElements()) {
            try {
                String s = sessionNames.nextElement();
                String v = (String) session.getAttribute(s);
                logger.info("Session:{} {}", s, v);
            } catch (Exception e) {
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}