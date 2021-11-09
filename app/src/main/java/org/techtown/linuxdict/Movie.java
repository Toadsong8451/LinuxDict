package org.techtown.linuxdict;

import com.google.gson.annotations.SerializedName;

public class Movie {
    private String title;
    private String release_date;
    private String image_url;
    private String plot;

    public Movie(String title, String image_url, String plot, String release_date) {
        this.title = title;
        this.plot = plot;
        this.image_url = image_url;
        this.release_date = release_date;
    }

    public String getTitle() {
        return title;
    }

    public String getPlot() {
        return plot;
    }

    public String getPoster_path() {
        return image_url;
    }


    public String getRelease_date() {

        if(release_date != null) return release_date;
        else return "Unknown...";
    }
}
