package Lihad.Religion.Information;

import Lihad.Religion.Religion;

public class BeyondInfoWriter {
	public static Religion plugin;
    public BeyondInfoWriter(Religion instance) {
        plugin = instance;
    }
    public static void writeConfigurationBoolean(String string, boolean arg){
    	Religion.information.set(string, arg);
    }
    public static void writeConfigurationInt(String string, int arg){
    	Religion.information.set(string, arg);
    }
    public static void writeConfigurationDouble(String string, double arg){
    	Religion.information.set(string, arg);
    }
    public static void writeConfigurationString(String string, String arg){
    	Religion.information.set(string, arg);
    }
    public static void writeConfigurationLong(String string, long arg){
    	Religion.information.set(string, arg);
    }
    public static void writeConfigurationNull(String string){
    	Religion.information.set(string, null);
    }
}
