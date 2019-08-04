package de.deeprobin.earny.platform.nukkit.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.scheduler.AsyncTask;
import de.deeprobin.earny.exception.ShorteningException;
import de.deeprobin.earny.platform.nukkit.EarnyPlugin;
import de.deeprobin.earny.shorteners.IShortener;

public class ShortUrlCommand extends Command {
    private final EarnyPlugin plugin;

    public ShortUrlCommand(final EarnyPlugin plugin) {
        super("short-url", "Shorts a url", "/short-url <shortener> <link>", new String[]{ "earny-short" });
        this.plugin = plugin;
        this.plugin.getLogger().info("Initialized short-url command");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length == 2) {
            String shortenerString = args[0];
            String url = args[1];

            IShortener shortener = this.plugin.getFactory().getShortenerManager().getShortenerByName(shortenerString, false);

            if (shortener == null) {
                sender.sendMessage(String.format("§cShortener %s is not available.", shortenerString.toUpperCase()));
                return true;
            }

            sender.sendMessage("§aPlease wait. Generating shortened link...");

            this.plugin.getServer().getScheduler().scheduleAsyncTask(this.plugin, new AsyncTask() {
                @Override
                public void onRun() {
                    try {
                        String shortUrl = shortener.shortUrl(url);
                        sender.sendMessage("§6Short URL: §e" + shortUrl);

                    } catch (ShorteningException e) {
                        String haste = plugin.getFactory().getErrorReportUtil().getErrorReport(e);
                        if(sender.hasPermission("earny.admin")) {
                            sender.sendMessage("§cCannot short url. Please check the api key for this service. Stack Trace: " + plugin.getFactory().getErrorReportUtil().getErrorReport(e));
                        } else {
                            sender.sendMessage("§cSorry, an error occurred. Please contact an admin of this server.");
                        }
                        plugin.getLogger().warning(String.format("%s tried to short %s with the shortener %s. But there occurred an error. Please check your api keys or report this stacktrace: %s", sender.getName(), shortenerString, url, haste));
                    }
                }
            });


        } else {
            sender.sendMessage("§6--- EARNY - SHORT-URL ---");
            sender.sendMessage("\n");
            sender.sendMessage("§cSyntax: /short-url <shortener> <url>");
            sender.sendMessage("\n");
            sender.sendMessage("§6Available shorteners: ");
            for (IShortener s : this.plugin.getFactory().getShortenerManager().getShorteners()) {
                sender.sendMessage("§6* §e" + s.getIdentifiers()[0]);
            }
        }
        return true;
    }
}
