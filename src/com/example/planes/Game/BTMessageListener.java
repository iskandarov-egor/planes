package com.example.planes.Game;

import com.example.planes.Communication.Message.ControlMessage;
import com.example.planes.Communication.Message.TurnMessage;
import com.example.planes.Communication.Message.Message;
import com.example.planes.Communication.Message.ReceivedMessage;
import com.example.planes.Communication.MessageListener;
import com.example.planes.Communication.RemoteAbonent;
import com.example.planes.Config.GameConfig;
import com.example.planes.Game.Models.Bullet;
import com.example.planes.Game.Models.Plane;
import com.example.planes.Game.Models.Player;
import com.example.planes.Interface.MyActivity;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by egor on 06.08.15.
 */
public class BTMessageListener implements MessageListener {
    private final Round game;
    private ArrayList<Player> players = null;

    public BTMessageListener(Round game, ArrayList<Player> players) {
        this.game = game;
        this.players = players;
    }

    ConcurrentLinkedQueue<ReceivedMessage> queue = new ConcurrentLinkedQueue<>();

    @Override
    public void onMessage(ReceivedMessage msg) {
        queue.add(msg);
    }

    @Override
    public void onDisconnected() {
        game.onDisconnected(); // todo debug this
    }

    public void processMessages() {
        ReceivedMessage msg = queue.poll();
        while (msg != null) {
            process(msg);
            msg = queue.poll();
        }
    }

    private void process(ReceivedMessage rmsg) {
        Message message = rmsg.getMessage();

        RemoteAbonent sender = rmsg.getSender();
        if(game.amIServer()) {
            broadcastMessageExcept(message, sender);
        }

        switch (message.getType()) {
            case Message.TURN_MESSAGE:
                TurnMessage msg = (TurnMessage) message;
                TurnMessage.Action action = msg.getAction();
                Plane plane = game.getPlane(indexOf(sender));
                processControlMessage(msg, plane);
                switch(action) {
                    case GO_LEFT: plane.goLeft(); break;
                    case GO_RIGHT: plane.goRight(); break;
                    case GO_STRAIGHT: plane.goStraight(); break;
                    case ENGINE: onEngine(plane); break;
                    case FIRE: fire(plane); break;
                }
                break;
            case Message.LOADED_MESSAGE:
                game.onLoadedMessage();
                break;
        }
    }

    private void fire(Plane plane) {
        Bullet bullet = plane.fire(game.getBulletsGroup());
//        game.getGameObjects().add(bullet);
//        game.getBulletsGroup().add(bullet.getSceneObject());
    }

    private void onEngine(Plane plane) {
        if(plane.isEngineOn()) {
            plane.stopEngine();
        } else {
            plane.startEngine();
        }
    }

    private void processControlMessage(ControlMessage msg, Plane plane) {
        plane.setXY(msg.getX(), msg.getY());
        plane.setVy(msg.getVy());
        plane.setVx(msg.getVx());
        plane.setAngle(msg.getAngle());
    }

    private int indexOf(RemoteAbonent sender) {
        for(Player player : players) {
            if(player.getAbonent() == sender) return players.indexOf(player);
        }
        throw new RuntimeException("abonent not found");
    }



    public void broadcastMessage(Message msg) {
        if(MyActivity.type == MyActivity.Type.NO_BT) return;
        for(Player player : players) {
            if(player.getAbonent() != null) player.getAbonent().sendMessage(msg);
        }
    }

    public void broadcastMessageExcept(Message msg, RemoteAbonent exception) {
        if(MyActivity.type == MyActivity.Type.NO_BT) return;
        for(Player player : players) {
            if(player.getAbonent() != exception && player.getAbonent() != null) player.getAbonent().sendMessage(msg);
        }
    }
}
