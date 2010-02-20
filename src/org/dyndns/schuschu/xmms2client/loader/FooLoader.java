package org.dyndns.schuschu.xmms2client.loader;

import java.io.FileNotFoundException;
import java.io.IOException;

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

		parseXML();

		parseArgs(args);

		// SWT stuff
		FooSWT.createDebug();

		createClient(args);

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

		host = FooXML.getString("config/client", "ip");
		port = FooXML.getInt("config/client", "port");

		show_on_start = FooXML.getBool("config/window", "visible");
		max_on_start = FooXML.getBool("config/window", "maximised");

		DEBUG = FooXML.getBool("config/debug", "enabled");
		VISUAL = FooXML.getBool("config/debug", "visual");

		// parsing command line arguments
		// if argument is invalid, drop user shit and assume default
		for (int run = 0; run < args.length; run++) {
			String trial_host = null;

			// check whether host has been explity specified
			if (args[run].equals("--host") || args[run].equals("-h")) {
				trial_host = new String(args[run + 1]);

				if (validIPAddress(trial_host)) {
					host = trial_host;
				}
			}

			// check whether port has been explity specified
			if (args[run].equals("--port") || args[run].equals("-p")) {
				int trial_port = 0;

				try {
					trial_port = Integer.parseInt(args[run + 1]);
				} catch (NumberFormatException e) {
				} catch (ArrayIndexOutOfBoundsException e) {
				}

				// check port range
				if ((trial_port > 0) && (trial_port < 65535)) {
					port = trial_port;
				}
			}

			// check if the window is supposed to go into tray directly
			if (args[run].equals("--icon") || args[run].equals("-i")) {
				try {
					show_on_start = args[run + 1].equals("on");
				} catch (ArrayIndexOutOfBoundsException e) {

				}
			}

			// check if the window is supposed to start maximized
			if (args[run].equals("--maximized") || args[run].equals("-m")) {
				try {
					max_on_start = args[run + 1].equals("on");
				} catch (ArrayIndexOutOfBoundsException e) {

				}
			}

			if (args[run].equals("--debug") || args[run].equals("-d")) {
				FooLoader.DEBUG = true;
			}

			if (args[run].equals("--visual") || args[run].equals("-v")) {
				FooLoader.VISUAL = true;
			}
		}

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
