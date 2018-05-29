package com.felipe.minhapedida.models;

/**
 * Created by Felipe on 26/05/2018.
 */
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DatabaseTable
public class Category implements Serializable {

    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private Integer id;

    @DatabaseField
    private String name;

    @ForeignCollectionField(eager = true)
    private Collection<Product> productsList = new ArrayList<>();


    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    public List<Product> getProductList() {
        List<Product> list = new ArrayList<>();
        for (Product p : this.productsList) {
            list.add(p);
        }
        return list;
    }

    public Collection<Product> getProductsList() {
        return productsList;
    }

    public void setProductsList(Collection<Product> productsList) {
        this.productsList = productsList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (id != null ? !id.equals(category.id) : category.id != null) return false;
        return name != null ? name.equals(category.name) : category.name == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ID: " + id.toString() + " - Nome: " + name + " (" + getProductsList().size() + ") ";
    }

}
