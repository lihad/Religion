package Lihad.Religion.Listeners;

import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

import Lihad.Religion.Religion;


public class BeyondPluginListener extends ServerListener {
	public static Religion plugin;
    public BeyondPluginListener(Religion instance) {
        plugin = instance;
    }
    public void onPluginEnable(PluginEnableEvent event){
    	if((event.getPlugin().getDescription().getName().equals("Permissions"))) plugin.setupPermissions();
    }
}
