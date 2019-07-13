package de.deeprobin.earny.platform.sponge.listener;

import de.deeprobin.earny.exception.ShorteningException;
import de.deeprobin.earny.platform.sponge.EarnyPlugin;
import de.deeprobin.earny.shorteners.IShortener;
import lombok.RequiredArgsConstructor;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.message.MessageEvent;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ChatListener {

    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(http://|https://)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}\\.([a-z]+)?$",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    private final EarnyPlugin plugin;

    public ChatListener(final EarnyPlugin plugin){
        this.plugin = plugin;
        this.plugin.getLogger().info("Initialized chat listener.");
    }


    @Listener
    public void onChat(MessageEvent event){
        if(this.plugin.getFactory().getConfiguration().replaceChatLinks) {
            return;
        }

        IShortener shortener = null;

        for(IShortener s : this.plugin.getFactory().getShortenerManager().getShorteners()) {
            for(String identifier : s.getIdentifiers()){
                if(identifier.equalsIgnoreCase(this.plugin.getFactory().getConfiguration().replaceChatLinksWith)){
                    shortener = s;
                    break;
                }
            }
        }

        if(shortener == null){
            this.plugin.getLogger().warn(String.format("Cannot replace chat links, because we didn't find the shortener %s (Please correct your configuration file and reload).", this.plugin.getFactory().getConfiguration().replaceChatLinksWith.toUpperCase()));
            return;
        }

        String message = event.getMessage().toPlain();
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
                this.plugin.getLogger().warn(String.format("Cannot short url %s. Please check your api key before you report the stack trace(%s).", url, this.plugin.getFactory().getErrorReportUtil().getErrorReport(e)));
            }
        }

        event.setMessage(Text.of(message));
    }
}
