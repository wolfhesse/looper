package com.jdkanani.looper;

import com.jdkanani.looper.middleware.CSRF;
import com.jdkanani.looper.route.BaseRoute;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Map;

public class Request {
    /**
     * Original HTTP servlet request
     */
    private HttpServletRequest request;
    /**
     * RouteEntry/FilterEntry entry object
     */
    private BaseRoute route;
    /**
     * Path parameters values</br>
     * e.g {@code /user/<id>} template gives {id:1234} for URI path {@code /user/1234}
     */
    private Map<String, String> params;

    public Request(BaseRoute route, HttpServletRequest request) {
        this.route = route;
        this.request = request;
    }

    public HttpServletRequest getHttpRequest() {
        return request;
    }

    public String params(String param) {
        if (params == null) {
            params = route.getPathMatcher().matches(request.getRequestURI());
        }
        if (params != null) {
            return params.get(param);
        }
        return null;
    }

    public HttpSession getSession() {
        return request.getSession();
    }

    public HttpSession getSession(boolean b) {
        request.getSession(false);
        return request.getSession(b);
    }

    public Cookie[] getCookies() {
        return request.getCookies();
    }

    public String getHeader(String key) {
        return request.getHeader(key);
    }

    public Enumeration<String> getHeaderNames() {
        return request.getHeaderNames();
    }

    public Enumeration<String> getHeaders(String key) {
        return request.getHeaders(key);
    }

    public String getMethod() {
        return request.getMethod();
    }

    public Object getAttribute(String key) {
        return request.getAttribute(key);
    }

    public Enumeration<String> getAttributeNames() {
        return request.getAttributeNames();
    }

    public String getParameter(String key) {
        return request.getParameter(key);
    }

    public Map<String, String[]> getParameterMap() {
        return request.getParameterMap();
    }

    public Enumeration<String> getParameterNames() {
        return request.getParameterNames();
    }

    // Get CSRF from session token
    public String csrfToken() {
        return (String) this.getSession().getAttribute(CSRF.SESSION_TOKEN);
    }
}
