package com.felipe.minhapedida.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.felipe.minhapedida.MyORMLiteHelper;
import com.felipe.minhapedida.R;
import com.felipe.minhapedida.models.Category;
import com.felipe.minhapedida.models.Product;

import java.sql.SQLException;
import java.util.ArrayList;

public class ProductManagementActivity extends Activity {

    EditText editName, editValue;
    ListView lvProducts;
    Spinner spCategories;

    ArrayList<Product> listProducts;
    ArrayList<Category> listCategories;

    ArrayAdapter<Product> adapterProduct;
    ArrayAdapter<Category> adapterCategory;

    MyORMLiteHelper db;
    Product product = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);
        db = MyORMLiteHelper.getInstance(this);

        editName = findViewById(R.id.editNome);
        editValue = findViewById(R.id.editValor);
        spCategories = findViewById(R.id.spCategories);
        lvProducts = findViewById(R.id.lvProducts);

        //Montagem do spinner e listView
        try {
            loadSpinner();
            loadListView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void loadListView() throws SQLException {
        listProducts = (ArrayList<Product>) db.getProductDao().queryForAll();
        adapterProduct = new ArrayAdapter<Product>(
                this,
                android.R.layout.simple_list_item_1,
                listProducts
        );
        lvProducts.setAdapter(adapterProduct);

        //Inicio clique curto - Editar
        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                product = adapterProduct.getItem(position);
                AlertDialog.Builder alerta = new AlertDialog.Builder(
                        ProductManagementActivity.this
                );
                alerta.setTitle("Visualizando product");
                alerta.setMessage(product.toString());
                alerta.setNeutralButton("Fechar", null);
                alerta.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editName.setText(product.getName());
                        editValue.setText(String.valueOf(product.getValue()));
                        for (int pos=0; pos<adapterCategory.getCount(); pos++) {
                            Category c = adapterCategory.getItem(pos);
                            if (c.getId()== product.getCategory().getId()) {
                                spCategories.setSelection(pos);
                                break;
                            }
                        }
                    }
                });
                alerta.show();
            }
        });
        //Fim clique curto - Editar

        //Inicio clique longo
        lvProducts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                product = adapterProduct.getItem(position);
                AlertDialog.Builder alerta = new AlertDialog.Builder(ProductManagementActivity.this);
                alerta.setTitle("Excluindo product");
                alerta.setMessage(product.toString());
                alerta.setNeutralButton("Cancelar", null);
                alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            db.getProductDao().delete(product);
                            adapterProduct.remove(product);
                            product = null;
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                alerta.show();
                return true;
            }
        });
    }

    public void loadSpinner() throws SQLException {
        listCategories = (ArrayList<Category>) db.getCategoryDao().queryForAll();

        adapterCategory = new ArrayAdapter<Category>(
                this,
                android.R.layout.simple_list_item_1,
                listCategories
        );
        spCategories.setAdapter(adapterCategory);
    }


    public void save(View v) throws SQLException {
        if (product ==null) {
            product = new Product();
            product.setName(editName.getText().toString());
            product.setValue(Double.parseDouble(editValue.getText().toString()));
            product.setCategory((Category) spCategories.getSelectedItem());
            long res = db.getProductDao().create(product);
            if (res != -1) {
                adapterProduct.add(product);
                Toast.makeText(this, "Cadastrado"+res, Toast.LENGTH_SHORT).show();
            }
        } else {
            product.setName(editName.getText().toString());
            product.setValue(Double.parseDouble(editValue.getText().toString()));
            product.setCategory((Category) spCategories.getSelectedItem());
            int res = db.getProductDao().update(product);
            if(res != -1){
                adapterProduct.notifyDataSetChanged();
                Toast.makeText(this, "Editado"+res, Toast.LENGTH_SHORT).show();
            }
        }
        product = null;
        editName.setText("");
        editValue.setText("");
        spCategories.setSelection(0);
        editName.requestFocus();
        Intent intent = new Intent();
        intent.putExtra("hasChanges", true);
        setResult(1010, intent);
    }


    public void manageCategory(View v){
        Intent it = new Intent(this, CategoryManagementActivity.class);
        startActivityForResult(it, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000 &&
                resultCode == 2020 &&
                data.getBooleanExtra("hasChanges", false)) {
            try {
                loadSpinner();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            loadSpinner();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
