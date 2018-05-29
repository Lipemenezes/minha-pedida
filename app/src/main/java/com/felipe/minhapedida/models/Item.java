package com.felipe.minhapedida.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable
public class Item implements Serializable {
    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private Integer id;

    @DatabaseField(canBeNull = false)
    private double value;

    @DatabaseField(canBeNull = false)
    private int quantity;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Product product;

    public Item() {
    }

    public Item(double value, int quantity, Product product) {
        this.value = value;
        this.quantity = quantity;
        this.product = product;
    }

    public Item(Integer id, double value, int quantity, Product product) {
        this.id = id;
        this.value = value;
        this.quantity = quantity;
        this.product = product;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product produto) {
        this.product = produto;
    }

    public double getSubtotal(){
        return quantity * value;
    }
    @Override
    public String toString() {
        return getProduct().getName() + " - " + value + " - " + quantity + " - " + getSubtotal();
    }
}
