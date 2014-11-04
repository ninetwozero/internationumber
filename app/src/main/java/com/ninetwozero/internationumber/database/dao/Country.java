package com.ninetwozero.internationumber.database.dao;

import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.Key;
import se.emilsjolander.sprinkles.annotations.Table;

@Table("Countries")
public class Country extends Model {
    public static final String TABLE_NAME = "Countries";
    public final static String DELIMITER = ":";

    @Key
    @Column("locale")
    private String locale;

    @Column("name")
    private String name;

    @Column("code")
    private int code;

    @Column("prefixes")
    private String prefixes;

    public Country() {}

    public String getLocale() {
        return locale;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public String getPrefixes() {
        return prefixes;
    }
}
