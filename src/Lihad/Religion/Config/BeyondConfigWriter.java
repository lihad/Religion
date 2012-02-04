package Lihad.Religion.Config;

import Lihad.Religion.Religion;


public class BeyondConfigWriter {
	public static Religion plugin;
    public BeyondConfigWriter(Religion instance) {
        plugin = instance;
    }
    public static void writeConfigurationBoolean(String string, boolean arg){
    	Religion.configuration.setProperty(string, arg);
    }
    public static void writeConfigurationInt(String string, int arg){
    	Religion.configuration.setProperty(string, arg);
    }
    public static void writeConfigurationDouble(String string, double arg){
    	Religion.configuration.setProperty(string, arg);
    }
    public static void writeConfigurationString(String string, String arg){
    	Religion.configuration.setProperty(string, arg);
    }
    public static void writeConfigurationLong(String string, long arg){
    	Religion.configuration.setProperty(string, arg);
    }
    public static void writeConfigurationNull(String string){
    	Religion.configuration.removeProperty(string);
    }
}
