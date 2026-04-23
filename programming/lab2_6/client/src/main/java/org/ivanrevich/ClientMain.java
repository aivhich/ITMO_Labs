package org.ivanrevich;

import org.ivanrevich.models.Vehicle;
import org.ivanrevich.network.Client;

import java.net.InetSocketAddress;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        Client client = new Client(new InetSocketAddress("localhost", 8000));
        client.sendObject(new Vehicle(0));
    }
}