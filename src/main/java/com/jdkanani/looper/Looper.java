package com.jdkanani.looper;

import com.jdkanani.looper.route.Filter;
import com.jdkanani.looper.route.HttpMethod;
import com.jdkanani.looper.route.Route;
import com.jdkanani.looper.route.Router;
import com.jdkanani.looper.view.template.RythmEngine;
import com.jdkanani.looper.view.template.TemplateEngine;
import com.jdkanani.looper.webserver.LooperServer;
import org.eclipse.jetty.util.resource.Resource;

import java.io.File;

/**
 * Main Looper application.<br/>
 *
 * @author jdkanani
 */
public final class Looper {
    private static String name;
    private static LooperServer server;
    private static TemplateEngine templateEngine;
    private static Router router;

    private static boolean running = false;

    static {
        router = Router.getInstance();
        server = new LooperServer();

        // Set default template root to "templates" directory
        try {
            templateRoot(Resource.newClassPathResource("templates").getFile());
        } catch (Exception e) {
        }
    }

    private Looper() {
    }

    /**
     * Entry point for looper application
     */
    public static void start() {
        if (running) return;
        running = true;
        new Thread(server::start).start();
    }

    /**
     * Stop looper application
     */
    public static void stop() {
        if (server != null) {
            server.stop();
        }
        running = false;
    }

    public static void setName(String name) {
        Looper.name = name;
    }

    public static String getName() {
        return Looper.name;
    }

    public static boolean isRunning() {
        return running;
    }

    public static String host() {
        return server.getHost();
    }

    public static String host(String host) {
        if (running) throwRunningException();
        server.setHost(host);
        return host;
    }

    public int port() {
        return server.getPort();
    }

    public static int port(int port) {
        if (running) throwRunningException();
        server.setPort(port);
        return port;
    }

    public static void staticRoot(File staticRoot) {
        server.setStaticRoot(staticRoot);
    }

    public static void templateRoot(File templateRoot) {
        setTemplateEngine(new RythmEngine(templateRoot));
    }

    public static TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public static void setTemplateEngine(TemplateEngine templateEngine) {
        Looper.templateEngine = templateEngine;
    }

    /**
     * clears all routes
     */
    public static void clearRoutes() {
        router.clear();
    }

    /**
     * Match path to route handler for given HTTP method
     */
    public synchronized static void route(String method, String path, Route route) {
        router.addRouter(method, path, route);
    }

    /**
     * Match path to route handler for given HTTP method
     */
    public synchronized static void route(HttpMethod method, String path, Route route) {
        router.addRouter(method, path, route);
    }

    /**
     * Adds filter in application scope for given method and path
     *
     * @param filters filter handlers
     */
    public synchronized static void filter(String method, String path, Filter... filters) {
        router.addFilter(method, path, filters);
    }

    /**
     * Adds filter in application scope for given method and path
     *
     * @param filters filter handlers
     */
    public synchronized static void filter(HttpMethod method, String path, Filter... filters) {
        router.addFilter(method, path, filters);
    }

    /**
     * Adds filter in application scope with given path
     *
     * @param path    route path
     * @param filters filter handlers
     */
    public synchronized static void filter(String path, Filter... filters) {
        for (HttpMethod method : HttpMethod.values()) {
            router.addFilter(method, path, filters);
        }
    }

    /**
     * Adds filter in application scope for all routes
     *
     * @param filters filter handlers
     */
    public synchronized static void filter(Filter... filters) {
        filter("/*", filters);
    }

    /**
     * Map path to route handler for HTTP GET request
     *
     * @param path
     * @param route route handler for path
     */
    public static void get(String path, Route route) {
        route(HttpMethod.get, path, route);
    }

    /**
     * Map path to route handler for HTTP POST request
     *
     * @param path
     * @param route route handler for path
     */
    public static void post(String path, Route route) {
        route(HttpMethod.post, path, route);
    }

    /**
     * Map path to route handler for HTTP DELETE request
     *
     * @param path
     * @param route
     */
    public static void delete(String path, Route route) {
        route(HttpMethod.delete, path, route);
    }

    /**
     * Map path to route handler for HTTP PUT request
     *
     * @param path
     * @param route
     */
    public static void put(String path, Route route) {
        route(HttpMethod.put, path, route);
    }

    /**
     * Map path to route handler for HTTP HEAD request
     *
     * @param path
     * @param route
     */
    public static void head(String path, Route route) {
        route(HttpMethod.head, path, route);
    }

    /**
     * Map path to route handler for HTTP PATCH request
     *
     * @param path
     * @param route
     */
    public static void patch(String path, Route route) {
        route(HttpMethod.patch, path, route);
    }

    /**
     * Map path to route handler for HTTP TRACE request
     *
     * @param path
     * @param route
     */
    public static void trace(String path, Route route) {
        route(HttpMethod.trace, path, route);
    }

    /**
     * Map path to route handler for HTTP CONNECT request
     *
     * @param path
     * @param route
     */
    public static void connect(String path, Route route) {
        route(HttpMethod.connect, path, route);
    }

    /**
     * Map path to route handler for HTTP OPTIONS request
     *
     * @param path
     * @param route
     */
    public static void options(String path, Route route) {
        route(HttpMethod.options, path, route);
    }

    /**
     * Throws illegal state exception
     */
    private static void throwRunningException() {
        throw new IllegalStateException("Cannot perform this operation! Application is running.");
    }
}