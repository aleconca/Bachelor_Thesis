package it.polimi.ingsw.client.View;


import it.polimi.ingsw.Network.MessageToServer.*;
import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.server.Model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ViewCLI implements View {

    private ServerHandler serverHandler;

    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    private final String[] COIN = {"\u00a9"};
    private final String[] STONE = {"\u25cf"};
    private final String[] SHIELD = {"\u25a0"};
    private final String[] SERVANT = {"\u263a"};

    /**
     * Method to choose 2 leaderCards and possibly resources at the beginning of the game
     */
    @Override
    public void initGame(Player player, ArrayList<LeaderCard> cards) throws IOException {
        InitGameView initGameView = new InitGameView();

        initGameView.initGameResources(player);
        initGameView.chooseLeaderCards(cards);

        InitGameResponse msg = new InitGameResponse(initGameView.getLeaderCardsChosen(), cards, initGameView.getResourcesChosen(), player);
        serverHandler.sendMsg(msg);
    }

    /**
     * The start of each turn; user can choose the action to do as first
     * the enum/string representing the action chosen
     * */
    @Override
    public void startTurn(Market market, DevelopCardDeck developCardDeck, Player player) throws IOException {

        System.out.println("\nIT'S YOUR TURN! What do you want to do?");
        System.out.println("Above you can see the " + Colour.ANSI_GREEN + "Develop Cards Grid"
                + Colour.RESET + ", the " + Colour.ANSI_GREEN + "Market "
                + Colour.RESET + "and" + Colour.ANSI_GREEN + " your Dashboard" + Colour.RESET + "!");
        System.out.println("Go to Market " + Colour.ANSI_RED + "[m]" + Colour.RESET +
                ", buy Develop Card " + Colour.ANSI_RED + "[d]" + Colour.RESET +
                ", active Production " + Colour.ANSI_RED + "[p]" + Colour.RESET +
                ", active Leader card(s) " + Colour.ANSI_RED + "[l]" + Colour.RESET +
                ", discard Leader card(s) " + Colour.ANSI_RED + "[dis]" + Colour.RESET +
                ", end turn" + Colour.ANSI_RED + " [e]" + Colour.RESET);

        Scanner scanner = new Scanner(System.in);
        String action = scanner.next();//stringa di risposta

        SelectedTurnMsg msg = new SelectedTurnMsg(action, player);
        serverHandler.sendMsg(msg);
    }

    /**
     * Method to that verifies the action in the market
     */
    public void goToMarket(Market m) {
        MarketView market = new MarketView(m, false);
        if (market.sureMessage(m)) {
            SelectedMarblesMsg selmarblemsg = new SelectedMarblesMsg();
            selmarblemsg.setRorC(market.getRorC());
            selmarblemsg.setNumber(market.getNumber() - 1);
            try {
                serverHandler.sendMsg(selmarblemsg);
            } catch (IOException e) {
                System.out.println("Error connection to server.");
                System.out.println("Re-insert data.");
                goToMarket(m);
            }
        } else {
            System.out.println("Don't worry. You can choose again ^_^\n");
            goToMarket(m);
        }
    }

    /**
     * Method to insert the resources in the storage
     */
    public void insertRes(ArrayList<Resource> resources, String action, boolean usedWhite) throws IOException {
        InsertResView resView = new InsertResView(serverHandler, resources);
        if (usedWhite){
            System.out.println("All WHITE MARBLES have been transformed using your" + Colour.ANSI_ORANGE + "'White Marble' Leader Card! \n" + Colour.RESET +
                    "Enjoy your resources!" + Colour.ANSI_RED + " <3\n" + Colour.RESET);
        }
        try {
            if (action.equals("f")) { //"f"irst action
                resView.printResources(resources);
            } else if (action.equals("c")) { //"c"hoose Spot
                System.out.println("Success!! Go on... ");
                resView.chooseResSpot(resources);
            } else if (action.equals("s")) {
                resView.switchLvls(resources);
            } else if (action.equals("ce")) {
                System.out.println("Choose another spot for this resource: ");
                resView.chooseResSpot(resources);
            } else {
                System.out.println("Error");
                insertRes(resources, action, usedWhite); //??
            }

        } catch (IOException e) {
            System.out.println(Colour.ANSI_RED + "There was an error!" + Colour.RESET + "Retry...");
            insertRes(resources, action, usedWhite);
        }
    }

    /**
     * Method to choose the resource of the white marble
     */
    public void chooseWhiteMarble(String[] resources, Marble[] marbles, int numWhite) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int[] chosenCards = new int[4];

        System.out.println("\nYou activated" + Colour.ANSI_ORANGE + " 2 'White Marble' Leader Cards!" + Colour.RESET);
        System.out.println("So you can choose what resource to pick for each white marble you took from the Market. ");

        for (int i = 0; i < numWhite; i++) {
            System.out.println("What "+ Colour.ANSI_ORANGE + "card" + Colour.RESET + " do you want to use for this marble? ");
            System.out.println("Choose between:\n  - first card [transform in: " + resources[0] + "] - '1' ");
            System.out.println("  - second card [transform in: " + resources[1] + "] - '2' ");
            chosenCards[i] = scanner.nextInt();
        }

        WhiteMarbleResponseMsg msg = new WhiteMarbleResponseMsg(chosenCards, marbles);
        serverHandler.sendMsg(msg);
    }

    /**
     * To print the resources
     */
    private void PrintResource(String res) {
        switch (res) {
            case "coin" -> System.out.print(Colour.ANSI_YELLOW + Arrays.toString(COIN) + Colour.RESET);
            case "stone" -> System.out.print(Colour.ANSI_GREY + Arrays.toString(STONE) + Colour.RESET);
            case "servant" -> System.out.print(Colour.ANSI_PURPLE + Arrays.toString(SERVANT) + Colour.RESET);
            case "shield" -> System.out.print(Colour.ANSI_BLUE + Arrays.toString(SHIELD) + Colour.RESET);
        }
    }

    /**
     *
     */
    public void printStorage(Storage s) {

        System.out.println("\n\n** STORAGE **\n");
        ArrayList<Resource> level;
        level = s.getLvl1();
        if (s.getLvl1().isEmpty() && s.getLvl2().isEmpty() && s.getLvl3().isEmpty()) {
            System.out.println("STORAGE IS EMPTY\n");
        } else {
            if (!s.getLvl1().isEmpty()) {
                System.out.print("1 ->  ");
                PrintResource(level.get(0).getResourcename());
            } else
                System.out.print("1 -> EMPTY");
            if (!s.getLvl2().isEmpty()) {
                level = s.getLvl2();
                System.out.print("\n\n2 ->  ");
                for (Resource resource : level) {
                    PrintResource(resource.getResourcename());
                    System.out.print("  ");
                }
            } else
                System.out.print("\n\n2 -> EMPTY");
            if (!s.getLvl3().isEmpty()) {
                level = s.getLvl3();
                System.out.print("\n\n3 ->  ");
                for (Resource resource : level) {
                    PrintResource(resource.getResourcename());
                    System.out.print("  ");
                }
            } else
                System.out.print("\n\n3 -> EMPTY\n\n");
        }
    }

    //-------------------------------View Buy  Dev Cards------------------------------------------------------------------

    /**
     * Method to show the devCards grid to the player
     */
    @Override
    public void ShowDevCards(ArrayList<DevelopCard> lvl1, ArrayList<DevelopCard> lvl2, ArrayList<DevelopCard> lvl3, Player player) throws IOException {

        /*Grid which will be printed vertically:
                lvl3 [1-green][4-blu][7-yellow][10-purple]
                lvl2 [2-green][5-blu][8-yellow][11-purple]
                lvl1 [3-green][6-blu][9-yellow][12-purple]
                 */


        int cardNum = 0;
        int index;
        //object that allows the usage of the search methods
        SearchCardsMethods2 searchMethods = new SearchCardsMethods2();

        for (int i = 0; i < 4; i++) {

            cardNum++;
            //lvl3
            System.out.print("\ncard : " + cardNum);

            index = searchMethods.searchCardLvl3(lvl3, cardNum);

            if (index >= 0) {
                devCard cardView = new devCard(lvl3.get(index));
            } else {
                System.out.println(Colour.ANSI_RED + "\nCards finished." + Colour.RESET);
            }


            cardNum++;

            //lvl2
            System.out.print("\ncard : " + cardNum);

            index = searchMethods.searchCardLvl2(lvl2, cardNum);

            if (index >= 0) {
                devCard cardView = new devCard(lvl2.get(index));
            } else {
                System.out.println(Colour.ANSI_RED + "\nCards finished." + Colour.RESET);
            }

            cardNum++;

            //lvl1
            System.out.print("\ncard : " + cardNum);

            index = searchMethods.searchCardLvl1(lvl1, cardNum);

            if (index >= 0) {
                devCard cardView = new devCard(lvl1.get(index));
            } else {
                System.out.println(Colour.ANSI_RED + "\nCards finished." + Colour.RESET);
            }

        }


        //which card?
        System.out.println("\nInsert the number of the card you'd like to buy: "
                + Colour.ANSI_GREEN + "(insert a number from 1 to 12)" + Colour.RESET);
        Scanner scanner = new Scanner(System.in);

        int chosenIndex = scanner.nextInt();//chosen Index

        //controls: asks the integer until it is in range
        while (chosenIndex < 1 || chosenIndex > 12) {
            //ask again
            System.out.println("\nInvalid input. Try again.\nInsert the number of the card you'd like to buy: "
                    + Colour.ANSI_GREEN + "(insert a number from 1 to 12)" + Colour.RESET);
            chosenIndex = scanner.nextInt();//chosen Index

        }

        PrintDevCards(player);

        //in which slot do you wanna put this card?
        System.out.println("\nIn which card slot you'd like to put this card: "
                + Colour.ANSI_GREEN + "(insert a number from 1 to 3)" + Colour.RESET);
        int chosenSlot = scanner.nextInt();

        //controls: asks for the integer until it is in range
        while (chosenSlot < 1 || chosenSlot > 3) {
            //ask again
            System.out.println("\nInvalid input. Try again.\nInsert the slot number: "
                    + Colour.ANSI_GREEN + "(insert a number from 1 to 3)" + Colour.RESET);
            chosenSlot = scanner.nextInt();//chosen Index

        }

        //sends indexes to call buyDevCard method
        SelectedDevCardMsg msg = new SelectedDevCardMsg(lvl1, lvl2, lvl3, chosenIndex, chosenSlot);
        serverHandler.sendMsg(msg);

    }

    /**
     * Method to print the last devCard for each slot
     */
    @Override
    public void PrintDevCards(Player player) {

        //print the last develop card for each slot
        ArrayList<DevelopCard> slot1 = player.getDashboard().getCardSlot().get(0).getSlot();
        ArrayList<DevelopCard> slot2 = player.getDashboard().getCardSlot().get(1).getSlot();
        ArrayList<DevelopCard> slot3 = player.getDashboard().getCardSlot().get(2).getSlot();

        if (slot1.size() == 0 && slot2.size() == 0 && slot3.size() == 0) {

            System.out.println("\nYour slots are empty.");

        } else {

            System.out.println("\nThese are the develop cards you already have in your slots:");

            if (slot1.size() > 0) {

                VerifyEmptySlot(slot1, 1);

            }

            if (slot2.size() > 0) {

                VerifyEmptySlot(slot2, 2);

            }

            if (slot3.size() > 0) {

                VerifyEmptySlot(slot3, 3);

            }

        }

    }


    /**
     * Prints a message to advice the player that the selected develop card ha been added to his slot
     */
    @Override
    public void DevCardSuccess(String toPrint) {
        System.out.println(toPrint);
    }


    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Method to print the dashboard for singlePlayer
     */
    @Override
    public void ShowDashboardSingle(Player player) {
        DashboardView dashboardView = new DashboardView(player, null);
        dashboardView.visualizePerSingle();
    }
    /**
     * Method to print the dashboard for Multiplayer
     */
    @Override
    public void ShowDashboardMulti(Player player, ArrayList<Player> players) {
        DashboardView dashboardView = new DashboardView(player, players);
        dashboardView.visualizePerMulti();
    }
    /**
     * Method to print the actionSignal
     */
    @Override
    public void ShowActionSignal(ActionSignal actionSignal) {
        System.out.print(Colour.ANSI_RED + "\n*" + Colour.RESET + " Action Signal: ");
        visualizeSignal(actionSignal);
        actionActivatedSignal(actionSignal);
    }
    /**
     * Method to print the actionSignal
     */
    private void visualizeSignal(ActionSignal actionSignal) {
        String CROSS2 = Colour.ANSI_BLACK + "\u24f6" + Colour.RESET;
        String CROSS1 = Colour.ANSI_BLACK + "\u24f5" + Colour.RESET;
        String REQBLUE = "-2" + Colour.ANSI_BLUE + "\u25ae" + Colour.RESET;
        String REQYELLOW = "-2" + Colour.ANSI_YELLOW + "\u25ae" + Colour.RESET;
        String REQPURPLE = "-2" + Colour.ANSI_PURPLE + "\u25ae" + Colour.RESET;
        String REQGREEN = "-2" + Colour.ANSI_GREEN + "\u25ae" + Colour.RESET;

        if (actionSignal instanceof BlackCross1Reset)
            System.out.println(CROSS1);
        else if (actionSignal instanceof BlackCrossMove2)
            System.out.println(CROSS2);
        else if (actionSignal instanceof DiscardBlueSignal)
            System.out.println(REQBLUE);
        else if (actionSignal instanceof DiscardGreenSignal)
            System.out.println(REQGREEN);
        else if (actionSignal instanceof DiscardPurpleSignal)
            System.out.println(REQPURPLE);
        else if (actionSignal instanceof DiscardYellowSignal)
            System.out.println(REQYELLOW);
    }

    private void actionActivatedSignal(ActionSignal actionSignal) {
        if (actionSignal instanceof BlackCross1Reset)
            System.out.println("Lorenzo is moving (+1) and the ActionSignal stack has been reset\n");
        else if (actionSignal instanceof BlackCrossMove2)
            System.out.println("Lorenzo is moving (+2)\n");
        else if (actionSignal instanceof DiscardBlueSignal)
            System.out.println("2 Blue Cards has been discarded!\n");
        else if (actionSignal instanceof DiscardGreenSignal)
            System.out.println("2 Green Cards has been discarded!\n");
        else if (actionSignal instanceof DiscardPurpleSignal)
            System.out.println("2 Purple Cards has been discarded!\n");
        else if (actionSignal instanceof DiscardYellowSignal)
            System.out.println("2 Yellow Cards has been discarded!\n");
    }

    /**
     * Print the market
     */
    @Override
    public void ShowMarket(Market market) {
        MarketView marketView = new MarketView(market, true);
    }


    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Method to activate a leaderCard
     */
    public void activateLeaderCard() throws IOException {
        int n = 0;
        int counter = 0;
        Scanner scanner = new Scanner(System.in);

        do {

            if (counter > 0)
                System.out.println("Try again...");
            System.out.println("Select which leadercard you wanna activate: " + Colour.ANSI_RED + " 1 " + Colour.RESET +
                    " or " + Colour.ANSI_RED + " 2 " + Colour.RESET);
            n = scanner.nextInt();

            counter++;
        } while (n < 1 || n >= 3);

        LeaderCardNumberMsg msg = new LeaderCardNumberMsg(n);
        serverHandler.sendMsg(msg);
    }

    /**
     * Method to discard the leaderCard
     */
    public void discardLeaderCard() throws IOException {
        int n = 0;
        int counter = 0;
        Scanner scanner = new Scanner(System.in);

        do {
            if (counter > 0)
                System.out.println("Try again...");
            System.out.println("Select which leadercard you wanna discard: " + Colour.ANSI_RED + " 1 " + Colour.RESET +
                    " or " + Colour.ANSI_RED + " 2 " + Colour.RESET);
            n = scanner.nextInt();

            counter++;
        } while (n < 1 || n >= 3);

        LeaderCardNumberDiscardMsg msg = new LeaderCardNumberDiscardMsg(n);
        serverHandler.sendMsg(msg);
    }


    //--------------------View activate Production------------------------------------------------------------------------


    /**
     * Array of strings that will be used server side to remove the resources from the palyer
     */
    @Override
    public ArrayList<String> DevelopCardProduction() {

        //Array of strings that will be used server side to remove the resources from the palyer
        ArrayList<String> develop = new ArrayList<>();
        int counter = 0;
        Scanner scanner = new Scanner(System.in);
        String w;//second output string
        boolean stop = false;


        while (counter <= 4) {

            if (counter==0){

                System.out.println("\nInsert  necessary resource number " + Colour.ANSI_ORANGE + counter + Colour.RESET +
                        Colour.ANSI_GREEN + ":[coin,shield,stone,servant]." + Colour.RESET);
                w = scanner.next();

                develop = VerifyInput(w,develop);

            }else if (counter > 1) {

                while (!stop) {

                    System.out.println("\nNeed to insert another resource?:" + Colour.ANSI_GREEN + "[y/n]" + Colour.RESET);

                    String s = scanner.next();

                    if (s.equals("n")) {

                        stop = true;
                        return develop;//fine richiesta dell'input

                    } else if (s.equals("y")) {

                        System.out.println("\nInsert  necessary resource number " + Colour.ANSI_ORANGE + counter + Colour.RESET +
                                Colour.ANSI_GREEN + ":[coin,shield,stone,servant]." + Colour.RESET);
                        w = scanner.next();

                        develop = VerifyInput(w, develop);

                    } else {

                        System.out.println(Colour.ANSI_RED + "\nNot a valid input." + Colour.RESET + " Try again.");//ritenta

                    }
                }

            }

            counter++;

        }

        System.out.println("\nMax resources' number reached.");

        return develop;

    }


    /**
     * Verifies input and returns array basic with the string resource added
     **/
    @Override
    public ArrayList<String> VerifyInput(String s, ArrayList<String> basic) {
        Scanner scanner = new Scanner(System.in);

        if (!s.equals("coin") && !s.equals("shield") && !s.equals("stone") && !s.equals("servant")) {

            while (!(s.equals("coin") || s.equals("shield") || s.equals("stone") || s.equals("servant"))) {
                System.out.println(Colour.ANSI_RED + "\nInvalid resource name." + Colour.RESET + " Choose between " + Colour.ANSI_GREEN + " coin,shield,stone or servant." + Colour.RESET);
                s = scanner.next();
            }

        }
        basic.add(s);

        return basic;//returns the new array

    }

    /**
     *  Print the last develop card for each slot
     */
    @Override
    public int PrintSlots(Player player) {

        //print the last develop card for each slot
        ArrayList<DevelopCard> slot1 = player.getDashboard().getCardSlot().get(0).getSlot();
        ArrayList<DevelopCard> slot2 = player.getDashboard().getCardSlot().get(1).getSlot();
        ArrayList<DevelopCard> slot3 = player.getDashboard().getCardSlot().get(2).getSlot();
        int counter = 0;

        //zero
        if (slot1.size() == 0 && slot2.size() == 0 && slot3.size() == 0) {

            System.out.println("\nYour slots are empty. You can't activate any develop card.");
            counter = 3;

        } else {

            System.out.println("\nThese are the slots you can activate:");

            if (slot1.size() > 0) {

                VerifyEmptySlot(slot1, 1);

            }

            if (slot2.size() > 0) {

                VerifyEmptySlot(slot2, 2);

            }

            if (slot3.size() > 0) {

                VerifyEmptySlot(slot3, 3);

            }

        }
        return counter;//counter == 4 salta richiesta slot

    }

    /**
     *  Print the last develop card for each slot
     */
    public void PrintSlotsForDashboard(Player player) {

        //print the last develop card for each slot
        ArrayList<DevelopCard> slot1 = player.getDashboard().getCardSlot().get(0).getSlot();
        ArrayList<DevelopCard> slot2 = player.getDashboard().getCardSlot().get(1).getSlot();
        ArrayList<DevelopCard> slot3 = player.getDashboard().getCardSlot().get(2).getSlot();
        int counter = 0;

        //zero
        if (slot1.size() == 0 && slot2.size() == 0 && slot3.size() == 0) {

            System.out.println("\n" + Colour.ANSI_MAGENTA + "**" + Colour.RESET + " CARDS SLOTS " + Colour.ANSI_MAGENTA + "**" + Colour.RESET);
            System.out.print("\nEMPTY");
            counter = 3;

        } else {

            System.out.print("\n" + Colour.ANSI_MAGENTA + "**" + Colour.RESET + " CARDS SLOTS " + Colour.ANSI_MAGENTA + "**" + Colour.RESET);
            if (slot1.size() > 0) {

                VerifyEmptySlot(slot1, 1);

            }

            if (slot2.size() > 0) {

                VerifyEmptySlot(slot2, 2);

            }

            if (slot3.size() > 0) {

                VerifyEmptySlot(slot3, 3);

            }

            if (player.getDashboard().numberOfDevelopCard()==1)
                System.out.print("\n\nYou have " + Colour.ANSI_RED + player.getDashboard().numberOfDevelopCard() + Colour.RESET + " develop card");
            else
                System.out.print("\n\nYou have " + Colour.ANSI_RED + player.getDashboard().numberOfDevelopCard() + Colour.RESET + " develop cards");
        }

    }

    /**
     * Method to verify whether the slot is empty or contains a card and print it
     */
    @Override
    public void VerifyEmptySlot(ArrayList<DevelopCard> slotx, int slotNum) {


        if (slotx.size() > 0) {

            System.out.println(Colour.ANSI_MAGENTA + "\n\nSlot" + slotNum + ":" + Colour.RESET);
            devCard cardx = new devCard(slotx.get(slotx.size() - 1));

        }

    }

    /**
     * Method to ask a slot to the player
     */
    @Override
    public ArrayList<Integer> AskSlot(Player player) {

        Scanner scanner = new Scanner(System.in);
        Scanner scanner1 = new Scanner(System.in);
        int x;
        boolean stop= false;
        ArrayList<Integer> num = new ArrayList<>(3);
        ArrayList<Integer> index = new ArrayList<>(3);
        int c=0,c1=0,c2=0;

        System.out.println("\nWhich " + Colour.ANSI_GREEN + "slot" + Colour.RESET + " do you want to activate? " +
                Colour.ANSI_GREEN + "(choose between the options below): " + Colour.RESET);

        for (int i = 1; i<= 3; i++) {
            ArrayList<DevelopCard> slotx = player.getDashboard().getCardSlot().get(i - 1).getSlot();
            if (slotx.size() > 0) {
                System.out.println(Colour.ANSI_GREEN+ "> "+ i +""+ Colour.RESET);
                num.add(i);
            }
        }

        do {

            x = scanner.nextInt();
            for (Integer integer : num) {
                if (x == integer) {
                    if (x == 1)
                        c++;
                    else if (x == 2)
                        c1++;
                    else if (x == 3)
                        c2++;
                }
            }

            if (c == 1 || c1 == 1 || c2 == 1)
                index.add(x-1);
            else System.out.println(Colour.ANSI_RED + "Wrong number!\n" + Colour.RESET);
            if (index.size()==num.size())
                stop=true;

            if (!stop) {
                System.out.println("Do you want another slot?" + Colour.ANSI_GREEN+ " [y/n]" + Colour.RESET);
                String s = scanner1.nextLine();
                if (s.equals("n"))
                    stop=true;
                else System.out.println( Colour.ANSI_GREEN + "Slot: " + Colour.RESET);
            }
        } while (!stop);

        return index;

    }

    /**
     * Method to activate the Devcard production
     */
    @Override
    public void ActivateProduction(Player player) throws IOException {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> basic = new ArrayList<>();//array to activate basic production per attivare base production
        boolean stop = false;
        int counter = 0;
        ArrayList<Integer> z = new ArrayList<>();//integers of the slots that have to be activated
        ArrayList<ArrayList<String>> cards = new ArrayList<>();//memorizes resources of the cards of the slot x
        int x; //int for scanner.nextInt
        boolean ins = true;


        while (!stop) {

            //ask for the arbitrary resources and saves them in order to remove them server side
            System.out.println("\nDo you want to activate your" + Colour.ANSI_YELLOW + " basic production" + Colour.RESET + "?: "
                    + Colour.ANSI_GREEN + "[y/n]" + Colour.RESET);
            String s = scanner.next();


            if (s.equals("y")) {

                //asks for two resources
                for (int i = 0; i < 2; i++) {

                    System.out.println("\nInsert 1 resource to give away: " + Colour.ANSI_GREEN + "[coin,shield,stone,servant]" + Colour.RESET);
                    s = scanner.next();

                    basic = VerifyInput(s, basic);

                }


                System.out.println("\nInsert 1 resource of your choice that you'd like to obtain: "
                        + Colour.ANSI_GREEN + "[coin,shield,stone,servant]" + Colour.RESET);
                s = scanner.next();
                basic = VerifyInput(s, basic);

                //show last card in each dev card slot
                if (player.getDashboard().numberOfDevelopCard()!=0) {
                    System.out.println("\nAlso if you want to activate a " + Colour.ANSI_ORANGE + "develop card" + Colour.RESET + " ...");
                    counter = PrintSlots(player);

                    System.out.println("\nSo do you want to activate a " + Colour.ANSI_YELLOW + "develop card " + Colour.RESET
                            + "?: " + Colour.ANSI_GREEN + "[y/n]" + Colour.RESET);


                    s = scanner.next();

                    if (!s.equals("n")) {
                        z = AskSlot(player);
                    }
                }
                else
                    System.out.println(Colour.ANSI_RED + "\nYou don't have any develop cards to activate!\n" + Colour.RESET);
                stop = true;

            } else if (s.equals("n")) {

                //show dev cards
                if (player.getDashboard().numberOfDevelopCard()!=0) {
                    counter = PrintSlots(player);


                    System.out.println("\nDo you want to activate a " + Colour.ANSI_YELLOW + "develop card " + Colour.RESET
                            + "?: " + Colour.ANSI_GREEN + "[y/n]" + Colour.RESET);

                    s = scanner.next();

                    if (!s.equals("n")) {
                        z = AskSlot(player);
                    }
                }
                else
                    System.out.println(Colour.ANSI_RED + "\nYou don't have any develop cards to activate!\n" + Colour.RESET);
                stop = true;

            }
        }

        //send array with the resources to search the slot to activate and correspondent resources
        //plus resources for the basic production
        SelectedProductionMsg msg = new SelectedProductionMsg(basic, cards, z);
        serverHandler.sendMsg(msg);

    }

    /**
     * Method to activate the LeaderCard production
     */
    @Override
    public void ActivateLeaderProduction(Player player, Resource resource) throws IOException {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> leaderProduction = new ArrayList<>();
        boolean input = false;

        while (!input) {

            System.out.println("\nYou can use your upgraded production leader card by paying 1 " + resource.getResourcename()
                    +" and receive 1 resource of your choice + 1 faith point.");
            System.out.println("\nDo you wanna activate its production?"+ Colour.ANSI_GREEN + " [y or n]" + Colour.RESET);

            String s = scanner.next();

            if (s.equals("y")) {

                System.out.println("\n1 " + resource.getResourcename() + " will be removed from your dashboard.");
                leaderProduction.add(resource.getResourcename());

                System.out.println("\nInsert one resource of your choice: " + Colour.ANSI_GREEN +"[coin,shield,stone,servant]" + Colour.RESET);
                s = scanner.next();
                leaderProduction = VerifyInput(s, leaderProduction);

                System.out.println("\nOne faith point will be added to your faith route.");
                leaderProduction.add("faith indicator");

                SelectedLeaderProductionMsg msg = new SelectedLeaderProductionMsg(leaderProduction);
                serverHandler.sendMsg(msg);


                input=true;
            } else if (s.equals("n")) {
                System.out.println("\nOk.");
                input=true;
            } else {
                System.out.println(Colour.ANSI_RED + "\nInvalid input."+ Colour.RESET +" Try again.");
            }
        }

    }

    /**
     * Method to activate 2 LeaderCards production in the same turn
     */
    @Override
    public void ActivateDoubleLeaderProd(Player player, ArrayList<Resource> req) throws IOException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("\nYou have two active upgraded production leader cards.");

        System.out.println("\nTo activate the production from leader card 1: ");
        ActivateLeaderProduction(player, req.get(0));

        System.out.println("\nTo activate the production from leader card 2: ");
        ActivateLeaderProduction(player,req.get(1));

    }

    //-------------------------EndTurn----------------------------------------------------------------------------------

    /**
     * Print the end of an action
     */
    public void endAction() throws IOException {
        System.out.println(Colour.ANSI_BGREEN + "\nGOOD!"+ Colour.RESET+ " The action is over. ");
        System.out.println("Let's see what you can still do... ");
        IsEndTurnMsg isEndTurnMsg = new IsEndTurnMsg();
        serverHandler.sendMsg(isEndTurnMsg);
    }

    /**
     * Print an error if the player insert a wrong input
     */
    public void endActionDefault() throws IOException {
        System.out.println("INVALID INPUT ! Try again...");
        IsEndTurnMsg isEndTurnMsg = new IsEndTurnMsg();
        serverHandler.sendMsg(isEndTurnMsg);
    }

    //-----------------------Error messages-----------------------------------------------------------------------------

    /**
     * Print an error
     */
    @Override
    public void printError(String error){
        System.out.println(error);
    }

    /**
     * Method to print if occurs an error in the slot
     */
    @Override
    public void SlotError(ArrayList<DevelopCard> lvl1, ArrayList<DevelopCard> lvl2, ArrayList<DevelopCard> lvl3,
                          int chosenIndex, int chosenSlotIndex,Player player) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println(Colour.ANSI_YELLOW+ "⚠" +Colour.RESET+ "You chose an invalid slot index! Try again.");

        //in which slot do you wanna put this card?
        System.out.println("\n\nIn which card slot you'd like to put this card(insert number from 1 to 3):");
        chosenSlotIndex = scanner.nextInt();

        while (chosenSlotIndex<1 || chosenSlotIndex>3){
            System.out.println("\nInvalid input. Try again. \nIn which card slot you'd like to put this card(insert number from 1 to 3):");
            chosenSlotIndex = scanner.nextInt();
        }


        //repeat call to method in controller by keeping the old chosenIndex passed to the method above
        SelectedDevCardMsg msg = new SelectedDevCardMsg(lvl1,lvl2,lvl3,chosenIndex,chosenSlotIndex);
        serverHandler.sendMsg(msg);

    }


    /**
     * Print if occcurs an error in the market
     */
    public void marketError(Market m, String errorType) {
        System.out.println("There was an error in the Market Action: " + errorType);
        System.out.println("Try again!");
        MarketView newMarketView = new MarketView(m, false);
    }

    //---------------------------Wait-----------------------------------------------------------------------------------

    /**
     * Print which player has the turn active
     */
    @Override
    public void WaitForYourTurn(String string) {
        System.out.println(string);
    }

    //----------------------------End Game------------------------------------------------------------------------------

    /**
     * Method that prints the result of the game
     */
    @Override
    public void EndGame(ArrayList<Player> players, ArrayList<Integer> sumFinalPoints, String winner){

        for (int i=0; i<players.size(); i++) {
            System.out.println(Colour.ANSI_BRED + "\u25cf " + Colour.RESET +
                    players.get(i).getName()+", you scored "+ Colour.ANSI_YELLOW + sumFinalPoints.get(i) +
                    Colour.RESET + " points !");
        }
        System.out.println("\nTHE WINNER IS : " + Colour.ANSI_BRED + winner + Colour.RESET);
        serverHandler.setGameover(true);
    }

    /**
     * Print the result of the singlePlayergame
     */
    public void EndGameSinglePlayer(Player player, int points, boolean lorenzowin){
        if (!lorenzowin) {
            System.out.println("\n" +  Colour.ANSI_BRED + "\u25cf " + Colour.RESET +
                    player.getName()+" WINS , you scored "+ Colour.ANSI_YELLOW + points +
                    Colour.RESET + " points !");
        }
        else
            System.out.println( Colour.ANSI_BRED + "LORENZO " + Colour.RESET +  "WIN ... GAME OVER :(" + Colour.RESET);
        serverHandler.setGameover(true);
    }

}
