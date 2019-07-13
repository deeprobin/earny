package de.deeprobin.earny.platform.nukkit.logging;

import cn.nukkit.plugin.PluginLogger;
import de.deeprobin.earny.logging.EarnyLogger;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NukkitLoggerImplementation implements EarnyLogger {

    @NonNull private final PluginLogger logger;

    @Override
    public void info(String string) {
        this.logger.info(string);
    }

    @Override
    public void warn(String string) {
        this.logger.warning(string);
    }

    @Override
    public void error(String string) {
        this.logger.error(string);
    }
}
