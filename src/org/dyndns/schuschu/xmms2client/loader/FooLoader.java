package org.dyndns.schuschu.xmms2client.loader;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.dyndns.schuschu.xmms2client.factory.FooFactory;
import org.xml.sax.SAXException;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.ClientFactory;
import se.fnord.xmms2.client.ClientStatus;

/**
 * @author schuschu
 * 
 */
public class FooLoader {

	public static boolean DEBUG;
	public static boolean VISUAL;

	public static Client CLIENT;

	// set default initial state of the window
	private static boolean show_on_start;
	private static boolean max_on_start;

	private static String host;
	private static int port;

	/**
	 * parses command line arguments initializes main window
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		//Params, core
		parseXML();
		parseArgs(args);
		
		//GUI init (debug(
		FooSWT.createDebug();
		
		createClient(args);
		
		//Window elements
		FooFactory.loadPlugins();

		//Open window
		FooSWT.init(show_on_start, max_on_start);
	}

	/**
	 * checks whether an ip address is valid
	 * 
	 * @param ip
	 * @return boolean
	 */
	public final static boolean validIPAddress(String ip) {
		String[] octets = ip.split("\\.");

		if (octets.length != 4) {
			return false;
		}

		for (String octet : octets) {
			int i = Integer.parseInt(octet);

			if ((i < 0) || (i > 255)) {
				return false;
			}
		}

		return true;
	}

	public static void parseXML() {
		FooXML.init("data/config.xml");

		try {
			FooXML.parse();
		} catch (FileNotFoundException e) {
			// TODO: write default config file

			System.out.println("Error no config file");
			System.exit(1);
		} catch (IOException e) {
			// FATAAAAAL!

			e.printStackTrace();
			System.exit(1);
		} catch (SAXException e) {
			System.out.println("Error in config file");

			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void parseArgs(String[] args) {

		host = FooXML.exists("config/client", "ip") ? FooXML.getString(
				"config/client", "ip") : "127.0.0.1";
		port = FooXML.exists("config/client", "port") ? FooXML.getInt(
				"config/client", "port") : 1234;

		show_on_start = FooXML.exists("config/window", "visible") ? FooXML
				.getBool("config/window", "visible") : true;
		max_on_start = FooXML.exists("config/window", "maximised") ? FooXML
				.getBool("config/window", "maximised") : false;

		DEBUG = FooXML.exists("config/debug", "enabled") ? FooXML.getBool(
				"config/debug", "enabled") : false;
		VISUAL = FooXML.exists("config/debug", "visual") ? FooXML.getBool(
				"config/debug", "visual") : false;

		// parsing command line arguments
		// if argument is invalid, drop user shit and assume default
		for (int run = 0; run < args.length; run++) {
			String trial_host = null;

			// check whether host has been explity specified
			if (args[run].equals("--host") || args[run].equals("-h")) {
				trial_host = new String(args[++run]);

				if (validIPAddress(trial_host)) {
					host = trial_host;
				} else {
					exitWithError("illegal host!");
				}
			}

			// check whether port has been explity specified
			else if (args[run].equals("--port") || args[run].equals("-p")) {
				int trial_port = 0;

				try {
					trial_port = Integer.parseInt(args[++run]);
				} catch (NumberFormatException e) {
					exitWithError("illegal port!");
				} catch (ArrayIndexOutOfBoundsException e) {
				}

				// check port range
				if ((trial_port > 0) && (trial_port < 65535)) {
					port = trial_port;
				} else {
					exitWithError("illegal port!");
				}
			}

			// check if the window is supposed to go into tray directly
			else if (args[run].equals("--icon") || args[run].equals("-i")) {
				try {
					run++;
					if (args[run].equals("on") || args[run].equals("off")) {
						show_on_start = args[run].equals("on");
					} else {
						exitWithError("please specify either on or off");
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					exitWithError("please specify either on or off");
				}
			}

			// check if the window is supposed to start maximized
			else if (args[run].equals("--maximized") || args[run].equals("-m")) {
				try {
					run++;
					if (args[run].equals("on") || args[run].equals("off")) {
						max_on_start = args[run].equals("on");
					} else {
						exitWithError("please specify either on or off");
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					exitWithError("please specify either on or off");
				}
			}

			else if (args[run].equals("--debug") || args[run].equals("-d")) {
				try {
					run++;
					if (args[run].equals("on") || args[run].equals("off")) {
						FooLoader.DEBUG = args[run].equals("on");
					} else {
						exitWithError("please specify either on or off");
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					exitWithError("please specify either on or off");
				}
			}

			else if (args[run].equals("--visual") || args[run].equals("-v")) {
				try {
					run++;
					if (args[run].equals("on") || args[run].equals("off")) {
						FooLoader.VISUAL = args[run].equals("on");
					} else {
						exitWithError("please specify either on or off");
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					exitWithError("please specify either on or off");
				}
			} else {
				exitWithError("unkown argument " + args[run]);
			}
		}
	}

	private static void exitWithError(String message) {
		System.out.println(message);
		System.exit(1);
	}

	private static void createClient(String[] args) {

		CLIENT = ClientFactory.create("fooXmms2", "tcp://" + host + ":" + port);

		CLIENT.start();

		if (CLIENT.getStatus() == ClientStatus.DEAD) {
			System.out.println("can't connect to xmms2d");
			System.exit(1);
		}
	}
}
