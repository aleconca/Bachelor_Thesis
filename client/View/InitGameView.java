package it.polimi.ingsw.client.View;

import it.polimi.ingsw.server.Model.LeaderCard;
import it.polimi.ingsw.server.Model.Player;

import java.util.ArrayList;
import java.util.Scanner;

public class InitGameView {
    private String[] resourcesChosen = new String[2];
    private boolean hasFaithPoint = false;

    public String[] getResourcesChosen() {
        return resourcesChosen;
    }

    private ArrayList<Integer> leaderCardsChosen = new ArrayList<>(2);

    /**
     * Setter for leaderCards chosen by the player
     */
    public void setLeaderCardsChosen(ArrayList<Integer> leaderCardsChosen) {
        this.leaderCardsChosen = leaderCardsChosen;
    }

    /**
     *Player chooses one (or two) resources and gets faithpoints before the game starts
     */
    public void initGameResources(Player player) {
        Scanner scanner = new Scanner(System.in);
        String input;

        if (player.getNumber()==1) {
            System.out.println("YOU ARE FIRST PLAYER ! NEITHER RESOURCES NOR FAITHPOINTS :(");
        }
        else if (player.getNumber() == 2 || player.getNumber()==3) {
            do {
                System.out.println(Colour.RESET + "CHOOSE" + Colour.ANSI_RED + " 1 " + Colour.RESET + "RESOURCE : " + "TYPE " + Colour.ANSI_RED + "coin" +
                        Colour.RESET + " - " + Colour.ANSI_RED +  "stone" + Colour.RESET + " - " + Colour.ANSI_RED + "servant"  +
                        Colour.RESET + " - " + Colour.ANSI_RED +  "shield" + Colour.RESET);
                input = scanner.next();
                if (goodRes(input)) {
                    resourcesChosen[0] = input;
                }
                else
                    System.out.println(Colour.RESET + "RETRY...");
            } while (!goodRes(input));

            if (player.getNumber() == 3) {
                hasFaithPoint=true;
            }
        } else if (player.getNumber() == 4) {
            int counter = 0;
            do {
                System.out.println(Colour.RESET + "CHOOSE" + Colour.ANSI_RED + " 2 " + Colour.RESET + "RESOURCE : " + "TYPE " + Colour.ANSI_RED + "coin " +
                        Colour.RESET + "- " + Colour.ANSI_RED +  "stone" + Colour.RESET + " - " + Colour.ANSI_RED + "servant"  +
                        Colour.RESET + " - " + Colour.ANSI_RED +  "shield" + Colour.RESET);
                input = scanner.next();
                if (goodRes(input)) {
                    resourcesChosen[counter]=input;
                    counter++;
                }
                else System.out.println("RETRY...");
            } while (counter<=1);
            hasFaithPoint=true;
        }
    }

    /**
     * Returns the resource as a string
     */
    private boolean goodRes(String in){
        return in.equals("coin") || in.equals("shield") || in.equals("servant") || in.equals("stone");
    }

    /**
     * Method to choose leadercards at the beginning of the game
     */
    public void chooseLeaderCards(ArrayList<LeaderCard> leaderCards) {

        LeaderCardView leaderCardView = new LeaderCardView();
        int n=0,m=0;
        boolean stop=false;
        boolean passo1=true;
        ArrayList<Integer> num = new ArrayList<>(2);
        Scanner scanner = new Scanner(System.in);

        for (LeaderCard leaderCard : leaderCards) {
            leaderCardView.printLeaderCard(leaderCard);
        }

        System.out.println("\n" + Colour.RESET + "CHOOSE" + Colour.ANSI_RED + " TWO "  + Colour.RESET + "OF THESE CARDS" +
                " ( TYPE " + Colour.ANSI_RED + "1 "+ Colour.RESET + "- " + Colour.ANSI_RED + "2"
                +Colour.RESET + " - " + Colour.ANSI_RED + "3" + Colour.RESET + " - " + Colour.ANSI_RED+ "4 " + Colour.RESET + ") : ");

        //Player choose 2 leaderCards
        do {
            if (passo1) {
                n = scanner.nextInt();
                if (n > 0 && n < 5) {
                    num.add(n);
                    passo1=false;
                } else
                    System.out.println("RETRY...");
            }
            if (!passo1){
                m = scanner.nextInt();
                if ((m > 0 && m < 5) && m!=n) {
                    num.add(m);
                    stop=true;
                } else
                    System.out.println("RETRY...");
            }
        }while(!stop);

        System.out.println("YOU CHOSE CARD " + Colour.ANSI_RED + num.get(0) + Colour.RESET +" AND CARD " + Colour.ANSI_RED + num.get(1) + Colour.RESET);

        setLeaderCardsChosen(num);
    }

    /**
     * Getter for leaderCards chosen
     */
    public ArrayList<Integer> getLeaderCardsChosen() {
        return leaderCardsChosen;
    }
}
