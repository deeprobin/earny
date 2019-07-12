package de.deeprobin.earny.platform.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

@Plugin(id = "earny", name = "Earny", version = "@VERSION@",
        description = "Earn money with minecraft!", authors = {"DeepRobin"})
public class EarnyPlugin {
    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public EarnyPlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;

        // TODO: register commands
        // TODO: register chat listener
        // TODO
    }
}
