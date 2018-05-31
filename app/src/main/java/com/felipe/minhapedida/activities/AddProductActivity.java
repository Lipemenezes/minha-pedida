package com.felipe.minhapedida.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.felipe.minhapedida.models.Category;
import com.felipe.minhapedida.models.Item;
import com.felipe.minhapedida.MyORMLiteHelper;
import com.felipe.minhapedida.models.Product;
import com.felipe.minhapedida.R;

import java.sql.SQLException;
import java.util.ArrayList;


public class AddProductActivity extends Activity {

    Spinner spProducts;
    NumberPicker npQuantity;
    ArrayList<Product> listProducts;
    ArrayAdapter<Product> adapterProducts;
    MyORMLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        db = MyORMLiteHelper.getInstance(this);

        spProducts = findViewById(R.id.spProducts);
        fillSpinner();

        npQuantity = findViewById(R.id.npQuantity);
        npQuantity.setMinValue(1);
        npQuantity.setMaxValue(99);
        npQuantity.setValue(1);
    }

    public void manageProduct(View v){
        Intent it = new Intent(this, ProductManagementActivity.class);
        startActivityForResult(it, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 &&
                resultCode == 1010 &&
                data.getBooleanExtra("hasChanges", false)) {
            fillSpinner();
        }
    }

    public void fillSpinner() {
        try {
            listProducts = (ArrayList<Product>) db.getProductDao().queryForAll();

            if (listProducts.size() == 0)
                setProducts();

            adapterProducts = new ArrayAdapter<Product>(this, android.R.layout.simple_spinner_item, listProducts);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        spProducts.setAdapter(adapterProducts);
    }

    public void setProducts() throws SQLException {
        Category ctFood = new Category("Comida");
        Category ctDrink = new Category("Bebida");
        db.getCategoryDao().create(ctFood);
        db.getCategoryDao().create(ctDrink);

        Product prod1 = new Product("Refrigerante", 3.50, ctDrink);
        Product prod2 = new Product("Cerveja", 5.00, ctDrink);
        Product prod3 = new Product("Batata Frita", 10.00, ctFood);
        Product prod4 = new Product("Água", 2.50, ctDrink);
        Product prod5 = new Product("Pastel", 3.50, ctFood);
        Product prod6 = new Product("Petiscos", 6.00, ctFood);

        db.getProductDao().create(prod1);
        db.getProductDao().create(prod2);
        db.getProductDao().create(prod3);
        db.getProductDao().create(prod4);
        db.getProductDao().create(prod5);
        db.getProductDao().create(prod6);
    }

    public void send(View v){
        Item item = new Item();
        //Desenvolver

        finish();
    }

}
