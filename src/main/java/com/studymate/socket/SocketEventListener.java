package com.studymate.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;

public abstract class SocketEventListener<T> implements DataListener<T> {

    public SocketEventListener(SocketIOServer server) {
        Class<T> classType = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        server.addEventListener(getName(), classType, this);
    }

    protected abstract String getName();

}
