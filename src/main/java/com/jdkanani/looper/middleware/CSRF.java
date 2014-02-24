package com.jdkanani.looper.middleware;

import com.jdkanani.looper.Request;
import com.jdkanani.looper.Response;
import com.jdkanani.looper.route.Chain;
import com.jdkanani.looper.route.FilterHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author jdkanani
 */
public class CSRF implements FilterHandler {
    public static final String SESSION_TOKEN = "_csrfToken";
    public static final String HEADER_TOKEN = "x-csrf-token";
    public static final String REQUEST_TOKEN = "__csrftoken";

    @Override
    public void handle(Request request, Response response, Chain chain) {
        String method = request.getMethod();
        String secret = (String) request.getSession().getAttribute(SESSION_TOKEN);

        if (secret == null) {
            secret = UUID.randomUUID().toString();
            request.getSession().setAttribute(SESSION_TOKEN, secret);
        }

        if ("GET".equalsIgnoreCase(method) || "HEAD".equalsIgnoreCase(method) || "OPTIONS".equalsIgnoreCase(method)) {
            chain.next();
            return;
        } else if (!secret.equals(this.value(request))) {
            response.send(HttpServletResponse.SC_FORBIDDEN, "Token mismatched.");
            return;
        }

        chain.next();
    }

    private String value(Request request) {
        if (request.getHeader(HEADER_TOKEN) != null) {
            return request.getHeader(HEADER_TOKEN);
        } else if (request.getParameter(REQUEST_TOKEN) != null) {
            return request.getParameter(REQUEST_TOKEN);
        }
        return null;
    }

    public static FilterHandler csrfToken() {
        return new CSRF();
    }
}