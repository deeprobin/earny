package de.deeprobin.earny.logging;

import com.sun.tools.sjavac.Log;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class JavaLoggerImplementation implements EarnyLogger {

    @NonNull private final Logger logger;

    @Override
    public void info(String string) {
        logger.info(string);
    }

    @Override
    public void warn(String string) {
        logger.warning(string);
    }

    @Override
    public void error(String string) {
        logger.log(Level.SEVERE, string);
    }
}
