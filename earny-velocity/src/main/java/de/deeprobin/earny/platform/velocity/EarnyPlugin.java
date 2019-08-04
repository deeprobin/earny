package de.deeprobin.earny.platform.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import de.deeprobin.earny.IPlugin;
import de.deeprobin.earny.PluginFactory;
import de.deeprobin.earny.logging.Slf4JLoggerImplementation;
import de.deeprobin.earny.platform.velocity.command.ShortUrlCommand;
import de.deeprobin.earny.platform.velocity.listener.ChatListener;
import lombok.Getter;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "earny", name = "Earny", version = "@VERSION@",
        description = "Earn money with minecraft!", authors = {"DeepRobin"})
public class EarnyPlugin implements IPlugin {

    @Getter
    private final ProxyServer server;

    @Getter
    private final Logger logger;

    @Getter
    private PluginFactory factory;

    @Inject @DataDirectory
    private Path dataDirectory;

    @Inject
    public EarnyPlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
        this.factory = new PluginFactory(new Slf4JLoggerImplementation(this.getLogger()), this);
        this.factory.run();
        this.server.getCommandManager().register(new ShortUrlCommand(this), "short-url", "earny-short");
        this.server.getEventManager().register(this, new ChatListener(this));
        this.logger.info("Earny enabled.");
    }

    @Override
    public String getConfigDir() {
        return this.dataDirectory.toString();
    }

    @Override
    public String getServerVersion() {
        return this.server.getVersion().getVersion();
    }

    @Override
    public String getGameVersion() {
        return "Different by player";
    }
}
