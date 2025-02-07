package com.rewrite.socketio;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URI;

public class SocketIOClientExample {
    public static void main(String[] args) throws Exception {
        URI uri = new URI("http://localhost:3000"); // 替换为你的Socket.IO服务器地址
        Socket socket = IO.socket(uri);

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Connected to server!");
                socket.emit("message", "Hello from Java client!"); // 发送消息到服务器
            }
        }).on("response", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Received response: " + args[0]); // 接收服务器响应
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Disconnected from server!");
            }
        });

        socket.connect();
    }
}
