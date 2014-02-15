package com.jdkanani.looper.route;

/**
 * Represents each route entry
 *
 * @author jdkanani
 */
public class Route implements BaseRoute {
	private HttpMethod method;
	private String path;
	private RouteHandler routeHandler;
	private UriMatcher pathMatcher;

	public Route(HttpMethod method, String path, RouteHandler routeHandler) {
		this.method = method;
		this.path = path;
		this.routeHandler = routeHandler;

		this.pathMatcher = new UriMatcher(path);
	}

	@Override
	public HttpMethod getMethod() {
		return method;
	}

	@Override
	public String getPath() {
		return path;
	}

	public RouteHandler getRouteHandler() {
		return routeHandler;
	}

	@Override
	public UriMatcher getPathMatcher() {
		return pathMatcher;
	}
}
