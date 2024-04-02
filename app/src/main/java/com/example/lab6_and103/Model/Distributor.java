package com.example.lab6_and103.Model;

import com.google.gson.annotations.SerializedName;

public class Distributor {
    @SerializedName("_id")
    private String _id;
    private String title;

    public Distributor() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
