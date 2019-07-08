package de.deeprobin.earny.config;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class Configuration implements Serializable {

    public int adFlyUserId;
    public String adFlyApiKey;

    public int adultXyzUserId;
    public String adultXyzKey;

    public String adFocusApiKey;

    public boolean replaceChatLinks;
    public String replaceChatLinksWith;

}
