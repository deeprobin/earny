package de.deeprobin.earny.util;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

@RequiredArgsConstructor
public class ErrorReportUtil {

    private final String version;
    private final String gameVersion;

    public String getErrorReport(Throwable throwable) {
        StringBuilder builder = new StringBuilder("--- EARNY PLUGIN - ERROR REPORT ---\n\n\n");
        builder.append("Created: ").append(System.currentTimeMillis()).append("\n");
        builder.append("Version: ").append(version).append("\n");
        builder.append("Game Version: ").append(gameVersion);
        for (Map.Entry entry : System.getProperties().entrySet()) {
            builder.append("Prop > ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        builder.append(" --- \n\n");
        builder.append(this.getStackTrace(throwable));
        try {
            return "https://hasteb.in/" + HastebinUtil.newHaste(builder.toString());
        } catch (IOException e) {
            return "-> Cannot create stacktrace report <-";
        }
    }

    private String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

}
