package de.deeprobin.earny.manager;

import de.deeprobin.earny.logging.EarnyLogger;
import de.deeprobin.earny.shorteners.IShortener;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;

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

    public IShortener getShortenerByName(@NonNull String name, final boolean caseSensitive){
        for(IShortener shortener : this.getShorteners()){
            for(String identifier : shortener.getIdentifiers()){
                if(!caseSensitive && identifier.equalsIgnoreCase(name)) {
                    return shortener;
                } else if(caseSensitive && identifier.equals(name)){
                    return shortener;
                }
            }
        }
        return null;
    }

}
