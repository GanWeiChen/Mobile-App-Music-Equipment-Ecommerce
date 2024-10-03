
package com.example.shopping.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.example.shopping.Model.ItemsModel;
import com.example.shopping.Model.OrderModel;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;

public class TinyDB {
    private SharedPreferences preferences;
    public TinyDB(Context appContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    /**
     * Get parsed ArrayList of String from SharedPreferences at 'key'
     *
     * @param key SharedPreferences key
     * @return ArrayList of String
     */
    public ArrayList<String> getListString(String key) {
        return new ArrayList<>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }

    // Put methods
    public ArrayList<ItemsModel> getListObject(String key) {
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<ItemsModel> playerList = new ArrayList<>();

        for (String jObjString : objStrings) {
            ItemsModel player = gson.fromJson(jObjString, ItemsModel.class);
            playerList.add(player);
        }
        return playerList;
    }

    /**
     * Put ArrayList of String into SharedPreferences with 'key' and save
     *
     * @param key        SharedPreferences key
     * @param stringList ArrayList of String to be added
     */
    public void putListString(String key, ArrayList<String> stringList) {
        checkForNullKey(key);
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    public void putListObject(String key, ArrayList<ItemsModel> playerList) {
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for (ItemsModel player : playerList) {
            objStrings.add(gson.toJson(player));
        }
        putListString(key, objStrings);
    }

    /**
     * Remove SharedPreferences item with 'key'
     *
     * @param key SharedPreferences key
     */
    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }

    /**
     * null keys would corrupt the shared pref file and make them unreadable this is a preventive measure
     *
     * @param key the pref key to check
     */
    private void checkForNullKey(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
    }

    public void putListOrder(String key, ArrayList<OrderModel> orderList) {
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<>();
        for (OrderModel order : orderList) {
            objStrings.add(gson.toJson(order));
        }
        putListString(key, objStrings);
    }

    public ArrayList<OrderModel> getListOrder(String key) {
        Gson gson = new Gson();
        ArrayList<String> objStrings = getListString(key);
        ArrayList<OrderModel> orderList = new ArrayList<>();
        for (String jObjString : objStrings) {
            OrderModel order = gson.fromJson(jObjString, OrderModel.class);
            orderList.add(order);
        }
        return orderList;
    }

    public void clearOrderList(String key) {
        // Remove the order list associated with the key
        remove(key);
    }
}