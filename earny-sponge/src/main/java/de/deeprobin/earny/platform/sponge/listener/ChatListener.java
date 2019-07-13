package de.deeprobin.earny.platform.sponge.listener;

import de.deeprobin.earny.platform.sponge.EarnyPlugin;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.message.MessageEvent;
import org.spongepowered.api.text.Text;

public final class ChatListener {

    private final EarnyPlugin plugin;

    public ChatListener(final EarnyPlugin plugin){
        this.plugin = plugin;
        this.plugin.getLogger().info("Initialized chat listener.");
    }


    @Listener
    public void onChat(MessageEvent event){
        event.setMessage(Text.of(this.plugin.getFactory().replaceChatMessage(event.getMessage().toPlain())));
    }
}
