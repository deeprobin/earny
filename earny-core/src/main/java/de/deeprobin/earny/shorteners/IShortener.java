package de.deeprobin.earny.shorteners;

import de.deeprobin.earny.exception.ShorteningException;

public interface IShortener {
    String shortUrl(String url) throws ShorteningException;
    String[] getIdentifiers();
}
