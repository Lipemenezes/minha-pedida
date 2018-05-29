package com.felipe.minhapedida;

/**
 * Created by Felipe on 26/05/2018.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.felipe.minhapedida.models.Category;
import com.felipe.minhapedida.models.Item;
import com.felipe.minhapedida.models.Product;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class MyORMLiteHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "minhapedida.db";
    private static final int DATABASE_VERSION = 1;
    private static MyORMLiteHelper mInstance = null;

    Dao<Category, Integer> daoCategory;
    Dao<Product, Integer> daoProduct;
    Dao<Item, Integer> daoItem;

    public MyORMLiteHelper(Context c){
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public static MyORMLiteHelper getInstance(Context ctx){
        if(mInstance == null){
            mInstance =  new MyORMLiteHelper(ctx);
        }
        return mInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Category.class);
            TableUtils.createTable(connectionSource, Product.class);
            TableUtils.createTable(connectionSource, Item.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, Category.class, true);
            TableUtils.dropTable(connectionSource, Product.class, true);
            TableUtils.dropTable(connectionSource, Item.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(sqLiteDatabase, connectionSource);
    }


    public Dao<Category, Integer> getCategoryDao() throws SQLException {
        if(daoCategory==null){
            daoCategory = getDao(Category.class);
        }
        return daoCategory;
    }

    public Dao<Product, Integer> getProductDao() throws SQLException {
        if(daoProduct==null){
            daoProduct = getDao(Product.class);
        }
        return daoProduct;
    }

    public Dao<Item, Integer> getItemDao() throws SQLException {
        if(daoItem==null){
            daoItem = getDao(Item.class);
        }
        return daoItem;
    }
}
