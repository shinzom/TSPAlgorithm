package com.cy.demo1.algorithm;

public class Point {
    /**
     * 经度
     */
    private Double lng;
    /**
     * 纬度
     */
    private Double lat;

    public Point() {
    }

    ;

    public Point(Double lng, Double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    ;
}
