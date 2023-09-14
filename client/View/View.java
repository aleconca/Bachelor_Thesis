package it.polimi.ingsw.client.View;

import it.polimi.ingsw.Exceptions.ArrayDontFitException;
import it.polimi.ingsw.Exceptions.IncorrectMoveException;
import it.polimi.ingsw.server.Controller.Game;
import it.polimi.ingsw.server.Model.*;

import java.io.IOException;
import java.util.ArrayList;

public interface View {

    /**
     * Method to choose 2 leaderCards and possibly resources at the beginning of the game
     */
    void initGame(Player player,ArrayList<LeaderCard> cards) throws IncorrectMoveException, ArrayDontFitException, IOException;

    /**
     * The start of each turn; user can choose the action to do as first
     * the enum/string representing the action chosen
     * */
    void startTurn(Market market, DevelopCardDeck developCardDeck, Player player) throws IOException;

    /**
     * Method to show the devCards grid to the player
     */
    void ShowDevCards(ArrayList<DevelopCard> lvl1, ArrayList<DevelopCard> lvl2, ArrayList<DevelopCard> lvl3, Player player) throws IOException;

    /**
     * Method to print the last devCard for each slot
     */
    void PrintDevCards(Player player);

    /**
     * Print a success message
     */
    void DevCardSuccess(String toPrint);

    /**
     * Print the market
     */
    void ShowMarket(Market market);

    /**
     * Array of strings that will be used server side to remove the resources from the palyer
     */
    public ArrayList<String> DevelopCardProduction();

    /**
     * Verifies input and returns array basic with the string resource added
     **/
    public ArrayList<String> VerifyInput(String s, ArrayList<String> basic);

    /**
     * Method to print the dashboard for singlePlayer
     */
    void ShowDashboardSingle(Player player);

    /**
     * Method to print the actionSignal
     */
    void ShowActionSignal(ActionSignal actionSignal);

    /**
     * To print an error message
     */
    void printError(String error);

    /**
     *  Print the last develop card for each slot
     */
    int PrintSlots(Player player);

    /**
     * Method to verify whether the slot is empty or contains a card
     */
    public void VerifyEmptySlot(ArrayList<DevelopCard> slotx, int slotNum);

    /**
     * Method to ask a slot to the player
     */
    public ArrayList<Integer> AskSlot(Player player);

    /**
     * Method to activate the Devcard production
     */
    void ActivateProduction(Player player) throws IOException;

    /**
     * Method to activate the LeaderCard production
     */
    void ActivateLeaderProduction(Player player, Resource resource) throws IOException;

    /**
     * Method to activate 2 LeaderCards production in the same turn
     */
    public void ActivateDoubleLeaderProd(Player player, ArrayList<Resource> resource) throws IOException;

    /**
     * Method to print if occurs an error in the slot
     */
    void SlotError(ArrayList<DevelopCard> lvl1, ArrayList<DevelopCard> lvl2, ArrayList<DevelopCard> lvl3,
                   int chosenIndex, int chosenSlotIndex,Player player) throws IOException;

    /**
     * Print which player has the turn active
     */
    void WaitForYourTurn(String string);

    /**
     * Method that prints the result of the game
     */
    void EndGame(ArrayList<Player> players, ArrayList<Integer> finalPoints, String winner);

    /**
     * Method to print the dashboard for Multiplayer
     */
    void ShowDashboardMulti(Player player, ArrayList<Player> players);}
