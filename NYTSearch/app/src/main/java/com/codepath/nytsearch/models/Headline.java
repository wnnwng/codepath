package com.codepath.nytsearch.models;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by winnie_wang on 8/10/16.
 */
@Parcel
public class Headline {
    public String main;

    public String getMain() { return main; }

    @ParcelConstructor
    public Headline(String main) {
        this.main = main;
    }
}
