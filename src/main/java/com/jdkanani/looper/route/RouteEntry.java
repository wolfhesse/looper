package com.jdkanani.looper.route;

/**
 * Represents each route entry
 *
 * @author jdkanani
 */
public class RouteEntry extends BaseRoute {
    private Route route;

    public RouteEntry(HttpMethod method, String path, Route route) {
        this.method = method;
        this.path = path;
        this.route = route;

        this.pathMatcher = new UriMatcher(path);
    }

    public Route getRoute() {
        return route;
    }
}
