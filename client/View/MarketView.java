package it.polimi.ingsw.client.View;

import it.polimi.ingsw.server.Model.Marble;
import it.polimi.ingsw.server.Model.Market;

import java.util.Arrays;
import java.util.Scanner;

public class MarketView {

    private int RorC = 0;
    private int number = 0;
    Scanner scanner = new Scanner(System.in);

    private final String[] MARBLE = {"\u25cf"};

    /**
     * This method prints the updated Market
     * took from "Game" class
     * is true if it has to print just the market;
     * is false if the player chose to go to the market (so it will print questions and scanner answers)
     * */
    public MarketView(Market market, boolean isStart) {
        System.out.println(Colour.ANSI_RED + "\n**" + Colour.RESET + " MARKET " +Colour.ANSI_RED+"**" + Colour.RESET);
        for (int i = 0; i < 3; i++) {
            System.out.println();
            for (int j = 0; j < 4; j++) {
                PrintColoredMarble(market.getMat()[i][j]);
            }
        }
        System.out.print("\n\n" + "TR: ");
        PrintColoredMarble(market.getTR());
        System.out.println();

        if (!isStart) {
            chooseRorC();
        }
    }

    /**
     * Algorithm to assign a specific color to marbles
     */
    private void PrintColoredMarble(Marble.color c){
        if (c == Marble.color.y)
            System.out.print(Colour.ANSI_YELLOW + Arrays.toString(MARBLE) + Colour.RESET);
        else if (c== Marble.color.r)
            System.out.print(Colour.ANSI_RED + Arrays.toString(MARBLE) + Colour.RESET);
        else if (c == Marble.color.w)
            System.out.print(Colour.ANSI_WHITE + Arrays.toString(MARBLE) + Colour.RESET);
        else if (c == Marble.color.b)
            System.out.print(Colour.ANSI_BLUE + Arrays.toString(MARBLE) + Colour.RESET);
        else if (c == Marble.color.p)
            System.out.print(Colour.ANSI_PURPLE + Arrays.toString(MARBLE) + Colour.RESET);
        else if (c == Marble.color.g)
            System.out.print(Colour.ANSI_GREY + Arrays.toString(MARBLE) + Colour.RESET);
        else
            System.out.println("Errore");
    }

    /**
     * Method to choose a row or a coloumn
     */
    private void chooseRorC() {
        System.out.println("Okay, then choose if you want to take a row [R] or column [C]:");
        String s;
        do {
            s=scanner.next();
            if(s.equals("R") || s.equals("r")) RorC=1;
            else if(s.equals("C") || s.equals("c")) RorC=2;
            else System.out.println("Not valid! Please type 'R' for Row or 'C' for Column [not Case-Sensitive]");
        } while (RorC==0);
        chooseNumber();
    }

    /**
     * Method to choose the number of the row or column selected
     */
    private void chooseNumber() {
        System.out.print("Now choose which ");
        if(RorC==1) {
            System.out.println("row: [1, 2, 3]");
            int i;
            do {
                i = scanner.nextInt();
                if(i==1 || i==2 || i==3) {
                    number=i;
                    System.out.println("You chose the row number " + number);
                }
                else {
                    System.out.print("Number not valid! Please enter '1' or '2' or '3':  ");
                }
            } while (number==0);
        }
        else if(RorC==2) {
            System.out.println("column: [1, 2, 3, 4]");
            int i;
            do {
                i = scanner.nextInt();
                if(i==1 || i==2 || i==3 || i==4){
                    number=i;
                    System.out.println("You chose the column number " + number);
                }
                else {
                    System.out.println("Number not valid! Please enter '1' or '2' or '3' or '4' ");
                }
            } while (number==0);
        }
        else System.out.println("Error");

    }

    /**
     * Method to take the marbles and confirm the choice
     */
    public boolean sureMessage(Market m){
        System.out.print("\nFinally you chose these marbles: ");
        if(RorC==1){
            for (Marble.color row: m.getColorsRow(number-1, m.getMat())) {
                PrintColoredMarble(row);
            }
        }
        else if(RorC==2){
            for (Marble.color col: m.getColorsColumn(number-1, m.getMat())) {
                PrintColoredMarble(col);
            }
        }
        else System.out.println("Error");

        System.out.println("\nDo you want to take these marbles? [Y]es or [N]o ");
        String r;
         do {
             r = scanner.next();
             if(r.equals("Y") || r.equals("y")) return true;
             else if(r.equals("N") || r.equals("n")) return false;
             else System.out.println("Input invalid. Please type 'Y' or 'N' ");
        } while (true);
    }

    /**
     * Getter for row or column
     */
    public int getRorC() {
        return RorC;
    }

    /**
     * Getter for number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Setter for number
     */
    public void setNumber(int number) {
        this.number = number;
    }
}
