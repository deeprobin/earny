package de.deeprobin.earny.platform.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

@Plugin(id = "earny", name = "Earny", version = "@VERSION@",
        description = "Earn money with minecraft!", authors = {"DeepRobin"})
public class EarnyPlugin {

    @Getter
    private final ProxyServer server;

    @Getter
    private final Logger logger;

    @Inject
    public EarnyPlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
        this.logger.warn("Sorry, we currently doesn't support velocity. But feel free to contribute.");
        // TODO: register commands
        // TODO: register chat listener
        // TODO
    }
}
