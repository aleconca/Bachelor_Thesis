package it.polimi.ingsw.client;

import it.polimi.ingsw.client.View.Colour;
import it.polimi.ingsw.client.View.ViewCLI;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        ServerHandler serverHandler;
        ViewCLI viewCLI;
        int porta;
        Socket socket;


        try {
            //Login();
            Scanner scanner = new Scanner(System.in);

            System.out.println("Welcome to " + Colour.ANSI_RED + "MASTER OF RENAISSANCE" + Colour.RESET + " Game!");
            System.out.println("To create a game or connect with your friends, please fill the following fields ^_^ ");

            System.out.print("Type username: ");
            String username = scanner.nextLine();

            System.out.print("Type ip server: ");
            String ip = scanner.nextLine();

            System.out.print("Type port number: ");
            porta = scanner.nextInt();
            socket = new Socket(ip, porta);

            viewCLI = new ViewCLI();
            serverHandler = new ServerHandler(socket, username, viewCLI);
            viewCLI.setServerHandler(serverHandler);
            Thread sHThread = new Thread(serverHandler);
            sHThread.start();


        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Connection failed. Port Number Invalid");
        }
    }

}