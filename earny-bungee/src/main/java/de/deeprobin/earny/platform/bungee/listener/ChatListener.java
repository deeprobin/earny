package de.deeprobin.earny.platform.bungee.listener;

import de.deeprobin.earny.platform.bungee.EarnyPlugin;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {

    private final EarnyPlugin plugin;

    public ChatListener(EarnyPlugin plugin){
        this.plugin = plugin;
        this.plugin.getLogger().info("Initialized chat listener.");
    }

    @EventHandler
    public void onChat(ChatEvent event){
        event.setMessage(this.plugin.getFactory().replaceChatMessage(event.getMessage()));
    }

}
