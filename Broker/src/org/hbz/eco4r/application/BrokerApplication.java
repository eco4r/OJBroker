package org.hbz.eco4r.application;

import org.hbz.eco4r.resource.AggSummaryResource;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class BrokerApplication extends Application{

	/**
     * Creates a root Restlet that will receive all incoming calls.
     */
	@Override
    public synchronized Restlet createInboundRoot() {
    	Router router = new Router(getContext());
        
        router.attach("/query", AggSummaryResource.class);
        
        return router;
    }
}
