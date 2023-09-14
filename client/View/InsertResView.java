package it.polimi.ingsw.client.View;

import it.polimi.ingsw.Network.MessageToServer.DiscardResMsg;
import it.polimi.ingsw.Network.MessageToServer.ResSpotMsg;
import it.polimi.ingsw.Network.MessageToServer.SwitchLvlsMsg;
import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.server.Model.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class InsertResView {
    ServerHandler serverHandler; //per mandare i messaggi
    ArrayList<Resource> resources;
    //boolean isFirst; //need to print resources?

    private final String[] COIN = {"\u00a9"};
    private final String[] STONE = {"\u25cf"};
    private final String[] SHIELD = {"\u25a0"};
    private final String[] SERVANT = {"\u263a"};

    /**
     * Constructor
     */
    public InsertResView(ServerHandler server, ArrayList<Resource> resources) {
        this.serverHandler = server;
        this.resources = resources;
    }

    /**
     * Method to print resources
     */
    public void printResources(ArrayList<Resource> resources) throws IOException {
        System.out.print("\nHere are the resources you picked up: ");
        for (Resource res: resources) {
            PrintResource(res.getResourcename());
        }
        switchLvls(resources);
    }

    /**
     * Method to make choose if and, eventually, which levels to switch
     */
    public void switchLvls(ArrayList<Resource> resources) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String YorN;
        int lv1;
        int lv2;

        System.out.println("\n\nDo you want to switch levels in your storage to make room? ['Y' or 'N']");
        YorN = scanner.next();
        if(YorN.equals("Y") || YorN.equals("y")) {
            System.out.println("What levels do you want to switch? [Press enter after typing each level] ");
            lv1 = scanner.nextInt();
            lv2 = scanner.nextInt();
            SwitchLvlsMsg msg = new SwitchLvlsMsg(lv1, lv2, resources);
            serverHandler.sendMsg(msg);
        }
        else if(YorN.equals("N") || YorN.equals("n")){
            System.out.println("Right, let's start inserting these resources in your Storage! ");
            chooseResSpot(resources);
        }
        else {
            System.out.println(Colour.ANSI_RED + "Invalid input! " + Colour.RESET + "Retry: ");
            switchLvls(resources);
        }


    }

    /**
     * Method to choose whether to discard or keep a resource and select the level to store it
     */
    public void chooseResSpot(ArrayList<Resource> resources) {
        Scanner scanner = new Scanner(System.in);
        String ans;

        PrintResource(resources.get(0).getResourcename());
        System.out.println(" -> Do you want to keep ['K'] or discard ['D'] this resource? ");
        ans = scanner.next();
        if(ans.equals("D") || ans.equals("d")){
            DiscardResMsg discardResMsg = new DiscardResMsg();
            discardResMsg.setResources(resources);
            try {
                serverHandler.sendMsg(discardResMsg);
            } catch (IOException e) {
                System.out.println(Colour.ANSI_RED + "Error" + Colour.RESET + "in sending message... Retry:");
                chooseResSpot(resources);
            }
        }
        else if(ans.equals("K") || ans.equals("k")){
            System.out.println("In which level do you wanna put this resource? (You can go up to see your Storage ;) )");
            ResSpotMsg resSpotMsg = new ResSpotMsg();
            resSpotMsg.setRes(resources);
            // insertRes will pick up the first resource of the Array
            // (and then return by a msg the new array without this resource inserted)
            resSpotMsg.setLevel(scanner.nextInt());
            try {
                serverHandler.sendMsg(resSpotMsg);
            } catch (IOException e) {
                System.out.println(Colour.ANSI_RED + "Error" + Colour.RESET + "in sending message... Retry:");
                chooseResSpot(resources);
            }
        }
        else {
            System.out.println(Colour.ANSI_RED + "Invalid input!" + Colour.RESET + "Retry...");
            chooseResSpot(resources);
        }

    }

    /**
     * Method to print a resource with a string as a input
     */
    private void PrintResource(String res){
        switch (res) {
            case "coin" -> System.out.print(Colour.ANSI_YELLOW + Arrays.toString(COIN) + Colour.RESET);
            case "stone" -> System.out.print(Colour.ANSI_GREY + Arrays.toString(STONE) + Colour.RESET);
            case "servant" -> System.out.print(Colour.ANSI_PURPLE + Arrays.toString(SERVANT) + Colour.RESET);
            case "shield" -> System.out.print(Colour.ANSI_BLUE + Arrays.toString(SHIELD) + Colour.RESET);
        }
    }

}
