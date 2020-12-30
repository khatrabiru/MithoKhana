package com.khatribiru.mithokhana.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.khatribiru.mithokhana.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "MithoKhana.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"MenuId", "MenuImage", "MenuName", "Quantity", "Price", "Status", "Date"};
        String sqlTable = "OrderDetail";
        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);
        final List<Order> result = new ArrayList<>();
        if(c.moveToFirst()) {
            do {
                result.add(new Order(c.getString(c.getColumnIndex("MenuId")),
                        c.getString(c.getColumnIndex("MenuImage")),
                        c.getString(c.getColumnIndex("MenuName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Status")),
                        c.getString(c.getColumnIndex("Date")))
                );
            } while (c.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(MenuId,MenuImage,MenuName,Quantity,Price,Status,Date) VALUES('%s','%s','%s','%s','%s','%s','%s');",
                order.getMenuId(),
                order.getMenuImage(),
                order.getMenuName(),
                order.getQuantity(),
                order.getPrice(),
                order.getStatus(),
                order.getDate());
        db.execSQL(query);
    }

    public void removeItemFromCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE MenuId = '%s';",
                order.getMenuId());
        db.execSQL(query);
    }

    public void updateItemFromCart(String menuId, String Qty) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE OrderDetail SET Quantity = '%s' WHERE MenuId = '%s';",
                Qty,
                menuId);
        db.execSQL(query);
    }

    public void cleanCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);
    }
}
