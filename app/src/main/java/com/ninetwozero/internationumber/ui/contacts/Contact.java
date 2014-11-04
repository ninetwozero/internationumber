package com.ninetwozero.internationumber.ui.contacts;

import android.net.Uri;

public class Contact {
    private final long id;
    private final String name;
    private final String number;
    private final Uri pathToImage;

    public Contact(long id, String name, String number, Uri pathToImage) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.pathToImage = pathToImage;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public Uri getPathToImage() {
        return pathToImage;
    }
}
