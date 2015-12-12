package com.task.couponduniyatask.model;

/**
 * Created by getnow on 11/12/15.
 */
public class RestaurantPojo {
    private String OutletName;
    private String LogoURL;
    private String NumCoupons;
    private String NeighbourhoodName;
    private String cateGories;
    private double Latitude;
    private double Longitude;
    private float distance;

    public RestaurantPojo(String outletName, String logoURL, String numCoupons, String neighbourhoodName, double latitude, double longitude) {
        OutletName = outletName;
        LogoURL = logoURL;
        NumCoupons = numCoupons;
        NeighbourhoodName = neighbourhoodName;
        Latitude = latitude;
        Longitude = longitude;
    }


    public String getOutletName() {
        return OutletName;
    }

    public void setOutletName(String outletName) {
        OutletName = outletName;
    }

    public String getLogoURL() {
        return LogoURL;
    }

    public void setLogoURL(String logoURL) {
        LogoURL = logoURL;
    }

    public String getNumCoupons() {
        return NumCoupons;
    }

    public void setNumCoupons(String numCoupons) {
        NumCoupons = numCoupons;
    }

    public String getNeighbourhoodName() {
        return NeighbourhoodName;
    }

    public void setNeighbourhoodName(String neighbourhoodName) {
        NeighbourhoodName = neighbourhoodName;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getCateGories() {
        return cateGories;
    }

    public void setCateGories(String cateGories) {
        this.cateGories = cateGories;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
