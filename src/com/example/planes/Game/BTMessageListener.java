package com.example.planes.Game;

import com.example.planes.Communication.Message.ControlMessage;
import com.example.planes.Communication.Message.TurnMessage;
import com.example.planes.Communication.Message.Message;
import com.example.planes.Communication.Message.ReceivedMessage;
import com.example.planes.Communication.MessageListener;
import com.example.planes.Communication.RemoteAbonent;
import com.example.planes.Game.Models.Plane;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by egor on 06.08.15.
 */
public class BTMessageListener implements MessageListener {
    private final Game game;
    private ArrayList<RemoteAbonent> abonents = new ArrayList<>();

    public BTMessageListener(Game game, ArrayList<RemoteAbonent> otherPlayers) {
        this.game = game;
        configure(otherPlayers, game.getMyId());
    }

    ConcurrentLinkedQueue<ReceivedMessage> queue = new ConcurrentLinkedQueue<>();

    @Override
    public void onMessage(ReceivedMessage msg) {
        queue.add(msg);
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
                }
                break;
        }
    }

    private void onEngine(Plane plane) {
        if(plane.isEngineOn()) {
            plane.stopEngine();
        } else {
            plane.startEngine();
        }
    }

    private void processControlMessage(ControlMessage msg, Plane plane) {
        plane.getSceneObject().setXY(msg.getX(), msg.getY());
        plane.setVy(msg.getVy());
        plane.setVx(msg.getVx());
        plane.getSceneObject().setAngle(msg.getAngle());
    }

    private int indexOf(RemoteAbonent sender) {
        int index = abonents.indexOf(sender);
        if(index == -1) throw new RuntimeException("abonent not found");
        return index;
    }

    public void configure(ArrayList<RemoteAbonent> otherPlayers, int playerId) {
        abonents.clear();
        abonents.addAll(otherPlayers);
        abonents.add(playerId, null);
    }

    public void broadcastMessage(Message msg) {
        for(RemoteAbonent abonent : abonents) {
            if(abonent != null) abonent.sendMessage(msg);
        }
    }

    public void broadcastMessageExcept(Message msg, RemoteAbonent exception) {
        for(RemoteAbonent abonent : abonents) {
            if(abonent != exception && abonent != null) abonent.sendMessage(msg);
        }
    }
}
