package com.example.planes.Communication.Message;

import com.example.planes.Game.Models.Plane;

import java.nio.ByteBuffer;

/**
 * Created by egor on 06.08.15.
 */
public class TurnMessage extends ControlMessage {

    public Action getAction() {
        return action;
    }

    public static enum Action {
        GO_LEFT, GO_RIGHT, GO_STRAIGHT, ENGINE, FIRE
    }

    private final Action action;

    public TurnMessage(Action act, Plane plane) {
        super(TURN_MESSAGE, 4, plane);
        action = act;
        contents.putInt(action.ordinal());
    }

    public TurnMessage(ByteBuffer buffet) {
        super(TURN_MESSAGE, 4, buffet);
        action = Action.values()[buffet.getInt()]; // todo not safe
    }
}
