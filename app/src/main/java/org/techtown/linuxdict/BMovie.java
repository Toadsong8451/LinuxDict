package org.techtown.linuxdict;


import com.google.gson.annotations.SerializedName;

public class BMovie {
    @SerializedName("_source")
    private Movie movie;

    public Movie getMovie() {
        return movie;
    }
}