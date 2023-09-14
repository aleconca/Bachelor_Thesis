package it.polimi.ingsw.client.View;

import it.polimi.ingsw.server.Model.*;

import java.util.ArrayList;
import java.util.Arrays;

public class DashboardView {

    private Player player;
    private ArrayList<Player> players;

    private final String BOX = "\u25a1 ";
    private final String FAITHINDICATOR = "\u002b";

    private final String[] COIN = {"\u00a9"};
    private final String[] STONE = {"\u25cf"};
    private final String[] SHIELD = {"\u25a0"};
    private final String[] SERVANT = {"\u263a"};

    /**
     * When creating a dashboard, it will be associated to a specific player
     * @param p is the player owner of the dashboard
     * */
    public DashboardView(Player p, ArrayList<Player> players) {
        this.player = p;
        this.players = players;
    }

    /**
     * This will print out all the components of the multiplayer dashboard
     */
    public void visualizePerMulti() {
        ViewCLI v = new ViewCLI();
        System.out.println("This is your DASHBOARD:");
        visualizeFaithRoute(player.getDashboard().getFaithRoute());
        System.out.println("--------------------------------------------------");
        visualizeStorage(player.getDashboard().getStorage());
        System.out.println("\n\n--------------------------------------------------");
        visualizeCoffer(player.getDashboard().getCoffer());
        System.out.println("--------------------------------------------------");
        v.PrintSlotsForDashboard(player);
        System.out.println("\n\n--------------------------------------------------");
    }

    /**
     * This will print out all the components of the single player dashboard
     */
    public void visualizePerSingle() {
        ViewCLI v = new ViewCLI();
        System.out.println("This is your DASHBOARD:");
        visualizeFaithRouteSingle(player.getDashboard().getFaithRoute());
        System.out.println("--------------------------------------------------");
        visualizeStorage(player.getDashboard().getStorage());
        System.out.println("--------------------------------------------------");
        visualizeCoffer(player.getDashboard().getCoffer());
        System.out.println("--------------------------------------------------");
        v.PrintSlotsForDashboard(player);
        System.out.println("\n\n--------------------------------------------------");
    }

    /**
     * Method to visualize the faithroute
     */
    public void visualizeFaithRoute(FaithRoute faithRoute){
        System.out.println("\n\n" + Colour.ANSI_MAGENTA + "** " + Colour.RESET + "FAITH ROUTE " + Colour.ANSI_MAGENTA + "**\n" + Colour.RESET);
        System.out.print("         ");
        for (int i=0; i<faithRoute.getRoute().size(); i++){
            if (faithRoute.getRoute().get(i).getReportBox() != 0 && !faithRoute.getRoute().get(i).isPopeBox())
                System.out.print(Colour.ANSI_ORANGE + BOX + Colour.RESET);
            else if (faithRoute.getRoute().get(i).getReportBox() != 0 && faithRoute.getRoute().get(i).isPopeBox())
                System.out.print(Colour.ANSI_RED + BOX + Colour.RESET);
            else
                System.out.print(BOX);
        }
        System.out.println();

                //Prints faithPointer of the player who has begin the turn
                System.out.print("YOU      ");
                for (int i = 0; i <= player.getDashboard().getFaithRoute().getFaithIndicator(); i++) {
                    if (i == player.getDashboard().getFaithRoute().getFaithIndicator())
                        System.out.println(Colour.ANSI_RED + FAITHINDICATOR + Colour.RESET);
                    else
                        System.out.print("  ");
                }

        //Prints faithPointer of other players in the game

        for (int j=0; j<players.size(); j++){
                    if (players.get(j).getNumber() != player.getNumber()){
                        if (players.get(j).getName().length()<8) {
                            System.out.print(players.get(j).getName().substring(0,players.get(j).getName().length()));
                            for (int k = 0 ; k<(8 - players.get(j).getName().length()); k++){
                                System.out.print(" ");
                            }
                            System.out.print(" ");
                        } else {
                            System.out.print(players.get(j).getName().substring(0,8));
                            System.out.print(" ");
                        }
                        for (int i = 0; i <= players.get(j).getDashboard().getFaithRoute().getFaithIndicator(); i++) {
                            if (i == players.get(j).getDashboard().getFaithRoute().getFaithIndicator())
                                System.out.println(Colour.ANSI_RED + FAITHINDICATOR + Colour.RESET);
                            else
                                System.out.print("  ");
                        }
                    }
                }
        System.out.println();

    }

    /**
     * Method to visualize the faithroute for singleplayer
     */
    public void visualizeFaithRouteSingle(FaithRoute faithRoute){
        System.out.println("\n\n" + Colour.ANSI_MAGENTA + "** " + Colour.RESET + "FAITH ROUTE " + Colour.ANSI_MAGENTA + "**\n" + Colour.RESET);
        System.out.print("    ");
        for (int i=0; i<faithRoute.getRoute().size(); i++){
            if (faithRoute.getRoute().get(i).getReportBox() != 0 && !faithRoute.getRoute().get(i).isPopeBox())
                System.out.print(Colour.ANSI_ORANGE + BOX + Colour.RESET);
            else if (faithRoute.getRoute().get(i).getReportBox() != 0 && faithRoute.getRoute().get(i).isPopeBox())
                System.out.print(Colour.ANSI_RED + BOX + Colour.RESET);
            else
                System.out.print(BOX);
        }
        System.out.println();

        System.out.print("YOU ");
        for (int i = 0; i <= player.getDashboard().getFaithRoute().getFaithIndicator(); i++) {
            if (i == player.getDashboard().getFaithRoute().getFaithIndicator())
                System.out.println(Colour.ANSI_RED + FAITHINDICATOR + Colour.RESET);
            else
                System.out.print("  ");
        }

        System.out.print("LM  ");
        for (int i=0; i<=faithRoute.getFaithIndicatorLorenzo(); i++){
            if (i==faithRoute.getFaithIndicatorLorenzo())
                System.out.println(Colour.ANSI_GREY + FAITHINDICATOR + Colour.RESET);
            else
                System.out.print("  ");
        }
        System.out.println();
    }

    /**
     * Method to print the storage
     */
    public void visualizeStorage(Storage s){
        System.out.println("\n" + Colour.ANSI_MAGENTA + "** " + Colour.RESET + "STORAGE " + Colour.ANSI_MAGENTA + "**\n" + Colour.RESET);
        ArrayList<Resource> level;
        level = s.getLvl1();
        if (s.getLvl1().isEmpty() && s.getLvl2().isEmpty() && s.getLvl3().isEmpty()) {
            System.out.println("STORAGE IS EMPTY");
        }
        else {
            if (!s.getLvl1().isEmpty()){
                System.out.print("1 ->  ");
                PrintResource(level.get(0).getResourcename());
            }
            else
                System.out.print("1 -> EMPTY");
            if (!s.getLvl2().isEmpty()) {
                level = s.getLvl2();
                System.out.print("\n\n2 ->  ");
                for (int i = 0; i < level.size(); i++) {
                    PrintResource(level.get(i).getResourcename());
                    System.out.print("  ");
                }
            }
            else
                System.out.print("\n\n2 -> EMPTY");
            if (!s.getLvl3().isEmpty()) {
                level = s.getLvl3();
                System.out.print("\n\n3 ->  ");
                for (int i = 0; i < level.size(); i++) {
                    PrintResource(level.get(i).getResourcename());
                    System.out.print("  ");
                }
                System.out.println("\n");
            }
            else
                System.out.print("\n\n3 -> EMPTY\n");
        }

        visualizeExtraDeposit();
    }

    /**
     * Method to visualize the extraDeposit
     */
    private void visualizeExtraDeposit(){
        int c=0;
        for (int i=0; i<player.getLeaderCardsOfPlayer().size(); i++) {
            if (player.getLeaderCardsOfPlayer().get(i) instanceof ExtraDeposit && ((ExtraDeposit) player.getLeaderCardsOfPlayer().get(i)).isActive()) {
                if (c == 0) {
                    System.out.println("\n" + Colour.ANSI_MAGENTA + "** " + Colour.RESET + "EXTRA STORAGE " + Colour.ANSI_MAGENTA + "**" + Colour.RESET);
                    c++;
                }
                if (((ExtraDeposit) player.getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins() != null && ((ExtraDeposit) player.getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins().size() > 0) {
                    System.out.print("\nCOINS -> ");
                    for (int j = 0; j < ((ExtraDeposit) player.getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins().size(); j++) {
                        PrintResource("coin");
                    }
                }

                else if (((ExtraDeposit) player.getLeaderCardsOfPlayer().get(i)).getTwoExtraServants() != null && ((ExtraDeposit) player.getLeaderCardsOfPlayer().get(i)).getTwoExtraServants().size() > 0){
                    System.out.print("\nSERVANTS -> ");
                    for (int j = 0; j < ((ExtraDeposit) player.getLeaderCardsOfPlayer().get(i)).getTwoExtraServants().size(); j++) {
                        PrintResource("servant");
                    }
                }

                else if (((ExtraDeposit) player.getLeaderCardsOfPlayer().get(i)).getTwoExtraShields() != null && ((ExtraDeposit) player.getLeaderCardsOfPlayer().get(i)).getTwoExtraShields().size() > 0){
                    System.out.print("\nSHIELDS -> ");
                    for (int j = 0; j < ((ExtraDeposit) player.getLeaderCardsOfPlayer().get(i)).getTwoExtraShields().size(); j++) {
                        PrintResource("shield");
                    }
                }

                else if (((ExtraDeposit) player.getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks() != null && ((ExtraDeposit) player.getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks().size() > 0){
                    System.out.print("\nSTONES -> ");
                    for (int j = 0; j < ((ExtraDeposit) player.getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks().size(); j++) {
                        PrintResource("stone");
                    }
                }

            }
        }
        System.out.println("\n");

    }

    /**
     * Method to print Resources
     */
    private void PrintResource(String res){
        switch (res) {
            case "coin" -> System.out.print(Colour.ANSI_YELLOW + Arrays.toString(COIN) + Colour.RESET);
            case "stone" -> System.out.print(Colour.ANSI_GREY + Arrays.toString(STONE) + Colour.RESET);
            case "servant" -> System.out.print(Colour.ANSI_PURPLE + Arrays.toString(SERVANT) + Colour.RESET);
            case "shield" -> System.out.print(Colour.ANSI_BLUE + Arrays.toString(SHIELD) + Colour.RESET);
        }
    }


    /**
     * Method to visualize the coffer
     * coffer took from the player instance
     */
    public void visualizeCoffer(Coffer c) {
        System.out.println("\n" + Colour.ANSI_MAGENTA + "** " + Colour.RESET + "COFFER " + Colour.ANSI_MAGENTA + "**\n" + Colour.RESET);
        System.out.println("Coins" + Colour.ANSI_YELLOW + Arrays.toString(COIN) + Colour.RESET + " : " + c.numberOf("coin") + ",");
        System.out.println("Servants" + Colour.ANSI_PURPLE + Arrays.toString(SERVANT) + Colour.RESET + " : " + c.numberOf("servant") + ",");
        System.out.println("Shields" + Colour.ANSI_BLUE + Arrays.toString(SHIELD) + Colour.RESET + " : " + c.numberOf("shield") + ",");
        System.out.println("Stones" + Colour.ANSI_GREY + Arrays.toString(STONE) + Colour.RESET + " : " + c.numberOf("stone") + ",");
        System.out.println();
    }

    /**
     * Method to get the array of players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }
}
