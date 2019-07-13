package de.deeprobin.earny.logging;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@RequiredArgsConstructor
public class Slf4JLoggerImplementation implements EarnyLogger {

    private @NonNull final Logger logger;

    @Override
    public void info(String string) {
        this.logger.info(string);
    }

    @Override
    public void warn(String string) {
        this.logger.warn(string);
    }

    @Override
    public void error(String string) {
        this.logger.error(string);
    }
}
