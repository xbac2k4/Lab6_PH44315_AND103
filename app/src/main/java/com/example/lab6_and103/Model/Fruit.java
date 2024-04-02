package com.example.lab6_and103.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Fruit implements Serializable {
    private String _id, name , quantity, status, price;
    private ArrayList<String> image;
    private String description;
    @SerializedName("id_distributor")
    Distributor distributor;

    public Fruit() {
    }

    public Fruit(String _id, String name, String quantity, String status, String price, ArrayList<String> image, String description, Distributor distributor) {
        this._id = _id;
        this.name = name;
        this.quantity = quantity;
        this.status = status;
        this.price = price;
        this.image = image;
        this.description = description;
        this.distributor = distributor;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }
}
