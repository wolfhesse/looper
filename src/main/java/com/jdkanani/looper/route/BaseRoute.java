package com.jdkanani.looper.route;

public interface BaseRoute {
    public String getPath();

    public HttpMethod getMethod();

    public UriMatcher getPathMatcher();
}
