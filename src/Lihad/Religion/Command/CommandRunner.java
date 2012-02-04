package Lihad.Religion.Command;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import Lihad.Religion.Religion;



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
		 * TODO: Maybe give some sort of 'help' page connected with this command
		 * Base Command, drives /rr
		 */
		if(cmd.getName().equalsIgnoreCase("rr") && arg.length == 0){
			sender.sendMessage(Religion.info.getReligions().toString());
			return true;
		}
		
		/**
		 * TODO: I believe '/rr join' with no param throws an exception.  This needs to be fixed
		 * Drives '/rr join <param>' command.  This allows a player to join the chosen tower. 
		 */
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("join")){
			if(!Religion.info.hasTower(arg[1])){
				sender.sendMessage("Invalid tower you fuck head");
				return true;
			}
			if(Religion.info.hasPlayer((Player)sender)){
				sender.sendMessage("You are already a member of a Tower. Please leave your current one to join this one.");
				return true;
			}
			Religion.info.addPlayer((Player)sender, Religion.info.getReligion(arg[1]), arg[1]);
			sender.sendMessage("You have joined the Tower of "+arg[1]+" and the Religion of "+Religion.info.getReligion(arg[1]));
			return true;
		}
		
		/**
		 * Drives '/rr leave'.  Allows player to leave tower and religion
		 */
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("leave")){
			if(!Religion.info.hasPlayer((Player)sender)){
				sender.sendMessage("YOU ARENT PART OF A RELIGION YOU CUNT");
				return true;
			}
			Religion.info.removePlayer((Player)sender);
			sender.sendMessage("You have left your tower and religion");
			return true;
		}
		
		/**
		 * Drives '/rr list'.  Lists to player all religions that exist
		 */
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("list")){
			sender.sendMessage("The religions that exist are: "+Religion.info.getReligions().toString());
			return true;
		}
		
		/**
		 * Drives '/rr <religion> list' Lists all towers associated with param religion
		 */
		else if(cmd.getName().equalsIgnoreCase("rr") && Religion.info.getReligions().contains(arg[0]) && arg[1].equals("list")){
			sender.sendMessage("The towers that exist are: "+Religion.info.getTowers(arg[0]));
			return true;
		}
		
		/**
		 * TODO: I don't think this works... make it work.  General details could include members, tower name, location.
		 * Drives '/rr details <towername>'  Gets general details of a tower.
		 */
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("details") && !(arg[1].isEmpty())){
			if(Religion.info.hasTower(arg[1])){
				sender.sendMessage("General Info");
			}else{
				sender.sendMessage("This is not a valid tower. The towers that exist are: "+Religion.info.getTowers(arg[0]));
			}
			return true;
		}
		
		/**
		 * TODO: I don't think this works... make it work.  In-Depth details could include members, tower name, location, influence
		 *Drives '/rr details' Gets in-depth details about a player specific religion
		 */
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("details")){
			if(Religion.info.hasPlayer((Player)sender)){
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
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("here")){
			sender.sendMessage("You are in the territory of "+Religion.info.getTower(((Player)sender).getLocation()));
			return true;
		}
		
		/**
		 * 
		 * Drives '/rr who'. Lists all members of players religion who are online in white, and all members associated with players tower who are online in red 
		 */
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("who")){
			String message = "";
			List<Player> onlineplayers = Arrays.asList(plugin.getServer().getOnlinePlayers());
			List<Player> towerplayers = Religion.info.getTowerPlayers(Religion.info.getTowerName((Player)sender));
			List<Player> religionplayers = Religion.info.getReligionPlayers(Religion.info.getReligion((Player)sender));
			for(int i=0;i<religionplayers.size();i++){
				if(towerplayers.contains(religionplayers.get(i)) && onlineplayers.contains(religionplayers.get(i))) message = message.concat(ChatColor.RED.toString()+religionplayers.get(i).getName());
				else if(onlineplayers.contains(religionplayers.get(i))) message = message.concat(ChatColor.WHITE.toString()+religionplayers.get(i).getName());
			}
			((Player)sender).sendMessage(message);
			return true;
		}
		
		else return false;
	}

}
