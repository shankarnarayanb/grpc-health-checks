package com.example;

import com.google.protobuf.Timestamp;
import roomservice.Roomservice;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Roomservice.Order order = Roomservice.Order.newBuilder()
                .setOrderTime(
                        Timestamp.newBuilder()
                                .setSeconds(System.currentTimeMillis()/1000).build())
                .setCost((float)19.42)
                .putMeals("Arthur",
                        Roomservice.Meal.newBuilder()
                                .setEntree(Roomservice.Entree.newBuilder()
                                        .setType("Cheeseburger")
                                        .addNotes("hold the onion")
                                        .build()
                                )
                                .setDrink(Roomservice.Drink.newBuilder()
                                        .setType("Coke")
                                        .setSize(Roomservice.Size.SIZE_MEDIUM)
                                        .build()
                                )
                                .addSides(Roomservice.Side.newBuilder()
                                        .setType("French fries")
                                        .setSize(Roomservice.Size.SIZE_LARGE)
                                        .build()
                                )
                                .build()
                )
                .build();

        System.out.println(order.toByteString());
        try {
            Roomservice.Order o2 = Roomservice.Order.parseFrom(order.toByteArray());
            System.out.println(o2.toString());
        } catch(Exception ignored) {}

    }
}