package de.deeprobin.earny.manager;

import de.deeprobin.earny.logging.EarnyLogger;
import de.deeprobin.earny.shorteners.IShortener;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public final class ShortenerManager {

    private final EarnyLogger logger;

    private List<IShortener> shortenerList = new LinkedList<>();

    public ShortenerManager(final EarnyLogger logger) {
        this.logger = logger;
        this.logger.info("Shortener manager initialized.");
    }

    public Iterable<IShortener> getShorteners(){
        return shortenerList;
    }

    public void registerShortener(IShortener shortener){
        shortenerList.add(shortener);
        this.logger.info(String.format("Registered shortener for %s.", shortener.getIdentifiers()[0].toUpperCase()));
    }

}
