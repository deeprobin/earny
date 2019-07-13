package de.deeprobin.earny.platform.bukkit.listener;

import de.deeprobin.earny.platform.bukkit.EarnyPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final EarnyPlugin plugin;

    public ChatListener(final EarnyPlugin plugin){
        this.plugin = plugin;
        this.plugin.getLogger().info("Initialized chat listener.");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        event.setMessage(this.plugin.getFactory().replaceChatMessage(event.getMessage()));
    }

}
