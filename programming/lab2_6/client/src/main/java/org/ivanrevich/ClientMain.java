package org.ivanrevich;

import org.ivanrevich.network.Client;

import java.net.InetSocketAddress;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.sendMessage("hello", new InetSocketAddress("localhost", 8000));
    }
}