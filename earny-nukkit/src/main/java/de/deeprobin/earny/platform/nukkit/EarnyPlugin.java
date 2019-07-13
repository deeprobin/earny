package de.deeprobin.earny.platform.nukkit;

import cn.nukkit.plugin.PluginBase;
import de.deeprobin.earny.IPlugin;
import de.deeprobin.earny.PluginFactory;
import de.deeprobin.earny.platform.nukkit.logging.NukkitLoggerImplementation;
import lombok.Getter;

public class EarnyPlugin extends PluginBase implements IPlugin {

    @Getter
    private PluginFactory factory;

    @Override
    public void onEnable(){
        this.factory = new PluginFactory(new NukkitLoggerImplementation(this.getLogger()), this);
        this.factory.run();
        // TODO: register commands
        // TODO: register chat listener
    }

    @Override
    public String getConfigDir() {
        return this.getServer().getDataPath();
    }

    @Override
    public String getServerVersion() {
        return this.getServer().getNukkitVersion() + " - api: " + this.getServer().getApiVersion();
    }

    @Override
    public String getGameVersion() {
        return this.getServer().getVersion();
    }
}
