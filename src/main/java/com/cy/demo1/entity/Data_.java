package com.cy.demo1.entity;

import java.io.Serializable;
import java.util.Objects;

public class Data_ extends BaseEntity implements Serializable {
    private int resultId;
    private int number;
    private String x;
    private String y;

    public int getResult_id() {
        return resultId;
    }

    public void setResult_id(int result_id) {
        this.resultId = result_id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data_ data_ = (Data_) o;
        return resultId == data_.resultId && number == data_.number && x.equals(data_.x) && y.equals(data_.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultId, number, x, y);
    }

    @Override
    public String toString() {
        return "Data_{" +
                "result_id=" + resultId +
                ", number=" + number +
                ", x='" + x + '\'' +
                ", y='" + y + '\'' +
                '}';
    }
}
