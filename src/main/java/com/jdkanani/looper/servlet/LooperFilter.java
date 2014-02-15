package com.jdkanani.looper.servlet;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jdkanani.looper.Request;
import com.jdkanani.looper.Response;
import com.jdkanani.looper.route.BaseRoute;
import com.jdkanani.looper.route.Route;
import com.jdkanani.looper.route.Router;
import com.jdkanani.looper.route.Chain;

public class LooperFilter implements Filter {
	private Router router = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		router = Router.getInstance();
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		if (router == null) {
            filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Filters
        Iterator<BaseRoute> filters = router.getFilterEntries(request, response);

        // Create chain for current request
        // cannot make it lambda as we are using chain itself in `next` method
        Chain chain = new Chain() {
			@Override
			public void next() {
				if (filters.hasNext()) {
	            	BaseRoute route = filters.next();
	    	    	Request req = new Request(route, request);
	    			Response res = new Response(route, response);
	    			try {
	    				((com.jdkanani.looper.route.Filter)route).getFilterHandler().handle(req, res, this);
	    			} catch(Exception e) {
	    				e.printStackTrace();
	    				compose(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error!");
	    			}
	            } else {
	            	handleRoute(request, response);
	            }
			}
        };
    	chain.next();
	}

	private void handleRoute(HttpServletRequest request, HttpServletResponse response) {
        BaseRoute route = router.getRouteEntries(request, response);
        if (route != null) {
	    	Request req = new Request(route, request);
			Response res = new Response(route, response);
			try {
				Object result = ((Route)route).getRouteHandler().handle(req, res);
				if (result != null && result instanceof String) {
					compose(response, HttpServletResponse.SC_OK, result.toString());
				}
			} catch(Exception e) {
				e.printStackTrace();
				compose(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error!");
			}
        } else {
        	compose(response, HttpServletResponse.SC_NOT_FOUND, "Not found!");
        }
	}

	private void compose(HttpServletResponse response, int status, String message){
		if (response.getContentType() == null) {
			response.setContentType("text/html; charset=utf-8");
        }
		response.setStatus(status);
        try {
        	response.getOutputStream().write(message.getBytes("utf-8"));
		} catch (IOException e) {
			// Error on error
		}
	}
}