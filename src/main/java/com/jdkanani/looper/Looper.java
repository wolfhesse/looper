package com.jdkanani.looper;

import com.jdkanani.looper.middleware.CSRF;
import com.jdkanani.looper.route.FilterHandler;
import com.jdkanani.looper.route.HttpMethod;
import com.jdkanani.looper.route.RouteHandler;
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
        } catch (Exception e) {}
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
    public synchronized static void route(String method, String path, RouteHandler routeHandler) {
        if (running) throwRunningException();
        router.addRouter(method, path, routeHandler);
    }

    /**
     * Adds filter in application scope for all routes
     *
     * @param filterHandlers filter handlers
     */
    public synchronized static void filter(FilterHandler... filterHandlers) {
        if (running) throwRunningException();
        for (FilterHandler filterHandler: filterHandlers) {
            router.addFilter("/*", filterHandler);
        }
    }

    /**
     * Adds filter in application scope
     *
     * @param path          route path
     * @param filterHandlers filter handlers
     */
    public synchronized static void filter(String path, FilterHandler... filterHandlers) {
        if (running) throwRunningException();
        for (FilterHandler filterHandler: filterHandlers) {
            router.addFilter(path, filterHandler);
        }
    }

    /**
     * Map path to route handler for HTTP GET request
     *
     * @param path
     * @param routeHandler
     */
    public static void get(String path, RouteHandler routeHandler, FilterHandler... filterHandlers) {
        filter(path, filterHandlers);
        route(HttpMethod.get.name(), path, routeHandler);
    }

    /**
     * Map path to route handler for HTTP POST request
     *
     * @param path
     * @param routeHandler
     */
    public static void post(String path, RouteHandler routeHandler) {
        route(HttpMethod.post.name(), path, routeHandler);
    }

    /**
     * Map path to route handler for HTTP DELETE request
     *
     * @param path
     * @param routeHandler
     */
    public static void delete(String path, RouteHandler routeHandler) {
        route(HttpMethod.delete.name(), path, routeHandler);
    }

    /**
     * Map path to route handler for HTTP PUT request
     *
     * @param path
     * @param routeHandler
     */
    public static void put(String path, RouteHandler routeHandler) {
        route(HttpMethod.put.name(), path, routeHandler);
    }

    /**
     * Map path to route handler for HTTP HEAD request
     *
     * @param path
     * @param routeHandler
     */
    public static void head(String path, RouteHandler routeHandler) {
        route(HttpMethod.head.name(), path, routeHandler);
    }

    /**
     * Map path to route handler for HTTP PATCH request
     *
     * @param path
     * @param routeHandler
     */
    public static void patch(String path, RouteHandler routeHandler) {
        route(HttpMethod.patch.name(), path, routeHandler);
    }

    /**
     * Map path to route handler for HTTP TRACE request
     *
     * @param path
     * @param routeHandler
     */
    public static void trace(String path, RouteHandler routeHandler) {
        route(HttpMethod.trace.name(), path, routeHandler);
    }

    /**
     * Map path to route handler for HTTP CONNECT request
     *
     * @param path
     * @param routeHandler
     */
    public static void connect(String path, RouteHandler routeHandler) {
        route(HttpMethod.connect.name(), path, routeHandler);
    }

    /**
     * Map path to route handler for HTTP OPTIONS request
     *
     * @param path
     * @param routeHandler
     */
    public static void options(String path, RouteHandler routeHandler) {
        route(HttpMethod.options.name(), path, routeHandler);
    }

    /**
     * Throws illegal state exception
     */
    private static void throwRunningException() {
        throw new IllegalStateException("Cannot perform this operation! Application is running.");
    }
}

