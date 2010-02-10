package org.dyndns.schuschu.xmms2client.loader;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceWindow;
import org.dyndns.schuschu.xmms2client.view.tray.FooTray;
import org.dyndns.schuschu.xmms2client.view.window.FooWindow;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.ClientFactory;

/**
 * @author schuschu
 * 
 */
public class Loader {

	public static boolean DEBUG = false;
	
	/**
	 * parses command line arguments initializes main window
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// TODO: put parameter parsing into a separate function/class (FooInit
		// with static functions?)

		// set default host
		String host = new String("127.0.0.1");
		// set default port
		int port = 9667;
		// set default initial state of the window
		boolean hidden = false;

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
				hidden = true;
			}
			
			if (args[run].equals("--debug") || args[run].equals("-d")) {
				Loader.DEBUG = true;
			}
		}

		Client client = ClientFactory.create("fooXmms2", "tcp://" + host + ":"
				+ port);

		client.start();

		FooInterfaceWindow main = new FooWindow(client);

		FooTray tray = new FooTray(main, client);
		tray.initialize();

		if (!tray.isSupported() || !hidden) {
			main.setVisible(true);
		}

		main.loop();
		System.exit(0);
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
}
