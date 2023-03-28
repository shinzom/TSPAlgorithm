package com.cy.demo1.entity;

import java.io.Serializable;
import java.util.Objects;

public class Result_  extends BaseEntity implements Serializable {
    private int resultId;
    private long time;
    private double distance;
    private String path;

    public int getResult_id() {
        return resultId;
    }

    public void setResult_id(int result_id) {
        this.resultId = result_id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result_ result_ = (Result_) o;
        return resultId == result_.resultId && time == result_.time && Double.compare(result_.distance, distance) == 0 && path.equals(result_.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultId, time, distance, path);
    }

    @Override
    public String toString() {
        return "Result_{" +
                "result_id=" + resultId +
                ", time=" + time +
                ", distance=" + distance +
                ", path='" + path + '\'' +
                '}';
    }
}
