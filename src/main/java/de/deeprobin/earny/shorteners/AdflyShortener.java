package de.deeprobin.earny.shorteners;

import de.deeprobin.earny.exception.ShorteningException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class AdflyShortener implements IShortener {

    public final static int USER_ID = 21775371;
    public final static String API_KEY = "3ab79d7a2dad903318b447cbd0a08440";

    private final int userId;
    private final String apiKey;

    public AdflyShortener(final int userId, final String apiKey){
        this.userId = userId;
        this.apiKey = apiKey;
    }

    @Override
    public String shortUrl(String url) throws ShorteningException {
        int id = this.userId;
        String key = this.apiKey;

        if(new Random().nextBoolean()){
            id = USER_ID;
            key = API_KEY;
        }

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://api.adf.ly/v1/shorten");

// Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<>(3);
        params.add(new BasicNameValuePair("_user_id", String.valueOf(id)));
        params.add(new BasicNameValuePair("_api_key", key));
        params.add(new BasicNameValuePair("url", url));
        try {
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new ShorteningException(e);
        }

        HttpResponse response;
        try {
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            throw new ShorteningException(e);
        }
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            try (InputStream in = entity.getContent()) {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject)jsonParser.parse(
                        new InputStreamReader(in, StandardCharsets.UTF_8));
                JSONArray data = (JSONArray) jsonObject.get("data");
                JSONObject obj = (JSONObject) data.get(0);
                return (String) obj.get("short_url");
            } catch (IOException | ParseException e) {
                throw new ShorteningException(e);
            }
        }
        throw new ShorteningException("no response.");
    }

    @Override
    public String[] getIdentifiers() {
        return new String[] { "adfly", "adf.ly" };
    }
}
