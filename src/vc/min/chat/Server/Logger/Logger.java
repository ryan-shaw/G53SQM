package vc.min.chat.Server.Logger;

/**
 * Logger
 * @author Ryan Shaw
 *
 */
public class Logger {
	
	/**
	 * Log a message at the given logging level
	 * @param type
	 * 			Type of message to log
	 * @param message
	 * 			Message to log
	 */
	public static void log(LogLevel type, String message){
		switch(type){
			case INFO:
				write("[INFO] " + message, false);
			break;
			case DEBUG:
				write("[DEBUG] " + message, false);
			break;
			case ERROR:
				write("[ERROR] " + message, true);
			break;
		}
	}
	
	/**
	 * Convenience method
	 * 	If error is true is writes to stderr else writes to stdout
	 * 
	 * @param message
	 * 				message to write and if it's error or not
	 */
	private static void write(String message, boolean error){
		if(!error){
			System.out.println(message);
		}else{
			System.err.println(message);
		}
	}
}
