package com.jdkanani.looper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.jdkanani.looper.route.BaseRoute;

public class Response {
	private final String UTF8 = "utf-8";

	/**
	 * Route/Filter entry object
	 */
	private BaseRoute route;
	private HttpServletResponse response;

	public Response(BaseRoute route, HttpServletResponse response) {
		this.route = route;
		this.response = response;
	}

	public HttpServletResponse getHttpResponse() {
		return response;
	}

	public void addCookie(Cookie cookie){
		response.addCookie(cookie);
	}

	public void addHeader(String t, String v){
		response.addHeader(t, v);
	}

	public void setStatus(int status){
		response.setStatus(status);
	}

	public Object ok(String message) {
		return send(message);
	}

	public Object send(String message) {
		return send(HttpServletResponse.SC_OK, message);
	}

	public Object send(int status, String message) {
		return send(status, message, UTF8);
	}

	public Object send(int status, String message, String charsetName) {
		try {
			if (response.getContentType() == null) {
				response.setContentType("text/html; charset="+charsetName);
            }
			response.setStatus(status);
			response.getOutputStream().write(message.getBytes(charsetName));
		} catch (IOException e) {
			// Internal server error
		}
		return null;
	}


    /**
     * Render template and make response
     *
     * @param templateName template file name
     */
    public Object render(String templateName) {
        return Looper.getTemplateEngine().render(templateName, new HashMap<String, Object>(){});
    }

    /**
     * Render template and make response
     *
     * @param templateName template file name
     * @param params map of parameters
     */
    public Object render(String templateName, Map<String, Object> params) {
        return Looper.getTemplateEngine().render(templateName, params);
    }
}
