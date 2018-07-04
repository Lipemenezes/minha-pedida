package com.felipe.minhapedida.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.felipe.minhapedida.MyORMLiteHelper;
import com.felipe.minhapedida.models.Item;
import com.felipe.minhapedida.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    ListView lvItems;
    List<Item> listItems;
    TextView tvTotal;
    ArrayAdapter<Item> adapterItems;
    Item item;
    MyORMLiteHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        lvItems = findViewById(R.id.lvItems);
        tvTotal = findViewById(R.id.tvTotal);
        db = MyORMLiteHelper.getInstance(this);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int i, final long l) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Adicionar item");

                builder.setMessage("Deseja adicionar mais um item a comanda?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Item it = (Item) adapterView.getItemAtPosition(i);
                        for (Item item : listItems) {
                            if (item.getId() == it.getId()) {
                                item.setQuantity(item.getQuantity() + 1);
                                item.setValue(item.getProduct().getValue() * item.getQuantity());
                                try {
                                    db.getItemDao().update(item);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                updateListAndTotal();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                builder.show();
            }
        });

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, final long l) {
                Toast.makeText(MainActivity.this, "Clique longo", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Excluir item");

                builder.setMessage("Deseja excluir este item?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Item it = (Item) adapterView.getItemAtPosition(i);
                        for (Item item : listItems) {
                            if (item.getId() == it.getId()) {
                                listItems.remove(item);
                                try {
                                    db.getItemDao().delete(item);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                updateListAndTotal();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                builder.show();
                return false;
            }
        });
    }

    private void updateListAndTotal() {
        try {
            List<Item> items = db.getItemDao().queryForAll();
            if (items.size() > 0) {
                listItems = items;

                adapterItems = new ArrayAdapter<Item>(
                        this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        listItems
                );
                lvItems.setAdapter(adapterItems);
            } else {
                listItems = new ArrayList<Item>();
                adapterItems = new ArrayAdapter<Item>(
                        this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        listItems
                );
                lvItems.setAdapter(adapterItems);
            }
            updateTotal();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateListAndTotal();
    }

    private void updateTotal() {
        double total = 0;

        if (listItems.size() > 0) {
            for (Item item : listItems) {
                total += item.getValue();
            }
        }
        tvTotal.setText("Total: R$" + total);
    }

    public void addProduct(View v){
        Intent it = new Intent(this, AddProductActivity.class);
        startActivity(it);
    }

    public void clearList(View v) {
        try {
            db.getItemDao().delete(listItems);
        }  catch (SQLException e) {
            e.printStackTrace();
        }
        listItems = new ArrayList<Item>();
        adapterItems = new ArrayAdapter<Item>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                listItems
        );
        lvItems.setAdapter(adapterItems);
        updateTotal();
    }


    public void addProductQRCode(View v){
        //Desenvolver
        //Você deve escnaer um QRCode, neste QRCode deverá existir o código de um product cadastrado.
        //Após escnaner o código do product, você deve adioná-lo à comanda com quantidade=1
    }
}
