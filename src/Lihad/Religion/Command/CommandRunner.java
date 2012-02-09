package Lihad.Religion.Command;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Lihad.Religion.Religion;
import Lihad.Religion.Information.BeyondInfo;

public class CommandRunner implements CommandExecutor {
	public static Religion plugin;

	public CommandRunner(Religion instance){
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] arg) {
		/**
		 * COMMAND RUNNER
		 * 
		 * All parts on this section drive commands referenced by the beginning /rr
		 * 
		 * TODO: Make symbols non-valid in command.
		 * TODO: Add ChatColor to text output
		 * TODO: Expand and clean-up text output
		 * 
		 * COMMANDS TO ADD
		 * 
		 * TODO: How about a command to list the members of your own tower, both online and offline (online in a different color)
		 */
		
		//------------------------------------------------------------------------------------------------
		
		/**
		 * 
		 * Base Command, drives /rr.  Tells the player what religion and tower they are member of
		 */
		if(cmd.getName().equalsIgnoreCase("rr") && arg.length == 0){
			if(BeyondInfo.getTowerName((Player)sender) == null) sender.sendMessage("You are not a member of any religion");
			else sender.sendMessage("You are a member of "+BeyondInfo.getTowerName((Player)sender)+", a tower of "+BeyondInfo.getReligion((Player)sender));
			return true;
		}
		
		/**
		 * TODO: I believe '/rr join' with no param throws an exception.  This needs to be fixed
		 * Drives '/rr join <param>' command.  This allows a player to join the chosen tower. 
		 */
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("join") && arg.length == 2){
			if(!BeyondInfo.hasTower(arg[1])){
				sender.sendMessage("Invalid tower you fuck head");
				return true;
			}
			if(BeyondInfo.hasPlayer((Player)sender)){
				sender.sendMessage("You are already a member of a Tower. Please leave your current one to join this one.");
				return true;
			}
			BeyondInfo.addPlayer((Player)sender, arg[1]);
			sender.sendMessage("You have joined the Tower of "+arg[1]+" and the Religion of "+BeyondInfo.getReligion(arg[1]));
			return true;
		}
		
		/**
		 * Drives '/rr leave'.  Allows player to leave tower and religion
		 */
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("leave") && arg.length == 1){
			if(!BeyondInfo.hasPlayer((Player)sender)){
				sender.sendMessage("YOU ARENT PART OF A RELIGION YOU CUNT");
				return true;
			}
			BeyondInfo.removePlayer((Player)sender);
			sender.sendMessage("You have left your tower and religion");
			return true;
		}
		
		/**
		 * Drives '/rr list'.  Lists to player all religions that exist
		 */
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("list") && arg.length == 1){
			sender.sendMessage("The religions that exist are: "+BeyondInfo.getReligions().toString());
			return true;
		}
		
		/**
		 * Drives '/rr <religion> list' Lists all towers associated with param religion
		 */
		else if(cmd.getName().equalsIgnoreCase("rr") && BeyondInfo.getReligions().contains(arg[0]) && arg[1].equals("list") && arg.length == 2){
			sender.sendMessage("The towers that exist are: "+BeyondInfo.getTowers(arg[0]));
			return true;
		}
		
		/**
		 * TODO: I don't think this works... make it work.  General details could include members, tower name, location.
		 * Drives '/rr details <towername>'  Gets general details of a tower.
		 */
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("details") && arg.length == 2){
			if(BeyondInfo.hasTower(arg[1])){
				sender.sendMessage("General Info");
			}else{
				sender.sendMessage("This is not a valid tower. The towers that exist are: "+BeyondInfo.getTowers(arg[0]));
			}
			return true;
		}
		
		/**
		 * TODO: I don't think this works... make it work.  In-Depth details could include members, tower name, location, influence
		 *Drives '/rr details' Gets in-depth details about a player specific religion
		 */
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("details") && arg.length == 1){
			if(BeyondInfo.hasPlayer((Player)sender)){
				sender.sendMessage("In-depth Info");
			}else{
				sender.sendMessage("This command is invalid because you are not a member of a tower");
			}
			return true;
		}
		
		/**
		 * 
		 * Drives '/rr here'.  Outputs the territory you are standing in.
		 */
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("here") && arg.length == 1){
			List<String> towers = BeyondInfo.getTowersOfLocation(((Player)sender).getLocation());
			if(towers.size() == 0) sender.sendMessage("You are not in a defined territory");
			else sender.sendMessage("You are in the territory of "+towers);
			return true;
		}
		
		/**
		 * 
		 * Drives '/rr who'. Lists all members of players religion who are online in white, and all members associated with players tower who are online in red 
		 */
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("who") && arg.length == 1){
			if(BeyondInfo.getReligion((Player)sender) == null){
				((Player)sender).sendMessage("You are not a member of a Tower and are thus unable to use this command");
				return true;
			}
			String message = "";
			System.out.println("BeyondInfo.getTowerName((Player)sender) "+BeyondInfo.getTowerName((Player)sender));
			List<Player> towerplayers = BeyondInfo.getTowerPlayers(BeyondInfo.getTowerName((Player)sender));
			List<Player> religionplayers = BeyondInfo.getReligionPlayers(BeyondInfo.getReligion((Player)sender));
			for(int i=0;i<religionplayers.size();i++){
				if(towerplayers.contains(religionplayers.get(i))) message = message.concat(ChatColor.RED.toString()+religionplayers.get(i).getName());
				else message = message.concat(ChatColor.WHITE.toString()+religionplayers.get(i).getName());
			}
			((Player)sender).sendMessage(message);
			return true;
		}
		
		else return false;
	}

}
