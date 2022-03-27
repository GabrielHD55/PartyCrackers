package com.gabrielhd.partycrackers.Commands;

import com.gabrielhd.partycrackers.Config.YamlConfig;
import com.gabrielhd.partycrackers.Crackers.Cracker;
import com.gabrielhd.partycrackers.Main;
import com.gabrielhd.partycrackers.Utils.ItemBuilder;
import com.gabrielhd.partycrackers.Utils.Utils;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;

public class Commands implements CommandExecutor, TabCompleter {

    private final Main plugin;

    public Commands(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        YamlConfig messages = this.plugin.getConfigManager().getMessages();

        if((sender instanceof Player player)) {
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("reload")) {
                    if(!player.hasPermission("partycrackers.reload")) {
                        player.sendMessage(Utils.Color(messages.getString("NoPermissions")));
                        return true;
                    }

                    if(this.plugin.getConfigManager().reload()) {
                        this.plugin.getCrackerManager().loadCrackers();

                        player.sendMessage(Utils.Color("&aPlugin reloaded successfully!"));
                        return true;
                    }
                    player.sendMessage(Utils.Color("&cFailed to reload the plugin, please check the console!"));
                    return true;
                }
            }

            if(args.length == 4) {
                if(args[0].equalsIgnoreCase("give")) {
                    if(!player.hasPermission("partycrackers.give")) {
                        player.sendMessage(Utils.Color(messages.getString("NoPermissions")));
                        return true;
                    }

                    Cracker cracker = this.plugin.getCrackerManager().getCracker(args[1]);
                    if(cracker == null) {
                        player.sendMessage(Utils.Color(messages.getString("CrackerNotExists")));
                        return true;
                    }

                    int amount = Utils.parseInt(args[2]);

                    Player target = Bukkit.getPlayer(args[3]);

                    if(target == null || !player.isOnline()) {
                        player.sendMessage(Utils.Color(messages.getString("PlayerNotOnline")));
                        return true;
                    }

                    player.sendMessage(Utils.Color(messages.getString("CrackerGiven").replace("%name%", cracker.getName()).replace("%displayname%", cracker.getDisplayName())));
                    ItemBuilder itemBuilder = cracker.getItemBuilder().clone();
                    itemBuilder.setAmount(amount);

                    target.getInventory().addItem(itemBuilder.build());

                    target.sendMessage(Utils.Color(messages.getString("CrackerReceived").replace("%name%", cracker.getName()).replace("%displayname%", cracker.getDisplayName())));
                    return true;
                }
            }

            this.sendHelp(player);
            return true;
        }
        sender.sendMessage(Utils.Color(messages.getString("OnlyPlayers")));
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        if((sender instanceof Player player)) {
            if(args.length == 1) {
                List<String> completions = Lists.newArrayList();

                StringUtil.copyPartialMatches(args[0], Lists.newArrayList("reload", "give"), completions);

                Collections.sort(completions);
                return completions;
            }

            if(args.length == 2) {
                List<String> completions = Lists.newArrayList();

                StringUtil.copyPartialMatches(args[1], this.plugin.getCrackerManager().getCrackers().keySet(), completions);

                Collections.sort(completions);
                return completions;
            }

            if(args.length == 4) {
                List<String> completions = Lists.newArrayList();

                List<String> playerNames = Lists.newArrayList();
                for(Player online : Bukkit.getOnlinePlayers()) {
                    playerNames.add(online.getName());
                }

                StringUtil.copyPartialMatches(args[3], playerNames, completions);

                Collections.sort(completions);
                return completions;
            }
        }
        return Lists.newArrayList();
    }

    public void sendHelp(Player player) {
        player.sendMessage(Utils.Color("&f"));
        player.sendMessage(Utils.Color("      &6&lPartyCrackers Help"));
        player.sendMessage(Utils.Color("&f"));
        player.sendMessage(Utils.Color("&e/partycrackers reload &8- &7Use this command to reload the plugin."));
        player.sendMessage(Utils.Color("&e/partycrackers give <Cracker name> <Amount> (Player) &8- &7Use this command to give a PartyCracker to a player."));
        player.sendMessage(Utils.Color("&f"));
        player.sendMessage(Utils.Color("      &6Plugin by &lGabrielHD"));
    }
}
