package com.football.interactors.service;

import com.github.nkzawa.socketio.client.Socket;

public class SocketHelper {

    private static final String EVENT_REGISTER = "";

    public static void register(Socket socket, String token) {
        socket.emit(Socket.EVENT_CONNECT, token);

    }
}
