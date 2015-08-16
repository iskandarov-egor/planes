package com.example.planes.Game.Models;

/**
 * Created by egor on 14.08.15.
 */
public class Player {
    private static int count = 1;
    private String name = "player" + String.valueOf(count);
    private int wins = 0;
    private Plane plane;

    public Player() {
        count++;
    }

    public void onWin() {
        wins++;
    }

    public int getWins() {
        return wins;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public Plane getPlane() {
        return plane;
    }
}
