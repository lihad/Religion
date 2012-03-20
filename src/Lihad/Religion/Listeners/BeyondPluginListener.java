package Lihad.Religion.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

import com.nijikokun.bukkit.Permissions.Permissions;

import de.diddiz.LogBlock.LogBlock;

import Lihad.Religion.Religion;


public class BeyondPluginListener implements Listener {
	public static Religion plugin;
    public BeyondPluginListener(Religion instance) {
        plugin = instance;
    }
    @EventHandler
    public void onPluginEnable(PluginEnableEvent event){
    	if(event.getPlugin().getDescription().getName().equals("Permissions"))plugin.setupPermissions();
    	else if(event.getPlugin().getDescription().getName().equals("PermissionsEx"))plugin.setupPermissionsEx();
    	else if(event.getPlugin().getDescription().getName().equals("LogBlock"))plugin.setupLogBlock();
    }
    @EventHandler
    public void onPluginDisable(PluginDisableEvent event){
    	if(event.getPlugin().getDescription().getName().equals("Permissions"))
    		plugin.setupPermissions();
    	else if(event.getPlugin().getDescription().getName().equals("LogBlock"))
    		plugin.setupLogBlock();
    }
}
