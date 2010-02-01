package org.dyndns.schuschu.xmms2client;

import se.fnord.xmms2.client.Client;
import se.fnord.xmms2.client.ClientFactory;

/**
 * @author schuschu
 *
 */
public class Loader {
	
	final static int ERROR_INVALID_PORT = -1;
	final static int ERROR_INVALID_DEBUGLEVEL = -2;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//TODO: Change to more parameters for ip, protocol etc 
		int port=0;
		for(String st : args) {
			if(st.startsWith("-p=") && port!=-1){
				port = Integer.parseInt(st.substring(3));
				if(port<=0 || port>65535) {
					System.out.println("Invalid Port!");
					System.exit(ERROR_INVALID_PORT);
				}
			}
		}
		
		Client client = ClientFactory
				.create("MyClient", "tcp://127.0.0.1:"+port);

		client.start();

		FooPluginWindowDefault main = new FooPluginWindowDefault(client);
		main.setVisible(true);

	}
}
