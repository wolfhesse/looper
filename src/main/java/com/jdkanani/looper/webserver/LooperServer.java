package com.jdkanani.looper.webserver;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.jdkanani.looper.servlet.LooperFilter;
import org.eclipse.jetty.util.resource.Resource;

import java.io.File;
import java.net.URL;

public class LooperServer {
	private final static String ALL_ROUTE = "/*";

	private String host = "127.0.0.1";
	private int port = 5001;
    private File staticRoot;

	private Server server;

	public LooperServer() {
        File tr = null;

        try {
            tr = Resource.newClassPathResource("static").getFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (tr != null) {
            staticRoot = tr;
        }
    }

	public void start() {
        ServerConnector connector = null;
        connector = new ServerConnector(new Server());
        connector.setHost(host);
        connector.setPort(port);

        server = connector.getServer();
        server.setConnectors(new Connector[]{ connector });

        // Handler list
        HandlerList handlers = new HandlerList();

        // Set static file location
        ResourceHandler staticHandler = getStaticHandler();

        if (staticHandler != null) {
            handlers.addHandler(staticHandler);
        }

        // Looper filter
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addFilter(LooperFilter.class, ALL_ROUTE, null);
        handlers.addHandler(context);


        // Add all handlers to jetty server
        server.setHandler(handlers);

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setStaticRoot(File staticRoot) {
        this.staticRoot = staticRoot;
    }

    public File getStaticRoot() {
        return staticRoot;
    }

    /**
     * Gets static file location
     */
    private ResourceHandler getStaticHandler() {
        if (staticRoot != null) {
            ResourceHandler resourceHandler = new ResourceHandler();
            if (staticRoot != null) {
                resourceHandler.setBaseResource(Resource.newResource(staticRoot));
                resourceHandler.setWelcomeFiles(new String[] { "index.html" });
                return resourceHandler;
            }
        }
        return null;
    }
}
