package de.deeprobin.earny.platform.bungee.command;

import de.deeprobin.earny.exception.ShorteningException;
import de.deeprobin.earny.platform.bungee.EarnyPlugin;
import de.deeprobin.earny.shorteners.IShortener;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.plugin.Command;

public class ShortUrlCommand extends Command {

    private final EarnyPlugin plugin;

    public ShortUrlCommand(EarnyPlugin plugin) {
        super("short-url", "earny.command.short-url", "shortUrl", "earny-short");
        this.plugin = plugin;
        this.plugin.getLogger().info("Initialized short url command.");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 2) {
            String shortenerString = args[0];
            String url = args[1];

            IShortener shortener = null;
            for (IShortener s : this.plugin.getFactory().getShortenerManager().getShorteners()) {
                for (String identifier : s.getIdentifiers()) {
                    if (identifier.equalsIgnoreCase(shortenerString)) {
                        shortener = s;
                        break;
                    }
                }
            }

            if (shortener == null) {
                sender.sendMessage(new TextComponent(ChatColor.RED + String.format("Shortener %s is not available.", shortenerString.toUpperCase())));
                return;
            }

            sender.sendMessage(new TextComponent(ChatColor.GREEN + "Please wait. Generating shortened link..."));

            IShortener finalShortener = shortener;
            this.plugin.getProxy().getScheduler().runAsync(this.plugin, () -> {
                try {
                    String shortUrl = finalShortener.shortUrl(url);
                    ComponentBuilder builder = new ComponentBuilder("Short URL: ");
                    builder.color(ChatColor.GOLD);
                    builder.append(shortUrl);
                    builder.color(ChatColor.YELLOW);
                    builder.event(new ClickEvent(ClickEvent.Action.OPEN_URL, shortUrl));
                    builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("Click to open url.")}));
                    sender.sendMessage(builder.create());
                } catch (ShorteningException e) {
                    sender.sendMessage(new TextComponent("§cCannot short url. Please check the api key for this service. Stack Trace: " + this.plugin.getFactory().getErrorReportUtil().getErrorReport(e)));
                }
            });


        } else {
            ComponentBuilder builder = new ComponentBuilder("--- EARNY - SHORT-URL ---");
            builder.color(ChatColor.GOLD);
            builder.append("\n");
            builder.append("Syntax: /short-url <shortener> <url>");
            builder.color(ChatColor.RED);
            builder.append("\nAvailable shorteners: ").color(ChatColor.GOLD);
            for (IShortener s : this.plugin.getFactory().getShortenerManager().getShorteners()) {
                builder.append("* ").color(ChatColor.GOLD);
                builder.append(s.getIdentifiers()[0]).color(ChatColor.YELLOW);
            }
        }
    }
}
