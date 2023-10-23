package com.finalprojectbillingapp.user;

public enum Country {
    AUSTRIA ("Austria"),
    BELGIUM ("Belgium"),
    BULGARIA ("Bulgaria"),
    CROATIA ("Croatia"),
    CYPRUS ("Cyprus"),
    CZECHIA ("Czech Republic"),
    DENMARK ("Denmark"),
    ESTONIA ("Estonia"),
    FINLAND ("Finland"),
    FRANCE ("France"),
    GERMANY ("Germany"),
    GREECE ("Greece"),
    HUNGARY ("Hungary"),
    ICELAND ("Iceland"),
    IRELAND ("Ireland"),
    ITALY ("Italy"),
    LATVIA ("Latvia"),
    LIECHTENSTEIN ("Liechtenstein"),
    LITHUANIA ("Lithuania"),
    LUXEMBOURG ("Luxembourg"),
    MALTA ("Malta"),
    NETHERLANDS ("Netherlands"),
    NORWAY ("Norway"),
    POLAND ("Poland"),
    PORTUGAL ("Portugal"),
    ROMANIA ("Romania"),
    SLOVAKIA ("Slovakia"),
    SLOVENIA ("Slovenia"),
    SPAIN ("Spain"),
    SWEDEN ("Sweden");

    private final String displayCountryName;
    Country(String displayCountryName){
        this.displayCountryName = displayCountryName;
    }

    public String getDisplayCountryName () {
        return displayCountryName;
    }
}