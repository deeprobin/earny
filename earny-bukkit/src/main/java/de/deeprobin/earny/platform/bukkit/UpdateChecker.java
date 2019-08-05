package de.deeprobin.earny.platform.bukkit;

import de.deeprobin.earny.exception.ShorteningException;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.apache.commons.codec.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

final class UpdateChecker {

    private final EarnyPlugin plugin;

    public UpdateChecker(final EarnyPlugin plugin){
        this.plugin = plugin;
        this.plugin.getLogger().info("Initialized update checker");
    }

    void startScheduler(){
        this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
            try {
                if(this.isUpdateAvailable()) {
                    this.plugin.getLogger().info("An update is available. Please update this plugin. Visit https://www.spigotmc.org/resources/69093/");
                    for(Player all : Bukkit.getOnlinePlayers()){
                        if(all.hasPermission("earny.admin")) {
                            ComponentBuilder builder = new ComponentBuilder("[Earny] ");
                            builder.color(ChatColor.GOLD);
                            builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("by DeepRobin").color(ChatColor.YELLOW).create()));
                            builder.append("An update is available Please update this plugin ").color(ChatColor.YELLOW);
                            builder.append("here").color(ChatColor.YELLOW).underlined(true).event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/69093/"));
                            builder.append(".").color(ChatColor.YELLOW);
                            all.spigot().sendMessage(builder.create());
                        }
                    }
                }
            } catch (IOException | URISyntaxException ignored) {}
        }, 0, 15 * 60 * 1000);
    }

    private boolean isUpdateAvailable() throws IOException, URISyntaxException {
        return !this.plugin.getDescription().getVersion().trim().equals(getNewestVersion().trim());
    }

    private String getNewestVersion() throws IOException, URISyntaxException {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost("api.spigotmc.org").setPath("/legacy/update.php")
                .setParameter("resource", "69093");
        builder.setCharset(Charsets.UTF_8);
            HttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(builder.build());
            HttpResponse response;
            try {
                response = httpclient.execute(httpGet);
            } catch (IOException e) {
                this.plugin.getLogger().warning("Cannot check updates - Is api.spigotmc.org down?");
                throw e;
            }
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } else {
                throw new IOException("HTTP entity is null");
            }
    }

}
