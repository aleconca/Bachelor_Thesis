package it.polimi.ingsw.server;

import it.polimi.ingsw.Network.MessageToClient.MessageToClient;
import it.polimi.ingsw.Network.MessageToClient.RivActivatedMsg;
import it.polimi.ingsw.Network.MessageToClient.StartTurnMsg;
import it.polimi.ingsw.Network.MessageToClient.WaitForTurnMsg;
import it.polimi.ingsw.Network.MessageToServer.MessageToServer;
import it.polimi.ingsw.server.Controller.Controller;
import it.polimi.ingsw.server.Controller.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientHandler implements Runnable {

    private Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String username;
    private Game game;
    private Controller controller;
    private boolean host;
    private int playernumber;
    private int numberofplayers;
    private boolean gamestarted=false;
    private AtomicBoolean isYourTurn = new AtomicBoolean(false);
    private boolean gameOver = false; //fine gioco
    private MessageToServer m;
    private boolean beginTurn= false;
    private boolean RIVactivared = false;

    public ClientHandler(Socket clientSocket, boolean host, int playernumber, Game game, Controller controller) {
        this.client = clientSocket;
        this.host = host;
        this.playernumber = playernumber;
        this.game = game;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(client.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connected to " + client.getInetAddress());

        try {

            if (host) {
                HostLoginRequest();

            } else
                ClientLoginRequest(playernumber);

            WaitForGameStarted();
            //end connection set-up -->begin ping-pong messages

            WaitForTurn();
            controller.startGame();

            controller.initGame(game.getLeaderCardsDeck());
            m = (MessageToServer) in.readObject();
            m.process(controller);

            out.reset();


        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("KO");
        }
        while (!gameOver) {
            try {
                WaitForTurn();

                if (!gameOver) {
                    if (beginTurn) {
                        StartTurnMsg start = new StartTurnMsg(game.getMarket(), game.getDevelopCardsDeck(), controller.PlayerTurnisTrue(), game.getPlayers());
                        sendMsg(start);
                        beginTurn = false;
                    }

                    m = (MessageToServer) in.readObject();
                    m.process(controller);
                    out.reset();
                }

            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("GAME OVER");
    }

    /**
     * Method to send message to client
     */
    public void sendMsg(MessageToClient msg) throws IOException {
        out.writeObject(msg);
    }

    /**
     * Method to set parameters for player 1 (host)
     */
    public synchronized void HostLoginRequest() throws IOException, ClassNotFoundException {
        username = (String) in.readObject();//riceve dal client l'username
        game.SetFirstPlayer(username);
        System.out.println("Host username: " + game.getPlayers().get(0).getName() + "\n\n");
        out.writeObject("Login success... you are PLAYER 1 (HOST)");
        int i = (int) in.readObject();
        numberofplayers = i;
        isYourTurn.set(true);
        if (i==1)
            System.out.println("\n** SINGLE PLAYER GAME STARTED **");
    }

    /**
     * Getter
     */
    public synchronized int getNumberofplayers() {
        return numberofplayers;
    }

    /**
     * Method to set parameters for players 2,3,4
     */
    public synchronized void ClientLoginRequest(int playernumber) throws IOException, ClassNotFoundException {
        username = (String) in.readObject();
        if (playernumber==1) {
            game.SetSecondPlayer(username);
            System.out.println("Second player username: " + game.getPlayers().get(1).getName() + "\n\n");
            out.writeObject("Login success... you are PLAYER 2");
        }
        else if (playernumber==2){
            game.SetThridPlayer(username);
            System.out.println("Third player username: " + game.getPlayers().get(2).getName() + "\n\n");
            out.writeObject("Login success... you are PLAYER 3");
        }
        else if (playernumber==3){
            game.SetFourthPlayer(username);
            System.out.println("Fourth player username: " + game.getPlayers().get(3).getName() + "\n\n");
            out.writeObject("Login success... you are PLAYER 4");
            System.out.println("\nFour Players connected!");
            System.out.println("\n** GAME STARTED **");
        }
    }

    /**
     * Method to wait for the beginning of the game
     */
    public synchronized void WaitForGameStarted() throws InterruptedException, IOException {
        while(!gamestarted){
            this.wait(2000);
        }
    }

    /**
     * Method for waiting for the player turn, and it send a message if the RIV got activated
     */
    public synchronized void WaitForTurn() throws InterruptedException, IOException {
        RivActivatedMsg msg = new RivActivatedMsg();
        int c=0;
        while(!getIsYourTurn().get()){
            if (isRIVactivared()){
                sendMsg(msg);
                setRIVactivared(false);
            }
            if (game.getPlayers().size()>1 && c==0) {
                WaitForTurnMsg msg1 = new WaitForTurnMsg(controller.PlayerTurnisTrue());
                sendMsg(msg1);
            }
            this.wait(2000);
            c++;
        }
    }

    /**
     * Getter and Setter
     */
    public int getPlayernumber() {
        return playernumber;
    }

    public AtomicBoolean getIsYourTurn() {
        return isYourTurn;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setGamestarted(boolean gamestarted) {
        this.gamestarted = gamestarted;
    }

    public void setBeginTurn(boolean beginTurn) {
        this.beginTurn = beginTurn;
    }

    public boolean isRIVactivared() {
        return RIVactivared;
    }

    public void setRIVactivared(boolean RIVactivared) {
        this.RIVactivared = RIVactivared;
    }
}