package de.deeprobin.earny.platform.velocity.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import de.deeprobin.earny.platform.velocity.EarnyPlugin;

public class ChatListener {

    private final EarnyPlugin plugin;

    public ChatListener(final EarnyPlugin plugin){
        this.plugin = plugin;
        this.plugin.getLogger().info("Initialized chat listener.");
    }

    @Subscribe(order = PostOrder.NORMAL)
    public void onChat(PlayerChatEvent event) {
        event.setResult(PlayerChatEvent.ChatResult.message(this.plugin.getFactory().replaceChatMessage(event.getMessage())));
    }

}
