package com.felipe.minhapedida.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.felipe.minhapedida.MyORMLiteHelper;
import com.felipe.minhapedida.models.Item;
import com.felipe.minhapedida.R;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends Activity {
    public static final int REQUEST_ADD_PRODUCT = 10;
    ListView lvItems;
    ArrayList<Item> listItems;
    ArrayAdapter<Item> adapterItems;
    Item item;
    MyORMLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = findViewById(R.id.lvItems);
        db = MyORMLiteHelper.getInstance(this);

        adapterItems = new ArrayAdapter<Item>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                listItems
        );

        //Clique curto
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "Clique curto", Toast.LENGTH_SHORT).show();
                //Desenvolver
            }
        });

        //Clique longo
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "Clique longo", Toast.LENGTH_SHORT).show();
                //Desenvolver
                return false;
            }
        });
    }

    public void addProduct(View v){
        Intent it = new Intent(this, AddProductActivity.class);
        startActivityForResult(it, REQUEST_ADD_PRODUCT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode== REQUEST_ADD_PRODUCT){
            item = (Item) data.getSerializableExtra("selectedItem");

            int itemIndex = 0;
            for (int i = 0; i < listItems.size(); i++) {
                if (listItems.get(i).getId() == item.getId()) {
                    listItems.get(i).setQuantity(item.getQuantity());
                    itemIndex = i;
                    try {
                        db.getItemDao().update(listItems.get(i));
                    }  catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (itemIndex == 0) {
                try {
                    listItems.add(item);
                    db.getItemDao().create(item);
                }  catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            //Adicionar um item no listView e no db
            //Desenvolver
        } else if(resultCode ==  RESULT_CANCELED) {
            Toast.makeText(this, "Cancelou", Toast.LENGTH_SHORT).show();
        }
    }

    public void clearList(View v) {
        try {
            db.getItemDao().delete(listItems);
        }  catch (SQLException e) {
            e.printStackTrace();
        }
        listItems = new ArrayList<Item>();
    }


    public void addProductQRCode(View v){
        //Desenvolver
        //Você deve escnaer um QRCode, neste QRCode deverá existir o código de um product cadastrado.
        //Após escnaner o código do product, você deve adioná-lo à comanda com quantidade=1
    }
}
