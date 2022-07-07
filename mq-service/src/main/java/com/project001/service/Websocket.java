package com.project001.service;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/webSocket")
public class Websocket {

    private Session session;
    private static CopyOnWriteArraySet<Websocket> websockets = new CopyOnWriteArraySet<>();


    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        websockets.add(this);
    }

    @OnClose
    public void onClose(){
        websockets.remove(this);
    }

    @OnMessage
    public void onMessage(String message)
    {

    }


    public void sendMessage(String message)
    {
        for (Websocket websocket : websockets) {
            try {
                //发送消息
                websocket.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
