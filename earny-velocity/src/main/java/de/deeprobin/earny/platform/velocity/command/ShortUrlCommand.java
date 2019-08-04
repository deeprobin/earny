package de.deeprobin.earny.platform.velocity.command;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import de.deeprobin.earny.exception.ShorteningException;
import de.deeprobin.earny.platform.velocity.EarnyPlugin;
import de.deeprobin.earny.shorteners.IShortener;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ShortUrlCommand implements Command {

    private final EarnyPlugin plugin;

    public ShortUrlCommand(final EarnyPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getLogger().info("Initialized short-url command");
    }

    @Override
    public void execute(CommandSource source, @NonNull String[] args) {
        if (args.length == 2) {
            String shortenerString = args[0];
            String url = args[1];

            IShortener shortener = this.plugin.getFactory().getShortenerManager().getShortenerByName(shortenerString, false);

            if (shortener == null) {
                source.sendMessage(TextComponent.builder(String.format("Shortener %s is not available.", shortenerString.toUpperCase()), TextColor.RED).build());
                return;
            }


            source.sendMessage(TextComponent.builder("Please wait. Generating shortened link...", TextColor.GREEN).build());

            this.plugin.getServer().getScheduler().buildTask(this.plugin, () -> {
                try {

                    String shortUrl = shortener.shortUrl(url);
                    TextComponent.Builder builder = TextComponent.builder("Short URL: ");
                    builder.color(TextColor.GOLD);
                    builder.append(shortUrl);
                    builder.color(TextColor.YELLOW);
                    builder.clickEvent(ClickEvent.openUrl(shortUrl));
                    builder.hoverEvent(HoverEvent.showText(TextComponent.of("Click to open url.")));
                    source.sendMessage(builder.build());
                } catch (ShorteningException e) {
                    String haste = this.plugin.getFactory().getErrorReportUtil().getErrorReport(e);
                    if(source.hasPermission("earny.admin")) {
                        source.sendMessage(TextComponent.of("Cannot short url. Please check the api key for this service. Stack Trace: " + this.plugin.getFactory().getErrorReportUtil().getErrorReport(e)).color(TextColor.RED));
                    } else {
                        source.sendMessage(TextComponent.of("Sorry, an error occurred. Please contact an admin of this server.").color(TextColor.RED));
                    }
                    this.plugin.getLogger().warn(String.format("%s tried to short %s with the shortener %s. But there occurred an error. Please check your api keys or report this stacktrace: %s", "A command executor", shortenerString, url, haste));
                }
            }).schedule();


        } else {
            TextComponent.Builder builder = TextComponent.builder("--- EARNY - SHORT-URL ---");
            builder.color(TextColor.GOLD);
            builder.append("\n");
            builder.append("Syntax: /short-url <shortener> <url>");
            builder.color(TextColor.RED);
            builder.append("\nAvailable shorteners: ").color(TextColor.GOLD);
            for (IShortener s : this.plugin.getFactory().getShortenerManager().getShorteners()) {
                builder.append("* ").color(TextColor.GOLD);
                builder.append(s.getIdentifiers()[0]).color(TextColor.YELLOW);
            }
        }
    }
}
