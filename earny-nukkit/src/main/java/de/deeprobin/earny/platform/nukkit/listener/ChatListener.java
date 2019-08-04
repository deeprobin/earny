package de.deeprobin.earny.platform.nukkit.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import de.deeprobin.earny.platform.nukkit.EarnyPlugin;

public class ChatListener implements Listener {
    private final EarnyPlugin plugin;

    public ChatListener(final EarnyPlugin plugin){
        this.plugin = plugin;
        this.plugin.getLogger().info("Initialized chat listener.");
    }

    @EventHandler
    public void onChat(PlayerChatEvent event){
        event.setMessage(this.plugin.getFactory().replaceChatMessage(event.getMessage()));
    }
}
