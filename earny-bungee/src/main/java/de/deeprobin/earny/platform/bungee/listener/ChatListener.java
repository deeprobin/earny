package de.deeprobin.earny.platform.bungee.listener;

import de.deeprobin.earny.exception.ShorteningException;
import de.deeprobin.earny.platform.bungee.EarnyPlugin;
import de.deeprobin.earny.shorteners.IShortener;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {

    private final EarnyPlugin plugin;

    public ChatListener(EarnyPlugin plugin){
        this.plugin = plugin;
        this.plugin.getLogger().info("Initialized chat listener.");
    }

    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(http://|https://)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}\\.([a-z]+)?$",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    @EventHandler
    public void onChat(ChatEvent event){
        if(!this.plugin.getConfiguration().replaceChatLinks) {
            return;
        }

        IShortener shortener = null;

        for(IShortener s : this.plugin.getShortenerManager().getShorteners()) {
            for(String identifier : s.getIdentifiers()){
                if(identifier.equalsIgnoreCase(this.plugin.getConfiguration().replaceChatLinksWith)){
                    shortener = s;
                    break;
                }
            }
        }

        if(shortener == null){
            this.plugin.getLogger().warning(String.format("Cannot replace chat links, because we didn't find the shortener %s (Please correct your configuration file and reload).", this.plugin.getConfiguration().replaceChatLinksWith.toUpperCase()));
            return;
        }

        String message = event.getMessage();
        Matcher matcher = URL_PATTERN.matcher(message);
        List<String> urls = new ArrayList<>();
        while (matcher.find()) {
            int matchStart = matcher.start(1);
            int matchEnd = matcher.end();
            urls.add(message.substring(matchStart, matchEnd));
        }

        for(String url : urls){
            try {
                message = message.replace(url, shortener.shortUrl(url));
            } catch (ShorteningException e) {
                this.plugin.getLogger().warning(String.format("Cannot short url %s. Please check your api key before you report the stack trace(%s).", url, this.plugin.getErrorReportUtil().getErrorReport(e)));
            }
        }

        event.setMessage(message);
    }

}
