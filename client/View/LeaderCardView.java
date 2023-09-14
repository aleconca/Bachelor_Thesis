package it.polimi.ingsw.client.View;

import it.polimi.ingsw.server.Model.*;
import java.util.ArrayList;

public class LeaderCardView {

    private final String REQ = "\u25a0";
    private final String COIN = "\u00a9";
    private final String STONE = "\u25cf";
    private final String SHIELD = "\u25a0";
    private final String SERVANT = "\u263a";
    private final String MARBLE = "\u25cf";
    private final String FAITHPOINT = "\u002b";
    private final String RES = "\uD83C\uDD81";

    /**
     * Method to print an array of LeaderCards
     */
    public void visualize(Object leaderCards) {
        ArrayList<LeaderCard> cardArray = (ArrayList<LeaderCard>) leaderCards;
        printLeaderCard(cardArray.get(0));
        printLeaderCard(cardArray.get(1));
    }


    /**
     * Method to print a LeaderCard
     */
    public void printLeaderCard(LeaderCard leaderCard){
        System.out.println(Colour.ANSI_RED + "__________________");
        System.out.println("| "  + " LEADER CARD" + "   |");
        System.out.print("| ");
        if (leaderCard.getResourcesreq() == null) {
            printReqColor(leaderCard, leaderCard.getRequirements().size());
        }
        else {
            printReqRes(leaderCard, leaderCard.getResourcesreq().size());
            System.out.println(Colour.ANSI_RED + "             |");
        }
        System.out.println("|                |");
        System.out.println("|    VP: " + Colour.ANSI_YELLOW + leaderCard.getVictory_points() + Colour.ANSI_RED + "       |");
        System.out.println("|                |");
        if (leaderCard instanceof SaleProductionStone || leaderCard instanceof SaleProductionCoin ||
                leaderCard instanceof SaleProductionShield || leaderCard instanceof SaleProductionServant)
            System.out.print("|    -1 ");

        printRes(leaderCard);
        System.out.println("__________________");
    }

    /**
     * Method to print the color requirements for a LeaderCard
     */
    private void printReqColor(LeaderCard leaderCard , int size){
        if (size ==2) {
            for (int x = 0; x < size; x++) {
                if (leaderCard.getRequirements().get(x) == DevelopCard.Colour.B){
                    System.out.print(Colour.ANSI_BLUE + REQ + " "  + Colour.RESET);
                    if (x==size-1)
                        System.out.println(Colour.ANSI_RED + "            |");}
                else if (leaderCard.getRequirements().get(x) == DevelopCard.Colour.Y){
                    System.out.print(Colour.ANSI_YELLOW + REQ + " "  + Colour.RESET);
                    if (x==size-1)
                        System.out.println(Colour.ANSI_RED + "            |");}
                else if (leaderCard.getRequirements().get(x) == DevelopCard.Colour.P){
                    System.out.print(Colour.ANSI_PURPLE + REQ + " "  + Colour.RESET);
                    if (x==size-1)
                        System.out.println(Colour.ANSI_RED + "            |");}
                else{
                    System.out.print(Colour.ANSI_GREEN + REQ + " "  + Colour.RESET);
                    if (x==size-1)
                        System.out.println(Colour.ANSI_RED + "            |");}
            }
        }
        else if (size == 3) {
            for (int x = 1; x < size; x++) {
                if (leaderCard.getRequirements().get(x) == DevelopCard.Colour.B) {
                    if (x == 1)
                        System.out.print(Colour.ANSI_BLUE + "2 ");
                    System.out.print(Colour.ANSI_BLUE + REQ + " " + Colour.RESET);
                    if (x == size - 1)
                        System.out.println(Colour.ANSI_RED + "          |");
                } else if (leaderCard.getRequirements().get(x) == DevelopCard.Colour.Y) {
                    if (x == 1)
                        System.out.print(Colour.ANSI_YELLOW + "2 ");
                    System.out.print(Colour.ANSI_YELLOW + REQ + " "  + Colour.RESET);
                    if (x == size - 1)
                        System.out.println(Colour.ANSI_RED + "          |");
                } else if (leaderCard.getRequirements().get(x) == DevelopCard.Colour.P) {
                    if (x == 1)
                        System.out.print(Colour.ANSI_PURPLE + "2 ");
                    System.out.print(Colour.ANSI_PURPLE + REQ + " "  + Colour.RESET);
                    if (x == size - 1)
                        System.out.println(Colour.ANSI_RED + "          |");
                } else {
                    if (x == 1)
                        System.out.print(Colour.ANSI_GREEN + "2 ");
                    System.out.print(Colour.ANSI_GREEN + REQ + " "  + Colour.RESET);
                    if (x == size - 1)
                        System.out.println(Colour.ANSI_RED + "          |");
                }
            }
        }
        else if (size==1){
            if (leaderCard.getRequirements().get(0) == DevelopCard.Colour.B){
                System.out.print("LVL 2 ");
                System.out.print(Colour.ANSI_BLUE + REQ + Colour.RESET);
                System.out.println(Colour.ANSI_RED + "        |");
            }
            else if (leaderCard.getRequirements().get(0) == DevelopCard.Colour.Y){
                System.out.print("LVL 2 ");
                System.out.print(Colour.ANSI_YELLOW + REQ + Colour.RESET);
                System.out.println(Colour.ANSI_RED + "        |");
            }
            else if (leaderCard.getRequirements().get(0) == DevelopCard.Colour.P){
                System.out.print("LVL 2 ");
                System.out.print(Colour.ANSI_PURPLE + REQ + Colour.RESET);
                System.out.println(Colour.ANSI_RED + "        |");
            }
            else if (leaderCard.getRequirements().get(0) == DevelopCard.Colour.G){
                System.out.print("LVL 2 ");
                System.out.print(Colour.ANSI_GREEN + REQ + Colour.RESET);
                System.out.println(Colour.ANSI_RED + "        |");
            }
        }
    }

    /**
     * Method to print the resource requirements for a LeaderCard
     */
    private void printReqRes(LeaderCard leaderCard, int size){
        System.out.print(size);
        switch (leaderCard.getResourcesreq().get(0).getResourcename()) {
            case "coin" -> System.out.print(Colour.ANSI_YELLOW + COIN + Colour.RESET);
            case "stone" -> System.out.print(Colour.ANSI_GREY + STONE + Colour.RESET);
            case "shield" -> System.out.print(Colour.ANSI_BLUE + SHIELD + Colour.RESET);
            case "servant" -> System.out.print(Colour.ANSI_PURPLE + SERVANT + Colour.RESET);
        }
    }

    /**
     * Method to print the ability of a LeaderCard
     */
    private void printRes(LeaderCard leaderCard){
        if (leaderCard instanceof SaleProductionCoin){
            System.out.print(Colour.ANSI_YELLOW + COIN + Colour.RESET);
            System.out.println(Colour.ANSI_RED + "        |");}
        else if (leaderCard instanceof SaleProductionServant){
            System.out.print(Colour.ANSI_PURPLE + SERVANT + Colour.RESET);
            System.out.println(Colour.ANSI_RED + "        |");}
        else if (leaderCard instanceof SaleProductionShield){
            System.out.print(Colour.ANSI_BLUE + SHIELD + Colour.RESET);
            System.out.println(Colour.ANSI_RED + "        |");}
        else if (leaderCard instanceof SaleProductionStone){
            System.out.print(Colour.ANSI_GREY + STONE + Colour.RESET);
            System.out.println(Colour.ANSI_RED + "        |");}
        else if (leaderCard instanceof ExtraDeposit){
            if ( ((ExtraDeposit) leaderCard).getTwoExtraCoins() != null) {
                System.out.print(Colour.ANSI_RED + "| ");
                System.out.print(Colour.ANSI_RED + " 2 EXTRA " + Colour.ANSI_YELLOW + COIN);
                System.out.println(Colour.ANSI_RED + "     |");
            }
            else if (((ExtraDeposit) leaderCard).getTwoExtraRocks() != null) {
                System.out.print(Colour.ANSI_RED + "| ");
                System.out.print(Colour.ANSI_RED + " 2 EXTRA " + Colour.ANSI_GREY + STONE);
                System.out.println(Colour.ANSI_RED + "     |");
            }
            else if (((ExtraDeposit) leaderCard).getTwoExtraServants() != null) {
                System.out.print(Colour.ANSI_RED + "| ");
                System.out.print(Colour.ANSI_RED + " 2 EXTRA " + Colour.ANSI_PURPLE + SERVANT);
                System.out.println(Colour.ANSI_RED + "     |");
            }
            else if (((ExtraDeposit) leaderCard).getTwoExtraShields() != null) {
                System.out.print(Colour.ANSI_RED + "| ");
                System.out.print(Colour.ANSI_RED + " 2 EXTRA " + Colour.ANSI_BLUE + SHIELD);
                System.out.println(Colour.ANSI_RED + "     |");
            }
        }
        else if (leaderCard instanceof WhiteMarble){
            if (((WhiteMarble) leaderCard).getColor() == Marble.color.b) {
                System.out.print(Colour.ANSI_RED + "|    ");
                System.out.print(Colour.RESET + MARBLE + " = " + Colour.ANSI_BLUE + SHIELD);
                System.out.println(Colour.ANSI_RED + "       |");}
            else if (((WhiteMarble) leaderCard).getColor() == Marble.color.y){
                System.out.print(Colour.ANSI_RED + "|    ");
                System.out.print(Colour.RESET + MARBLE + " = " + Colour.ANSI_YELLOW + COIN);
                System.out.println(Colour.ANSI_RED + "       |");}
            else if (((WhiteMarble) leaderCard).getColor() == Marble.color.p){
                System.out.print(Colour.ANSI_RED + "|    ");
                System.out.print(Colour.RESET + MARBLE + " = " + Colour.ANSI_PURPLE + SERVANT);
                System.out.println(Colour.ANSI_RED + "       |");}
            else if (((WhiteMarble) leaderCard).getColor() == Marble.color.g){
                System.out.print(Colour.ANSI_RED + "|    ");
                System.out.print(Colour.RESET + MARBLE + " = " + Colour.ANSI_GREY + STONE);
                System.out.println(Colour.ANSI_RED + "       |");}
        }
        else if (leaderCard instanceof UpgradedProduction) {
            if (leaderCard.getRequirements().get(0) == DevelopCard.Colour.B) {
                System.out.print(Colour.ANSI_RED + "|  ");
                System.out.print(Colour.ANSI_PURPLE + SERVANT + Colour.RESET);
                System.out.print(Colour.ANSI_RED + " -> " + Colour.RESET + RES + Colour.ANSI_RED + "  1" + FAITHPOINT);
                System.out.println(Colour.ANSI_RED + "   |");
            } else if (leaderCard.getRequirements().get(0) == DevelopCard.Colour.G) {
                System.out.print(Colour.ANSI_RED + "|  ");
                System.out.print(Colour.ANSI_YELLOW + COIN + Colour.RESET);
                System.out.print(Colour.ANSI_RED + " -> " + Colour.RESET + RES + Colour.ANSI_RED + "  1" + FAITHPOINT);
                System.out.println(Colour.ANSI_RED + "   |");
            } else if (leaderCard.getRequirements().get(0) == DevelopCard.Colour.P) {
                System.out.print(Colour.ANSI_RED + "|  ");
                System.out.print(Colour.ANSI_GREY + STONE + Colour.RESET);
                System.out.print(Colour.ANSI_RED + " -> " + Colour.RESET + RES + Colour.ANSI_RED + "  1" + FAITHPOINT);
                System.out.println(Colour.ANSI_RED + "   |");
            } else if (leaderCard.getRequirements().get(0) == DevelopCard.Colour.Y) {
                System.out.print(Colour.ANSI_RED + "|  ");
                System.out.print(Colour.ANSI_BLUE + SHIELD + Colour.RESET);
                System.out.print(Colour.ANSI_RED + " -> " + Colour.RESET + RES + Colour.ANSI_RED + "  1" + FAITHPOINT);
                System.out.println(Colour.ANSI_RED + "   |");
            }
        }
    }
}
