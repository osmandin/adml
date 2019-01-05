package edu.mit.adml.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.FilterChain;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Debubbing filter, used to display/set any session variables
 */

@Deprecated
//@Component
@Order(1)
@WebFilter(urlPatterns = "/")
public class DiagonsticHttpFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(DiagonsticHttpFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.debug("filter:" + ((HttpServletRequest) servletRequest).getRequestURL());

        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        final HttpSession session = httpServletRequest.getSession(false);

        if (session == null) {
            logger.info("Session null");
        }

        // Information from TouchStone:

        logger.info("Touchstone Attribute:{}", httpServletRequest.getAttribute("displayName"));
        logger.info("Touchstone Attribute:{}", httpServletRequest.getAttribute("mail"));
        logger.info("Touchstone Attribute:{}", httpServletRequest.getAttribute("nickname"));

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}