package Lihad.Religion.Command;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import Lihad.Religion.Religion;
import Lihad.Religion.Bosses.Ahkmed;
import Lihad.Religion.Bosses.Bosses;
import Lihad.Religion.Config.BeyondConfig;
import Lihad.Religion.Information.BeyondInfo;
import Lihad.Religion.Listeners.BeyondPlayerListener;
import Lihad.Religion.Util.BeyondUtil;
import Lihad.Religion.Util.Notification;

public class CommandRunner implements CommandExecutor {
	public static Religion plugin;
	public ItemStack post;
	public static List<String> playersToBeRemoved = new LinkedList<String>();

	public CommandRunner(Religion instance){
		plugin = instance;
	}

	
	private void defaultCommand(CommandSender sender, Command cmd, String string,
			String[] arg){
		if(BeyondInfo.getTowerName((Player)sender) == null){
			sender.sendMessage("You are not a member of any religion");
		}
		else sender.sendMessage("You are a member of "+BeyondInfo.getTowerName((Player)sender)+", a tower of "+BeyondInfo.getReligion((Player)sender));		
	}
	private void joinCommand(CommandSender sender, Command cmd, String string,
			String[] arg){
		if(!BeyondInfo.hasTower(arg[1])){
			sender.sendMessage("This is not a valid tower");
		}
		else if(BeyondInfo.hasPlayer((Player)sender)){
			sender.sendMessage("You are already a member of a Tower.");
		}
		else if(BeyondInfo.isMemberInvited((Player)sender, arg[1])){
			BeyondInfo.addPlayer((Player)sender, arg[1]);
			plugin.notification("Player, "+((Player)sender).getName()+", has joined Tower "+arg[1]+", Religion of "+BeyondInfo.getReligion(arg[1]));
			Notification.event.add(ChatColor.WHITE.toString()+" - "+ChatColor.LIGHT_PURPLE.toString()+((Player)sender).getName()+ChatColor.GRAY.toString()+" joined "+ChatColor.LIGHT_PURPLE.toString()+arg[1]+ChatColor.GRAY.toString()+", Religion of "+ChatColor.LIGHT_PURPLE.toString()+BeyondInfo.getReligion(arg[1]));
			plugin.setPlayerSuffix(((Player)sender));
			sender.sendMessage("You have joined the Tower of "+arg[1]+" and the Religion of "+BeyondInfo.getReligion(arg[1]));
			BeyondInfo.removeInvited(((Player)sender), arg[1]);
		}else{
			sender.sendMessage("You have yet to be invited to join this tower");
		}
	}
	private void exileCommand(CommandSender sender, Command cmd, String string,
			String[] arg){
		if(!BeyondInfo.hasPlayer((Player)sender)){
			sender.sendMessage("You are not able to use this command");
		}
		else if(BeyondInfo.isPlayerAMember(arg[1], BeyondInfo.getTowerName((Player)sender))){
			BeyondInfo.removePlayer(arg[1]);
			sender.sendMessage(arg[1]+" has been removed from "+BeyondInfo.getTowerName((Player)sender));
			plugin.notification("Player, "+arg[1]+", has beened exiled by "+((Player)sender).getName()+", Tower of "+BeyondInfo.getTowerName((Player)sender));

			if(plugin.getServer().getPlayer(arg[1]) != null){
				plugin.getServer().getPlayer(arg[1]).sendMessage("You have been removed from "+BeyondInfo.getTowerName((Player)sender));
			}
		}else{
			sender.sendMessage("That player does not seem to be a member of this tower.  Check your caps.");
		}
	}
	private void inviteCommand(CommandSender sender, Command cmd, String string,
			String[] arg){
		if(plugin.getServer().getPlayer(arg[1]) == null){
			sender.sendMessage("This player is either not online of doesn't exist");
			return;
		}
		if(BeyondInfo.getInvitedList((Player)sender) == null){
		}else if(BeyondInfo.getInvitedList((Player)sender).contains(plugin.getServer().getPlayer(arg[1]).getName())){
			sender.sendMessage("This player was already invited to join your tower");
			return;
		}
		if(BeyondInfo.isMemberTrusted((Player)sender) || BeyondInfo.isPlayerLeader((Player)sender)){
			sender.sendMessage(arg[1]+" was invited to join your tower");
			plugin.getServer().getPlayer(arg[1]).sendMessage("You were invited to join "+BeyondInfo.getTowerName((Player)sender));
			BeyondInfo.addInvited(plugin.getServer().getPlayer(arg[1]).getName(), BeyondInfo.getTowerName((Player)sender));
		}
	}
	private void trustCommand(CommandSender sender, Command cmd, String string,
			String[] arg){
		if(!BeyondInfo.getOnlineReligionPlayers().contains((Player)sender)){
			sender.sendMessage("This command is invalid as you are not a leader of a tower");
			return;
		}else if(BeyondInfo.getTrustedList((Player)sender) == null){
			
		}else if(BeyondInfo.getTrustedList((Player)sender).contains(arg[1])){
			sender.sendMessage("This member is already on the trust list");
			return;
		}
		if(BeyondInfo.isPlayerLeader((Player)sender) && BeyondInfo.isPlayerAMember(arg[1], BeyondInfo.getTowerName((Player)sender))){
			BeyondInfo.addTrusted(arg[1], BeyondInfo.getTowerName((Player)sender));
			sender.sendMessage("Member "+arg[1]+" was made trusted.");
		}else{
			sender.sendMessage("This command is invalid as you are not the leader or that player is not currently online");
		}
	}
	private void leaveCommand(CommandSender sender, Command cmd, String string,
			String[] arg){
		if(!BeyondInfo.hasPlayer((Player)sender)){
			sender.sendMessage("You are not a member of a tower, and thus, this command does nothing");
		}
		else if(BeyondInfo.getLeader((Player)sender) != null){
			if(BeyondInfo.getLeader((Player)sender).equals(((Player)sender).getName())){
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run() {
						plugin.notification("Player "+playersToBeRemoved.get(0)+" has left the Tower "+BeyondInfo.getTowerNamePlayerString((playersToBeRemoved.get(0))));
						BeyondInfo.getTowerLocation(BeyondInfo.getTowerNamePlayerString(playersToBeRemoved.get(0))).getBlock().setTypeId(0);
						BeyondInfo.getTowerLocation(BeyondInfo.getTowerNamePlayerString(playersToBeRemoved.get(0))).getWorld().createExplosion(BeyondInfo.getTowerLocation(BeyondInfo.getTowerNamePlayerString(playersToBeRemoved.get(0))), 5, true);
						BeyondUtil.towerBroadcast(BeyondInfo.getTowerNamePlayerString(playersToBeRemoved.get(0)), "The leader of Tower "+BeyondInfo.getTowerNamePlayerString(playersToBeRemoved.get(0))+" has perished");
						BeyondInfo.removeTower(BeyondInfo.getReligion(BeyondInfo.getTowerNamePlayerString(playersToBeRemoved.get(0))), BeyondInfo.getTowerNamePlayerString(playersToBeRemoved.get(0)));
						playersToBeRemoved.remove(0);
						if(plugin.getServer().getPlayer(playersToBeRemoved.get(0)) != null)plugin.setPlayerSuffix(plugin.getServer().getPlayer((playersToBeRemoved.get(0))));
					}
				}, 72000L);
				playersToBeRemoved.add(((Player)sender).getName());
				plugin.notification("Player "+((Player)sender).getName()+" is beginning the process to leave Tower "+BeyondInfo.getTowerNamePlayerString(((Player)sender).getName()));

			}else{
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run() {
						plugin.notification("Player "+playersToBeRemoved.get(0)+" has left the Tower "+BeyondInfo.getTowerNamePlayerString((playersToBeRemoved.get(0))));
						BeyondInfo.removePlayer(playersToBeRemoved.get(0));
						playersToBeRemoved.remove(0);
					}
				}, 72000L);
				playersToBeRemoved.add(((Player)sender).getName());
				plugin.notification("Player "+((Player)sender).getName()+" is beginning the process to leave Tower "+BeyondInfo.getTowerNamePlayerString(((Player)sender).getName()));

			}
		}else{
			BeyondInfo.removePlayer((Player)sender);
			sender.sendMessage("You have left your tower and religion");
		}
	}
	private void hereCommand(CommandSender sender, Command cmd, String string,
			String[] arg) {
		List<String> towers = BeyondInfo.getTowersOfLocation(((Player)sender).getLocation());
		if(towers.size() == 0) sender.sendMessage("You are not in a defined territory");
		else sender.sendMessage("You are in the territory of "+towers);
	}
	private void whoCommand(CommandSender sender, Command cmd, String string,
			String[] arg) {
		if(BeyondInfo.getReligion((Player)sender) == null){
			((Player)sender).sendMessage("You are not a member of a Tower and are thus unable to use this command");
			return;
		}
		String message = "";
		List<Player> towerplayers = BeyondInfo.getTowerPlayers(BeyondInfo.getTowerName((Player)sender));
		List<Player> religionplayers = BeyondInfo.getReligionPlayers(BeyondInfo.getReligion((Player)sender));
		for(int i=0;i<religionplayers.size();i++){
			if(towerplayers.contains(religionplayers.get(i))) message = message.concat(ChatColor.RED.toString()+religionplayers.get(i).getName());
			else message = message.concat(ChatColor.WHITE.toString()+religionplayers.get(i).getName());
		}
		((Player)sender).sendMessage(message);
	}
	private void whoisCommand(CommandSender sender, Command cmd, String string,
			String[] arg) {
		if(BeyondInfo.getTowerNamePlayerString(arg[1]) != null){
			((Player)sender).sendMessage(arg[1]+" is a member of the tower "+BeyondUtil.getChatColor((Player)sender, BeyondInfo.getTowerNamePlayerString(arg[1]))+BeyondInfo.getTowerNamePlayerString(arg[1])+", Religion of "+BeyondInfo.getReligion(BeyondInfo.getTowerNamePlayerString(arg[1])));
		}else{
			((Player)sender).sendMessage("That player does not exist or does not belong to a religion");
		}
	}
	private void lookCommand(CommandSender sender, Command cmd, String string,
			String[] arg) {
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
	}
	private void dzCommand(CommandSender sender, Command cmd, String string,
			String[] arg) {
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
	}	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] arg) {
		if(cmd.getName().equalsIgnoreCase("rr") && arg.length == 0){
			defaultCommand(sender, cmd, string, arg);
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("join") && arg.length == 2){
			joinCommand(sender, cmd, string, arg);
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("exile") && arg.length == 2){
			exileCommand(sender, cmd, string, arg);
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("invite") && arg.length == 2){
			inviteCommand(sender, cmd, string, arg);
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("trust") && arg.length == 2){
			trustCommand(sender, cmd, string, arg);
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("leave") && arg.length == 1){
			leaveCommand(sender, cmd, string, arg);
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("force") && arg.length == 1 && ((Player)sender).isOp()){
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("list") && arg.length == 1){
			sender.sendMessage("The religions that exist are: "+BeyondInfo.getReligions().toString());
			return true;
		}
		else if(arg.length == 2 && cmd.getName().equalsIgnoreCase("rr") && BeyondInfo.getReligions().contains(arg[0]) && arg[1].equals("list")){
			sender.sendMessage("The towers that exist are: "+BeyondInfo.getTowers(arg[0]));
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("details") && arg.length == 2){
			if(BeyondInfo.hasTower(arg[1])){
				String religion = BeyondInfo.getReligion(arg[1]);
				sender.sendMessage("-------------------------");
				sender.sendMessage("Towername: "+arg[1]+" Religion: "+religion);
				sender.sendMessage("Members: "+BeyondInfo.getTowerMemberCount(arg[1])+" Influence: "+BeyondInfo.getTowerInfluence(arg[1]));
				sender.sendMessage("-------------------------");
			}else{
				sender.sendMessage("This is not a valid tower. The towers that exist are: "+BeyondInfo.getTowersAll());
			}
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("details") && arg.length == 1){
			if(BeyondInfo.hasPlayer((Player)sender)){
				String religion = BeyondInfo.getReligion((Player)sender);
				sender.sendMessage("-------------------------");
				sender.sendMessage("Towername: "+BeyondInfo.getTowerName((Player)sender)+" Religion: "+religion);
				sender.sendMessage("Members: "+BeyondInfo.getTowerMemberCount((Player)sender)+" Influence: "+BeyondInfo.getTowerInfluence((Player)sender));
				sender.sendMessage("-------------------------");
			}else{
				sender.sendMessage("This command is invalid because you are not a member of a tower");
			}
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("here") && arg.length == 1){
			hereCommand(sender, cmd, string, arg);
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("who") && arg.length == 1){
			whoCommand(sender, cmd, string, arg);
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("whois") && arg.length == 2){
			whoisCommand(sender, cmd, string, arg);
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
			lookCommand(sender, cmd, string, arg);
			return true;
		}

		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("gear") && arg.length == 1){
			double total = (BeyondUtil.rarity(((Player)sender).getInventory().getHelmet())+BeyondUtil.rarity(((Player)sender).getInventory().getLeggings())+BeyondUtil.rarity(((Player)sender).getInventory().getBoots())+BeyondUtil.rarity(((Player)sender).getInventory().getChestplate())+BeyondUtil.rarity(((Player)sender).getInventory().getItemInHand()))/5.00;
			((Player)sender).sendMessage(BeyondUtil.getColorOfRarity(total)+"Your Gear Rating is : "+total);
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("gear") && arg.length == 2){
			if(plugin.getServer().getPlayer(arg[1]) != null){
				Player player = plugin.getServer().getPlayer(arg[1]);
				double total = (BeyondUtil.rarity(player.getInventory().getHelmet())+BeyondUtil.rarity(player.getInventory().getLeggings())+BeyondUtil.rarity(player.getInventory().getBoots())+BeyondUtil.rarity(player.getInventory().getChestplate())+BeyondUtil.rarity(player.getInventory().getItemInHand()))/5.00;
				((Player)sender).sendMessage(player.getName()+" has a Gear Rating of "+BeyondUtil.getColorOfRarity(total)+total);
			}else ((Player)sender).sendMessage("This player either doesn't exist, or isn't online");
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("dz") && arg.length == 1){
			dzCommand(sender, cmd, string, arg);
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
			if(BeyondInfo.getDistanceToTower(((Player)sender).getLocation(), BeyondInfo.getClosestTower(((Player)sender).getLocation())) < 600){
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

		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("check")&& arg.length == 1){
			if(BeyondUtil.isActiveArea((Player)sender, null))((Player)sender).sendMessage(ChatColor.RED.toString()+"This area is active, you will be unable to create a tower here.");
			else ((Player)sender).sendMessage(ChatColor.GREEN.toString()+"This area is desolate");
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("rr") && arg[0].equals("examine")&& arg.length == 1 && BeyondInfo.hasPlayer((Player)sender)){
			String say = " The players inside your AoE are: ";
			for(int i = 0;i<plugin.getServer().getOnlinePlayers().length;i++){
				if(BeyondInfo.isTowerArea(plugin.getServer().getOnlinePlayers()[i].getLocation(), BeyondInfo.getTowerName((Player)sender))){
					say = say.concat(BeyondUtil.getChatColor((Player)sender, BeyondInfo.getTowerName(plugin.getServer().getOnlinePlayers()[i]))+"["+plugin.getServer().getOnlinePlayers()[i].getName()+" @{"+plugin.getServer().getOnlinePlayers()[i].getLocation().getBlockX()+", "+plugin.getServer().getOnlinePlayers()[i].getLocation().getBlockZ()+"}"+ChatColor.WHITE.toString()+"]");
				}
			}
			((Player)sender).sendMessage(say);
			return true;
		}
		else return false;
	}
}
