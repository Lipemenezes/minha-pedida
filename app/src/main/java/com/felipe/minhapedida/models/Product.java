package com.felipe.minhapedida.models;

/**
 * Created by Felipe on 26/05/2018.
 */
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;

@DatabaseTable(tableName = "produtos")
public class Product implements Serializable {
    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private Integer id;

    @DatabaseField(columnName = "name", canBeNull = false, width = 150)
    private String name;

    @DatabaseField
    private double value;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Category category;

    public Product() {
    }

    public Product(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public Product(Integer id, String name, double value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public Product(String name, double value, Category category) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if (category == null)
            category = new Category();
        return category.getName()+"\n"+id + ": " + name + " - R$" + value;
    }
}