package com.server.graphdataserver;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

public class GraphdataServerApplication extends Application {
	/**
     * Launches the application with an HTTP server.
     * 
     * @param args
     *            The arguments.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Component graphDataServer = new Component();
        graphDataServer.getServers().add(Protocol.HTTP, 8112);
        graphDataServer.getDefaultHost().attach(new GraphdataServerApplication());
        graphDataServer.start();
    }
    
    /**
     * Creates a root Router to dispatch call to server resources.
     */
    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/graph/hades/{source}/{destination}/{numMins}",
        		GraphdataServerResource.class);
        return router;
    }
}
