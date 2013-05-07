package model;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

public class ExecutionContext {

	private static ExecutionContext instance;
	private final EPServiceProvider serviceProvider;
	private final EPAdministrator administator;

	private ExecutionContext() {
		serviceProvider = EPServiceProviderManager.getDefaultProvider();
		administator = serviceProvider.getEPAdministrator();
	}

	public static ExecutionContext getInstance() {
		if (instance == null) {
			synchronized (ExecutionContext.class) {
				if (instance == null) {
					instance = new ExecutionContext();
				}
			}
		}
		return instance;
	}

	public EPServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	public EPAdministrator getAdministator() {
		return administator;
	}
}
