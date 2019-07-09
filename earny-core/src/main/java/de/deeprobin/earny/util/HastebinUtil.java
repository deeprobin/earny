package de.deeprobin.earny.util;

import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HastebinUtil {

    public static String newHaste(String contents) throws IOException
    {
        HttpURLConnection connection = (HttpURLConnection)new URL("https://hasteb.in/documents").openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        connection.setRequestProperty("user-agent", "Java/HastebinAPI");

        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

        writer.write(contents);

        writer.flush();
        writer.close();

        JsonParser parser = new JsonParser();

        InputStreamReader reader = new InputStreamReader(connection.getInputStream());

        String key = parser.parse(reader).getAsJsonObject().get("key").getAsString();

        reader.close();

        return key;
    }

}
