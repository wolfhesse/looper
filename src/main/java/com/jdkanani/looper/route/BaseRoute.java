package com.jdkanani.looper.route;

public abstract class BaseRoute {
    protected HttpMethod method;
    protected String path;
    protected UriMatcher pathMatcher;

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public UriMatcher getPathMatcher() {
        return pathMatcher;
    }
}
