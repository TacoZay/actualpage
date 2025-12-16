package com.autobots.model;

public class PizzaState {

    private static MenuItem editingItem;

    public static void setEditingItem(MenuItem item) {
        editingItem = item;
    }

    public static MenuItem getEditingItem() {
        return editingItem;
    }
}
