package com.jdkanani.looper;

import com.jdkanani.looper.route.FilterHandler;
import com.jdkanani.looper.route.HttpMethod;
import com.jdkanani.looper.route.RouteHandler;
import com.jdkanani.looper.route.Router;
import com.jdkanani.looper.webserver.LooperServer;

/**
 * Main Looper application.<br/>
 *
 * @author jdkanani
 *
 */
public final class Looper {
	private static String name;
	private static LooperServer server;
	private static Router router;


	private static String host = "127.0.0.1";
	private static int port = 5001;
	private static boolean running = false;

	static {
		router = Router.getInstance();
	}

	private Looper() {}

	/**
	 * Entry point for looper application
	 */
	public static void start() {
		if (running) return;
		server = new LooperServer();
		server.setHost(host);
		server.setPort(port);
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

	public static void setName(String name) { Looper.name = name; }

	public static String getName() {
		return Looper.name;
	}

	public static boolean isRunning() {
		return running;
	}

	public static String getHost() {
		return host;
	}

	public static void setHost(String host) {
		if (running) throwRunningException();
		Looper.host = host;
	}

	public int getPort() {
		return port;
	}

	public static void setPort(int port) {
		if (running) throwRunningException();
		Looper.port = port;
	}

	/**
	 * clears all routes
	 */
	public static void clearRoutes(){
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
	 * Adds filter in application scope
	 */
	public synchronized static void filter(String path, FilterHandler filterHandler) {
		if (running) throwRunningException();
		router.addFilter(path, filterHandler);
	}

	/**
	 * Map path to route handler for HTTP GET request
	 *
	 * @param path
	 * @param routeHandler
	 */
	public static void get(String path, RouteHandler routeHandler) {
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


    private static void throwRunningException() {
    	throw new IllegalStateException("Cannot perform this operation! Application is running.");
    }
}

