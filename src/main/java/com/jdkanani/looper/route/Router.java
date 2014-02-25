package com.jdkanani.looper.route;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Represents router object for Looper application.</br>
 * It contains routes list.
 *
 * @author jdkanani
 */
public class Router {
    private static Router router;

    private List<BaseRoute> routes;
    private RouteEntry notFoundEntry;
    private RouteEntry errorEntry;

    private Router() {
        routes = new ArrayList<BaseRoute>();
    }

    /**
     * Get instance of Router - Implements singleton pattern
     *
     * @return Router object
     */
    public static Router getInstance() {
        if (router == null) {
            router = new Router();
        }
        return router;
    }

    /**
     * Validates and adds to router
     *
     * @param type  method type for HTTP method (get, post, ...)
     * @param path  on which route is mapped to
     * @param route mapped handler for given type and path
     */
    public synchronized void addRouter(String type, String path, Route route) {
        Objects.requireNonNull(type, "Method type must not be null.");
        addRouter(HttpMethod.valueOf(type.toLowerCase()), path, route);
    }

    /**
     * Validates and adds to router
     *
     * @param method method for HTTP method
     * @param path   on which route is mapped to
     * @param route  mapped handler for given type and path
     */
    public synchronized void addRouter(HttpMethod method, String path, Route route) {
        Objects.requireNonNull(path, "path must not be null.");
        Objects.requireNonNull(route, "Invalid route for " + path);
        Objects.requireNonNull(method, "method must not be null for " + path);
        routes.add(new RouteEntry(method, path, route));
    }

    /**
     * Validates and adds to router
     *
     * @param type    method type for HTTP methods (get, post, ...)
     * @param path    on which filter is mapped to
     * @param filters mapped handlers for given path
     */
    public synchronized void addFilter(String type, String path, Filter... filters) {
        Objects.requireNonNull(type, "Method type must not be null.");
        addFilter(HttpMethod.valueOf(type.toLowerCase()), path, filters);
    }

    /**
     * Validates and adds to router
     *
     * @param method  method for HTTP methods
     * @param path    on which filter is mapped to
     * @param filters mapped handlers for given path
     */
    public synchronized void addFilter(HttpMethod method, String path, Filter... filters) {
        Objects.requireNonNull(path, "path must not be null.");
        Objects.requireNonNull(method, "method must not be null for " + path);
        for (Filter filter : filters) {
            routes.add(new FilterEntry(method, path, filter));
        }
    }

    /**
     * Returns all routes
     *
     * @return list of routes
     */
    public List<BaseRoute> getRoutes() {
        return routes;
    }

    /**
     * Clears all routes from this router
     */
    public void clear() {
        routes.clear();
    }

    /**
     * Get match route entry
     *
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     */
    // TODO may be it should return iterator of matched routes instead first one
    public BaseRoute getRouteEntries(HttpServletRequest request, HttpServletResponse response) {
        for (BaseRoute routeEntry : routes) {
            if (routeEntry instanceof RouteEntry && methodMatches(routeEntry, request) && pathMatches(routeEntry, request)) {
                return routeEntry;
            }
        }
        return null;
    }

    /**
     * Get matched filters
     *
     * @param request  HttpServletRequest object
     * @param response HttpServletResponse object
     */
    public Iterator<BaseRoute> getFilterEntries(HttpServletRequest request, HttpServletResponse response) {
        return routes.stream().filter((routeEntry) -> {
            if (routeEntry instanceof RouteEntry) return false;
            return methodMatches(routeEntry, request) && pathMatches(routeEntry, request);
        }).iterator();
    }

    /**
     * Helper method to match HTTP methods
     *
     * @param baseRoute BaseRoute object - can be filter or route
     * @param request   HttpServletRequest object
     * @return True if method matches or BaseRouteEntry's method in null
     */
    private boolean methodMatches(BaseRoute baseRoute, HttpServletRequest request) {
        HttpMethod routeMethod = baseRoute.getMethod();
        return request.getMethod().equalsIgnoreCase(routeMethod.name());
    }

    /**
     * Helper method to match path
     *
     * @param baseRoute BaseRoute object - can be filter or route
     * @param request   HttpServletRequest object
     * @return True if path matches
     */
    private boolean pathMatches(BaseRoute baseRoute, HttpServletRequest request) {
        return baseRoute.getPathMatcher().matches(request.getRequestURI()) != null;
    }

    /**
     * Returns 404 route entry
     */
    public RouteEntry notFoundRoute() {
        return notFoundEntry;
    }

    /**
     * Returns 500 route entry
     */
    public RouteEntry errorRoute() {
        return errorEntry;
    }
}