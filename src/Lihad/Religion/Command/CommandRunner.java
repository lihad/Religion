package Lihad.Religion.Command;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import Lihad.Religion.Religion;
import Lihad.Religion.Bosses.Bosses;
import Lihad.Religion.Config.BeyondConfig;
import Lihad.Religion.Information.BeyondInfo;
import Lihad.Religion.Util.BeyondUtil;

public class CommandRunner implements CommandExecutor {
	public static Religion plugin;
	public ItemStack post;

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
		 * 
		 * Completely changed how members join a tower, and the hierarchy within the tower.  There are "Leader", "Trusted" members and Members.
		 * Leaders get '/rr trust <playername>' and '/rr exile <playername>' and '/rr leader <playername>' and '/rr invite <playername>'
		 * Trusted get '/rr exile <playername>' and '/rr invite <playername>'
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
			if(BeyondInfo.getTowerName((Player)sender) == null){
				sender.sendMessage("You are not a member of any religion");
			}
			else sender.sendMessage("You are a member of "+BeyondInfo.getTowerName((Player)sender)+", a tower of "+BeyondInfo.getReligion((Player)sender));
			return true;
		}
		
		/**
		 * TODO: I believe '/rr join' with no param throws an exception.  This needs to be fixed
		 * Drives '/rr join <param>' command.  This allows a player to join the chosen tower if they exist on the invited list.
		 * 
		 *
		 */
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("join") && arg.length == 2){
			if(!BeyondInfo.hasTower(arg[1])){
				sender.sendMessage("This is not a valid tower");
			}
			else if(BeyondInfo.hasPlayer((Player)sender)){
				sender.sendMessage("You are already a member of a Tower.");
			}
			else if(BeyondInfo.isMemberInvited((Player)sender, arg[1])){
				BeyondInfo.addPlayer((Player)sender, arg[1]);
				sender.sendMessage("You have joined the Tower of "+arg[1]+" and the Religion of "+BeyondInfo.getReligion(arg[1]));
				BeyondInfo.removeInvited(((Player)sender), arg[1]);
			}else{
				sender.sendMessage("You have yet to be invited to join this tower");
			}
			return true;
		}
		/**
		 * 
		 */
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("exile") && arg.length == 2){
			if(!BeyondInfo.hasPlayer((Player)sender)){
				sender.sendMessage("You are not able to use this command");
			}
			else if(BeyondInfo.isPlayerAMember(arg[1], BeyondInfo.getTowerName((Player)sender))){
				BeyondInfo.removePlayer(arg[1]);
				sender.sendMessage(arg[1]+" has been removed from "+BeyondInfo.getTowerName((Player)sender));
				if(plugin.getServer().getPlayer(arg[1]) != null){
					plugin.getServer().getPlayer(arg[1]).sendMessage("You have been removed from "+BeyondInfo.getTowerName((Player)sender));
				}
			}else{
				sender.sendMessage("That player does not seem to be a member of this tower.  Check your caps.");
			}
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("invite") && arg.length == 2){
			if(plugin.getServer().getPlayer(arg[1]) == null){
				sender.sendMessage("This player is either not online of doesn't exist");
				return true;
			}
			if(BeyondInfo.getInvitedList((Player)sender) == null){
			}else if(BeyondInfo.getInvitedList((Player)sender).contains(plugin.getServer().getPlayer(arg[1]).getName())){
				sender.sendMessage("This player was already invited to join your tower");
				return true;
			}
			if(BeyondInfo.isMemberTrusted((Player)sender) || BeyondInfo.isPlayerLeader((Player)sender)){
				sender.sendMessage(arg[1]+" was invited to join your tower");
				plugin.getServer().getPlayer(arg[1]).sendMessage("You were invited to join "+BeyondInfo.getTowerName((Player)sender));
				BeyondInfo.addInvited(plugin.getServer().getPlayer(arg[1]).getName(), BeyondInfo.getTowerName((Player)sender));
			}
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("trust") && arg.length == 2){
			if(!BeyondInfo.getOnlineReligionPlayers().contains((Player)sender)){
				sender.sendMessage("This command is invalid as you are not a leader of a tower");
				return true;
			}else if(BeyondInfo.getTrustedList((Player)sender) == null){
				
			}else if(BeyondInfo.getTrustedList((Player)sender).contains(arg[1])){
				sender.sendMessage("This member is already on the trust list");
				return true;
			}
			if(BeyondInfo.isPlayerLeader((Player)sender) && BeyondInfo.isPlayerAMember(arg[1], BeyondInfo.getTowerName((Player)sender))){
				BeyondInfo.addTrusted(arg[1], BeyondInfo.getTowerName((Player)sender));
				sender.sendMessage("Member "+arg[1]+" was made trusted.");
			}else{
				sender.sendMessage("This command is invalid as you are not the leader or that player is not currently online");
			}
			return true;
		}

		/**
		 * Drives '/rr leave'.  Allows player to leave tower and religion
		 */
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("leave") && arg.length == 1){
			if(!BeyondInfo.hasPlayer((Player)sender)){
				sender.sendMessage("You are not a member of a tower, and thus, this command does nothing");
			}
			else if(BeyondInfo.getLeader((Player)sender) != null){
				if(BeyondInfo.getLeader((Player)sender).equals(((Player)sender).getName())){
				BeyondUtil.towerBroadcast(BeyondInfo.getTowerName((Player)sender), "The leader of Tower "+BeyondInfo.getTowerName((Player)sender)+" has parished");
				BeyondInfo.getTowerLocation(BeyondInfo.getTowerName((Player)sender)).getBlock().setTypeId(0);
				BeyondInfo.getTowerLocation(BeyondInfo.getTowerName((Player)sender)).getWorld().createExplosion(BeyondInfo.getTowerLocation(BeyondInfo.getTowerName((Player)sender)), 5, true);
				BeyondInfo.removeTower(BeyondInfo.getReligion(BeyondInfo.getTowerName((Player)sender)), BeyondInfo.getTowerName((Player)sender));
				}else{
					BeyondInfo.removePlayer((Player)sender);
					sender.sendMessage("You have left your tower and religion");
				}
			}else{
				BeyondInfo.removePlayer((Player)sender);
				sender.sendMessage("You have left your tower and religion");
			}
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("force") && arg.length == 1 && ((Player)sender).isOp()){
			//Bosses.bossExistMap.clear();
			//Bosses.bossHealthMap.clear();
			//Bosses.bossMaxHealthMap.clear();
			//Religion.bosses.bossInit();
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
		else if(arg.length == 2 && cmd.getName().equalsIgnoreCase("rr") && BeyondInfo.getReligions().contains(arg[0]) && arg[1].equals("list")){
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
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("whois") && arg.length == 2){
			if(BeyondInfo.getTowerNamePlayerString(arg[1]) != null){
				((Player)sender).sendMessage(arg[1]+" is a member of the tower "+BeyondUtil.getChatColor((Player)sender, BeyondInfo.getTowerNamePlayerString(arg[1]))+BeyondInfo.getTowerNamePlayerString(arg[1])+", Religion of "+BeyondInfo.getReligion(BeyondInfo.getTowerNamePlayerString(arg[1])));
			}else{
				((Player)sender).sendMessage("That player does not exist or does not belong to a religion");
			}
			return true;

		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("rarity") && arg.length == 1){
			if(BeyondUtil.rarity(((Player)sender).getItemInHand()) >= 60)((Player)sender).sendMessage("The Rarity Index of your "+ChatColor.BLUE.toString()+((Player)sender).getItemInHand().getType().name()+" is "+BeyondUtil.getColorOfRarity(BeyondUtil.rarity(((Player)sender).getItemInHand()))+BeyondUtil.rarity(((Player)sender).getItemInHand()));
			else ((Player)sender).sendMessage("This item has no Rarity Index");
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("post") && arg.length == 1){
			if(BeyondUtil.rarity(((Player)sender).getItemInHand()) >= 60){
				((Player)sender).chat(BeyondUtil.getColorOfRarity(BeyondUtil.rarity(((Player)sender).getItemInHand()))+"["+((Player)sender).getItemInHand().getType().name()+"] Rarity Index : "+BeyondUtil.rarity(((Player)sender).getItemInHand()));
				post = ((Player)sender).getItemInHand();
			}
			else ((Player)sender).sendMessage("This item has no Rarity Index so it can't be posted to chat");
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("look") && arg.length == 1){
			if(post != null){
				Player player =((Player)sender);
				player.sendMessage(ChatColor.YELLOW.toString()+" -------------------------------- ");
				player.sendMessage(BeyondUtil.getColorOfRarity(BeyondUtil.rarity(post))+"["+post.getType().name()+"] Rarity Index : "+BeyondUtil.rarity(post));
				for(int i = 0; i<post.getEnchantments().keySet().size(); i++){
					player.sendMessage(" -- "+ChatColor.BLUE.toString()+((Enchantment)(post.getEnchantments().keySet().toArray()[i])).getName()+ChatColor.WHITE.toString()+" LVL"+BeyondUtil.getColorOfLevel(post.getEnchantmentLevel(((Enchantment)(post.getEnchantments().keySet().toArray()[i]))))+post.getEnchantmentLevel(((Enchantment)(post.getEnchantments().keySet().toArray()[i]))));
				}
				if(post.getEnchantments().keySet().size() <= 0)player.sendMessage(ChatColor.WHITE.toString()+" -- This Item Has No Enchants");

				player.sendMessage(ChatColor.YELLOW.toString()+" -------------------------------- ");

			}else ((Player)sender).sendMessage("There is no item to look at");
			return true;
		}

		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("gear") && arg.length == 1){
			double total = (BeyondUtil.rarity(((Player)sender).getInventory().getHelmet())+BeyondUtil.rarity(((Player)sender).getInventory().getLeggings())+BeyondUtil.rarity(((Player)sender).getInventory().getBoots())+BeyondUtil.rarity(((Player)sender).getInventory().getChestplate())+BeyondUtil.rarity(((Player)sender).getInventory().getItemInHand()))/5.00;
			((Player)sender).sendMessage(BeyondUtil.getColorOfRarity(total)+"Your Gear Rating is : "+total);
			return true;
		}
		//Commented out until PEX fixes broadcast
		/**
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("gear") && arg[1].equals("post") && arg.length == 2){
			System.out.println("REF1");
			double total = (BeyondUtil.rarity(((Player)sender).getInventory().getHelmet())+BeyondUtil.rarity(((Player)sender).getInventory().getLeggings())+BeyondUtil.rarity(((Player)sender).getInventory().getBoots())+BeyondUtil.rarity(((Player)sender).getInventory().getChestplate())+BeyondUtil.rarity(((Player)sender).getInventory().getItemInHand()))/5.00;
			System.out.println("REF2");
			((Player)sender).getServer.broadcastMessage(" -- ["+((Player)sender).getName()+"] has a Gear Rating of "+BeyondUtil.getColorOfRarity(total)+total);
			System.out.println("REF3");

			return true;
		}
		*/
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("gear") && arg.length == 2){
			if(plugin.getServer().getPlayer(arg[1]) != null){
				Player player = plugin.getServer().getPlayer(arg[1]);
				double total = (BeyondUtil.rarity(player.getInventory().getHelmet())+BeyondUtil.rarity(player.getInventory().getLeggings())+BeyondUtil.rarity(player.getInventory().getBoots())+BeyondUtil.rarity(player.getInventory().getChestplate())+BeyondUtil.rarity(player.getInventory().getItemInHand()))/5.00;
				((Player)sender).sendMessage(player.getName()+" has a Gear Rating of "+BeyondUtil.getColorOfRarity(total)+total);
			}else ((Player)sender).sendMessage("This player either doesn't exist, or isn't online");
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("dz") && arg.length == 1){
			if(BeyondInfo.getDevastationZones() != null){
				Location location = BeyondInfo.getNearestDZLocation(((Player)sender).getLocation());
				if(location != null){
					((Player)sender).sendMessage("The nearest DZ is located at: X"+location.getBlockX()+" Z"+location.getBlockZ());
				}else{
					((Player)sender).sendMessage("There are currently no Devastation Zones that need repair. Check later.");
				}
			}else{
				((Player)sender).sendMessage("There are currently no Devastation Zones that need repair. Check later.");
			}
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("dz") && arg[1].equals("remove") && arg.length == 2 && Religion.handler.has(((Player)sender), "religion.dz")){
			List<String> zones = BeyondInfo.getDevastationZones();
			if(zones == null)return false;
			for(int i=0;i<zones.size();i++){
				if(BeyondInfo.isDevastationZone(((Player)sender).getLocation(), zones.get(i))){
					BeyondInfo.removeDevastationZone(zones.get(i));
					break;
				}
			}
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("holy") && arg[1].equals("set") && arg.length == 2 && BeyondInfo.isPlayerLeader((Player)sender)){
			if(BeyondInfo.getDistanceToTower(((Player)sender).getLocation(), BeyondInfo.getClosestValidTower(((Player)sender).getLocation())) < 600){
				((Player)sender).sendMessage("This Holy Zone is too close to a tower to be set.");
				return true;
			}
			if(BeyondInfo.hasHolyZone(BeyondInfo.getTowerName(((Player)sender)))){
				((Player)sender).sendMessage("A Holy Zone has already been set for your tower.");
				return true;
			}
			BeyondInfo.setHolyZone(BeyondInfo.getTowerName(((Player)sender)), ((Player)sender).getLocation());
			return true;
		}
		
		else return false;
	}
}
