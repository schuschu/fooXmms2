package org.dyndns.schuschu.xmms2client.factories;

import java.util.HashMap;


public class FooFactory {
	
	
	// TODO: make factories static
	private static FooViewFactory viewFactory = null;
	private static FooBackendFactory backendFactory = null;
	private static FooWatchFactory watchFactory = null;
	private static FooActionFactory actionFactory = null;
	private static FooMenuFactory menuFactory = null;
	
	private static final HashMap<String, Object> views=new HashMap<String, Object>();
	private static final HashMap<String, Object> backends=new HashMap<String, Object>();
	private static final HashMap<String, Object> watches=new HashMap<String, Object>();


	public static FooViewFactory getViewFactory() {
		if (viewFactory == null) {
			viewFactory = new FooViewFactory();
		}
		return viewFactory;
	}

	public static FooBackendFactory getBackendFactory() {
		if (backendFactory == null) {
			backendFactory = new FooBackendFactory();
		}
		return backendFactory;
	}

	public static FooWatchFactory getWatchFactory() {
		if (watchFactory == null) {
			watchFactory = new FooWatchFactory();
		}
		return watchFactory;
	}

	public static FooActionFactory getActionFactory() {
		if (actionFactory == null) {
			actionFactory = new FooActionFactory();
		}
		return actionFactory;
	}

	public static FooMenuFactory getMenuFactory() {
		if (menuFactory == null) {
			menuFactory = new FooMenuFactory();
		}
		return menuFactory;
	}
	
	public static void putView(String name, Object object){
		views.put(name, object);
	}
	
	public static Object getView(String name){
		return views.get(name);
	}
	
	public static void putBackend(String name, Object object){
		backends.put(name, object);
	}
	
	public static Object getBackend(String name){
		return backends.get(name);
	}
	
	public static void putWatch(String name, Object object){
		watches.put(name, object);
	}
	
	public static Object getWatch(String name){
		return watches.get(name);
	}
	
}
