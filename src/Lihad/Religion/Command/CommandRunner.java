package Lihad.Religion.Command;

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
	//TODO: Make symbols non-valid in command.
	//TODO: Make this look better
	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] arg) {
		if(cmd.getName().equalsIgnoreCase("rr") && arg.length == 0){
			sender.sendMessage(Religion.info.getReligions().toString());
			return true;
		}else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("join")){
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
		}else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("leave")){
			if(!Religion.info.hasPlayer((Player)sender)){
				sender.sendMessage("YOU ARENT PART OF A RELIGION YOU CUNT");
				return true;
			}
			Religion.info.removePlayer((Player)sender);
			sender.sendMessage("You have left your tower and religion");
			return true;
		}else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("list")){
			sender.sendMessage("The religions that exist are: "+Religion.info.getReligions().toString());
			return true;
		}else if(cmd.getName().equalsIgnoreCase("rr") && Religion.info.getReligions().contains(arg[0]) && arg[1].equals("list")){
			sender.sendMessage("The towers that exist are: "+Religion.info.getTowers(arg[0]));
			return true;
		}else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("details") && !(arg[1].isEmpty())){
			if(Religion.info.hasTower(arg[1])){
				sender.sendMessage("General Info");
			}else{
				sender.sendMessage("This is not a valid tower. The towers that exist are: "+Religion.info.getTowers(arg[0]));
			}
			return true;
		}else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("details")){
			if(Religion.info.hasPlayer((Player)sender)){
				sender.sendMessage("In-depth Info");
			}else{
				sender.sendMessage("This command is invalid because you are not a member of a tower");
			}
			return true;
		} else return false;
	}

}
