package com.jdkanani.looper.route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Represents router object for Looper application.</br>
 * It contains routes list.
 *
 * @author jdkanani
 */
public class Router {
    private static Router router;

    private List<BaseRoute> routes;
    private Route notFoundEntry;
    private Route errorEntry;

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
     * @param type         method type for HTTP methods (get, post, ...)
     * @param path         on which route is mapped to
     * @param routeHandler mapped handler for given type and path
     */
    public synchronized void addRouter(String type, String path, RouteHandler routeHandler) {
        HttpMethod method = null;
        if (type != null) {
            method = HttpMethod.valueOf(type.toLowerCase());
        }
        Objects.requireNonNull(path, "path must not be null.");
        Objects.requireNonNull(routeHandler, "Invalid routeHandler for + " + path);
        routes.add(new Route(method, path, routeHandler));
    }

    /**
     * Validates and adds to router
     *
     * @param path          on which filter is mapped to
     * @param filterHandler mapped handler for given path
     */
    public synchronized void addFilter(String path, FilterHandler filterHandler) {
        Objects.requireNonNull(path, "path must not be null.");
        Objects.requireNonNull(filterHandler, "Invalid filterHandler for + " + path);
        routes.add(new Filter(path, filterHandler));
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
        for (BaseRoute route : routes) {
            if (route instanceof Route && methodMatches(route, request) && pathMatches(route, request)) {
                return route;
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
            if (routeEntry instanceof Route) return false;
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
        return routeMethod == null || request.getMethod().equalsIgnoreCase(routeMethod.name());
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
    public Route notFoundRoute() {
        return notFoundEntry;
    }

    /**
     * Returns 500 route entry
     */
    public Route errorRoute() {
        return errorEntry;
    }
}