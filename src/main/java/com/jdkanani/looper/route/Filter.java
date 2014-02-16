package com.jdkanani.looper.route;

/**
 * Represents each filter entry
 *
 * @author jdkanani
 */
public class Filter implements BaseRoute {
	private String path;
	private UriMatcher pathMatcher;
	private FilterHandler filterHandler;

	public Filter(String path, FilterHandler filterHandler) {
		this.path = path;
		this.filterHandler = filterHandler;
		this.pathMatcher = new UriMatcher(path);
	}
	@Override
	public HttpMethod getMethod() {
		return null;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public UriMatcher getPathMatcher() {
		return pathMatcher;
	}

    public FilterHandler getFilterHandler() {
        return filterHandler;
    }
}
