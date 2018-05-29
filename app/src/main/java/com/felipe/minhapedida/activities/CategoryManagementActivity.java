package com.felipe.minhapedida.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.felipe.minhapedida.models.Category;
import com.felipe.minhapedida.MyORMLiteHelper;
import com.felipe.minhapedida.R;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryManagementActivity extends Activity {
    EditText editName;
    ListView lvCategories;
    ArrayList<Category> listCategories;
    ArrayAdapter<Category> adapterCategories;
    Category category = null;

    MyORMLiteHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_management);
        db = MyORMLiteHelper.getInstance(this);
        editName = findViewById(R.id.editNome);
        lvCategories = findViewById(R.id.lvCategories);
        lvCategories.setOnItemClickListener(cliqueCurto());
        lvCategories.setOnItemLongClickListener(cliqueLongo());

        try {
            listCategories = (ArrayList<Category>) db.getCategoryDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        adapterCategories = new ArrayAdapter<Category>(
                this,
                android.R.layout.simple_list_item_1,
                listCategories
        );
        lvCategories.setAdapter(adapterCategories);

        db = MyORMLiteHelper.getInstance(this);
    }

    public AdapterView.OnItemClickListener cliqueCurto(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Toast.makeText(CategoryManagementActivity.this, "Clique curto", Toast.LENGTH_SHORT).show();
                category = adapterCategories.getItem(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(CategoryManagementActivity.this);
                alert.setTitle("Visualizando dados");
                alert.setIcon(android.R.drawable.ic_menu_view);
                alert.setMessage(category.toString());
                alert.setNeutralButton("Fechar", null);
                alert.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editName.setText(category.getName());
                        editName.requestFocus();
                    }
                });
                alert.show();
            }
        };
    }

    public AdapterView.OnItemLongClickListener cliqueLongo(){
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CategoryManagementActivity.this, "Clique longo", Toast.LENGTH_SHORT).show();
                category = adapterCategories.getItem(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(CategoryManagementActivity.this);
                alert.setTitle("Excluindo Category");
                alert.setIcon(android.R.drawable.ic_menu_delete);
                alert.setMessage("Deseja excluir a category "+ category.getName()+"?");
                alert.setNegativeButton("Não", null);
                alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapterCategories.remove(category);
                        try {
                            db.getCategoryDao().delete(category);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        category = null;
                    }
                });
                alert.show();
                return true;
            }
        };
    }

    public void save(View v) throws SQLException {
        if(category ==null)
            category = new Category();

        category.setName(editName.getText().toString());

        Dao.CreateOrUpdateStatus res = db.getCategoryDao().createOrUpdate(category);
        if(res.isCreated()){
            adapterCategories.add(category);
            Toast.makeText(this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();
        } else if(res.isUpdated()){
            adapterCategories.notifyDataSetChanged();
            Toast.makeText(this, "Atualizado com sucessos", Toast.LENGTH_SHORT).show();

        }
        category = null;
        editName.setText("");
    }

}
