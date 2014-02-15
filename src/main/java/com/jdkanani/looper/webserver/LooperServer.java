package com.jdkanani.looper.webserver;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.jdkanani.looper.servlet.LooperFilter;

public class LooperServer {
	private final static String ALL_ROUTE = "/*";

	private String host;
	private int port;

	private Server server;

	public LooperServer() {}

	public void start() {
        ServerConnector connector = null;
        connector = new ServerConnector(new Server());
        connector.setHost(host);
        connector.setPort(port);

        server = connector.getServer();
        server.setConnectors(new Connector[]{ connector });

        // Looper filter
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addFilter(LooperFilter.class, ALL_ROUTE, null);
        server.setHandler(context);

        try {
        	System.out.println("==>> Looper has started! ");
        	System.out.println("==>> Running on " + host + ":" + port);
	        server.start();
	        server.join();
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    }
	}

	public void stop() {
		try {
			server.stop();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
