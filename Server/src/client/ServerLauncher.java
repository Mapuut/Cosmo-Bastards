package client;

import client.graphics.UpdateController;

public class ServerLauncher  {

    public void start() {
        new Thread(new UpdateController()).start();
        System.out.println("Server launched!");
    }

//    public static void main(String[] args) {
//        new ServerLauncher().start();
//    }
}
