package it.polimi.ingsw.server.Controller;

import it.polimi.ingsw.Exceptions.*;
import it.polimi.ingsw.Network.ErrorMsg.*;
import it.polimi.ingsw.Network.MessageToClient.*;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Model.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


public class Controller implements Serializable {

    private Game game;
    private ArrayList<ClientHandler> clientHandlers;
    private boolean lorenzoWin = false;

    ArrayList<Integer> VPofPlayers = new ArrayList<>(4);

    /**
     * Setter and Getter
     */
    public boolean isLorenzoWin() {
        return lorenzoWin;
    }

    public void setLorenzoWin(boolean lorenzoWin) {
        this.lorenzoWin = lorenzoWin;
    }

    public ArrayList<Integer> getVPofPlayers() {
        return VPofPlayers;
    }

    /**
     * Constructor
     */
    public Controller(Game game) {
        this.game = game;
    }

    public void setClientHandlers(ArrayList<ClientHandler> clientHandlers) {
        this.clientHandlers = clientHandlers;
    }

    /**
     * Returns the player who has the turn active
     */
    public Player PlayerTurnisTrue() {
        return game.getPlayers().get(searchTurn().getPlayernumber());
    }


    /**
     * Search for clienthandler which has the turn active
     * Used to send the message to client
     */
    private ClientHandler searchTurn(){
        for (ClientHandler c:
                clientHandlers) {
            if(c.getIsYourTurn().get()) return c;
        }
        return null;
    }

    /**
     * Method to pass the turn
     */
    private void passTurn(){
        int curr = searchTurn().getPlayernumber();
        game.getPlayers().get(curr).setBaseAction(false);
        if (curr==clientHandlers.size()-1) {
            clientHandlers.get(curr).getIsYourTurn().set(false);
            clientHandlers.get(0).getIsYourTurn().set(true);
            clientHandlers.get(0).setBeginTurn(true);
        }else{
            clientHandlers.get(curr).getIsYourTurn().set(false);
            clientHandlers.get(curr + 1).getIsYourTurn().set(true);
            clientHandlers.get(curr + 1).setBeginTurn(true);
        }
    }


    /**
     * Method called when the game starts;
     * It let the players chose their Leader Cards in the range of four (random chosen)
     * Returns 4 leaderCards
     */
    public void initGame(LeaderCardsDeck leaderCardsDeck) throws IOException {
        ArrayList<LeaderCard> cards = new ArrayList<>(4);

        for (int i =0; i<4; i++){
            int random = RandomInt(leaderCardsDeck.getLeaderCS().size());
            cards.add(leaderCardsDeck.getLeaderCS().get(random));
            leaderCardsDeck.getLeaderCS().remove(random);
        }
        InitGameMsg msg = new InitGameMsg(PlayerTurnisTrue(), cards);
        searchTurn().sendMsg(msg);

    }

    /**
     * Returns a random int in range
     */
    private int RandomInt(int range){
        return (int) (Math.random() * range);
    }
    /**
     * Method called at the start of the first turn
     */
    public void startGame() throws IOException {
        PrintEveryThingMsg msg = new PrintEveryThingMsg(game.getMarket(), PlayerTurnisTrue() , game.getPlayers());
        searchTurn().sendMsg(msg);
    }

    /**
     * Method to control if the action chosen can be done
     * It will be called if and only if the "startTurnMsg" will be send, so if the controller method isEndTurn allows it
     */
    public void action(String input) throws IOException {
        NextActionInvalidMsg nextActionInvalidMsg = new NextActionInvalidMsg(game.getMarket(), game.getDevelopCardsDeck(), PlayerTurnisTrue());
        switch (input.toLowerCase()) {
            case "m": //Market
                if(PlayerTurnisTrue().isBaseAction()) {
                    nextActionInvalidMsg.setType("Base");
                }
                else {
                    PlayerTurnisTrue().setBaseAction(true);
                    MarketActionMsg marketMsg = new MarketActionMsg(game.getMarket());
                    searchTurn().sendMsg(marketMsg);
                }
                break;

            case "d": //Buy Develop Card
                if(PlayerTurnisTrue().isBaseAction()) {
                    nextActionInvalidMsg.setType("Base");
                }
                else {
                    BuyDevelopCardMsg msgD = new BuyDevelopCardMsg(game.getDevelopCardsDeck().getLvl1(),
                            game.getDevelopCardsDeck().getLvl2(), game.getDevelopCardsDeck().getLvl3(), PlayerTurnisTrue());
                    searchTurn().sendMsg(msgD);
                }
                break;

            case "p": //Active Production
                if(PlayerTurnisTrue().isBaseAction()) {
                    nextActionInvalidMsg.setType("Base");
                } else{
                    ActivateProductionMsg msgP = new ActivateProductionMsg(PlayerTurnisTrue());

                    searchTurn().sendMsg(msgP);

                    LeaderProduction();
                }
                break;


            case "l": //Active Leader Card(s)
                if(PlayerTurnisTrue().isLeaderCard1() && PlayerTurnisTrue().isLeaderCard2()) {
                    nextActionInvalidMsg.setType("Leader");
                    break;
                }
                else{
                    ActivateLeaderCardMsg msg3 = new ActivateLeaderCardMsg(PlayerTurnisTrue());
                    searchTurn().sendMsg(msg3);
                }
                break;

            case "dis":
                if(PlayerTurnisTrue().isLeaderCard1() && PlayerTurnisTrue().isLeaderCard2()) {
                    nextActionInvalidMsg.setType("Leader");
                    break;
                }
                else{
                    DiscardLeaderMsg dismsg = new DiscardLeaderMsg();
                    searchTurn().sendMsg(dismsg);
                }
                break;

            case "e": //End turn
                if (PlayerTurnisTrue().isBaseAction()) {
                    isEndTurn(true);
                } else {
                    nextActionInvalidMsg.setType("End");
                }
                break;
            default:
                DefaultMsg defaultMsg = new DefaultMsg();
                searchTurn().sendMsg(defaultMsg);
                break;
        }

        if (!nextActionInvalidMsg.getType().equals("")) {
            searchTurn().sendMsg(nextActionInvalidMsg);
        }
    }

    /**
     * Method to pass turn, that checks RIV and gameover before pass the turn
     */
    public void isEndTurn(boolean wantToEnd) throws IOException {
        boolean isEndTurn = PlayerTurnisTrue().isBaseAction() && PlayerTurnisTrue().isLeaderCard1() && PlayerTurnisTrue().isLeaderCard2();
        checkRIV();

        //if the end of the match got activated
        if (!game.isGameOverGlobal()){
            if (game.getPlayers().size()>1) {
                game.setGameOverGlobal(EndGame());//check for gameover
            }
            else if (game.getPlayers().size()==1)
                game.setGameOverGlobal(EndGameSinglePlayer());

        }
        if (game.getPlayers().size()==1) {
            if (game.isGameOverGlobal()){
                SumPoints();
                searchTurn().setGameOver(true);
            }
        }
        else if (game.getPlayers().size()>1){
            if (game.isGameOverGlobal()){
                SumPoints();
                for (int i=0; i<game.getPlayers().size(); i++) {
                    searchTurn().setGameOver(true);
                    if (!clientHandlers.get(i).equals(searchTurn()))
                        clientHandlers.get(i).setGameOver(true);
                    clientHandlers.get(i).getIsYourTurn().set(true);
                }

                isEndTurn= false;
                wantToEnd = false;
            }
        }


        if(isEndTurn || wantToEnd){

            if (clientHandlers.size()>1) { //RAMO MULTI
                WaitForYourTurnMsg msg = new WaitForYourTurnMsg("Your turn ended.");
                searchTurn().sendMsg(msg);
                passTurn();
            }
            if (clientHandlers.size()==1){ //RAMO SINGLE
                if (!game.isGameOverGlobal()) {
                    WaitForYourTurnMsg msg = new WaitForYourTurnMsg("Your turn ended. Lorenzo is gonna do something...");
                    searchTurn().sendMsg(msg);
                    ActivateSignal(game);
                }
                passTurn();
            }
        } else {
            StartTurnMsg secondMsg = new StartTurnMsg(game.getMarket(), game.getDevelopCardsDeck(), PlayerTurnisTrue(), game.getPlayers());
            searchTurn().sendMsg(secondMsg);
        }


    }

    /**
     * setta le leaderCards del player, in base a quello che riceve dal client
     */
    public void initGameLeaderCards(ArrayList<Integer> num,ArrayList<LeaderCard> cards,String[] chosenResources) throws IOException {
        ArrayList<Resource> convertRes= new ArrayList<>();

        PlayerTurnisTrue().getLeaderCardsOfPlayer().add(cards.get(num.get(0)-1));
        PlayerTurnisTrue().getLeaderCardsOfPlayer().add(cards.get(num.get(1)-1));

        if(PlayerTurnisTrue().getNumber()==3 || PlayerTurnisTrue().getNumber()==4)
            PlayerTurnisTrue().getDashboard().getFaithRoute().moveIndicator(1);

        for (int i = 0 ; i< chosenResources.length; i++){
            if (chosenResources[i] != null) {
                convertRes.add(chooseRes(chosenResources[i]).get(i));
            }
        }

        PlayerTurnisTrue().getDashboard().getCoffer().addResources(convertRes);

        passTurn();

    }

    /**
     * Method that returns a resource based on the input name that receives as a parameter;
     * Used in initGameResources
     */
    private ArrayList<Resource> chooseRes(String res){
        ArrayList<Resource> risorsa = new ArrayList<>(1);
        Resource coin = new Resource("coin");
        Resource stone = new Resource("stone");
        Resource servant = new Resource("servant");
        Resource shield = new Resource("shield");

        if (res != null) {
            if (res.equals("coin")) {
                risorsa.add(coin);
                return risorsa;
            } else if (res.equals("stone")) {
                risorsa.add(stone);
                return risorsa;
            } else if (res.equals("servant")) {
                risorsa.add(servant);
                return risorsa;
            } else if (res.equals("shield")) {
                risorsa.add(shield);
                return risorsa;
            } else {
                return null;
            }
        }

        return null;
    }

    /**
     * Takes an array of resources and creates the resource indicated as a string and then adds to array
     */
    private ArrayList<Resource> chooseResVersion2(String res, ArrayList<Resource> risorsa){
        //ArrayList<Resource> risorsa = new ArrayList<>(1);
        Resource coin = new Resource("coin");
        Resource stone = new Resource("stone");
        Resource servant = new Resource("servant");
        Resource shield = new Resource("shield");
        Resource faithIndicator = new Resource("faith indicator");

        if (res != null) {
            if (res.equals("coin")) {
                risorsa.add(coin);
                return risorsa;
            } else if (res.equals("stone")) {
                risorsa.add(stone);
                return risorsa;
            } else if (res.equals("servant")) {
                risorsa.add(servant);
                return risorsa;
            } else if (res.equals("shield")) {
                risorsa.add(shield);
                return risorsa;
            } else if (res.equals("faith indicator")) {
                risorsa.add(faithIndicator);
                return risorsa;
            }
        }

        return risorsa;//ritorno array non modificato
    }


    //------------------------------------ Market Action ---------------------------------------------------------------

    /**
     * It calls transformMarbles and then changes the Market
     */
    public void updateMarket(int RorC, int num) throws IOException {
        Marble[] marbles = new Marble[4];
        Marble.color[][] oldMarket = game.getMarket().getMat();
        try {
            if(RorC == 1) {
                marbles = game.getMarket().getRow(num);
                game.getMarket().chooseRow(num);
            }
            else if (RorC == 2){
                marbles = game.getMarket().getColumn(num);
                game.getMarket().chooseColumn(num);
            }
            transformMarbles(marbles);

        } catch (InvalidRow | InvalidColumn invalid) {
            game.getMarket().setMat(oldMarket); //Restoring of Market
            MarketErrorMsg err = new MarketErrorMsg(game.getMarket(), "Invalid row or column");
            PlayerTurnisTrue().setBaseAction(false);
            searchTurn().sendMsg(err);
        } catch (InvalidInput input){
            game.getMarket().setMat(oldMarket); //restoring of Market
            MarketErrorMsg err = new MarketErrorMsg(game.getMarket(), "Invalid resource");
            PlayerTurnisTrue().setBaseAction(false);
            searchTurn().sendMsg(err);
        }
    }

    /**
     * Transform marbles into resources or faith point (moving indicator on the route)
     */
    public void transformMarbles(Marble[] marbles) throws IOException, InvalidInput {
        ArrayList<Resource> resources = new ArrayList<>(4);
        ArrayList<Marble> marbleList = new ArrayList<>(4);
        boolean whiteCardUsed = false;
        boolean isWhite = false;

        for (Marble m: marbles) {
            if (m.getCol().equals(Marble.color.w)) {
                isWhite = true;
                break;
            }
        }

        if(isWhite && numWhiteMarbleCard()==2){
            String[] resStrings = new String[2];
            for (int i = 0; i < 2; i++) {
                resStrings[i] = ((WhiteMarble) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).whichResource();
            }
            int numWhite = (int) Arrays.stream(marbles).filter(m -> m.getCol().equals(Marble.color.w)).count(); //non c'è problema a castarlo perchè al massimo sono 4
            WhiteMarbleMsg msg = new WhiteMarbleMsg(resStrings, marbles, numWhite);
            searchTurn().sendMsg(msg);
        }
        else {

            for (Marble m: marbles) {
                if (m.getCol().equals(Marble.color.w)) {
                    if (numWhiteMarbleCard() == 1) {
                        marbleList.add(((WhiteMarble) PlayerTurnisTrue().getLeaderCardsOfPlayer().stream().filter(leaderCard -> leaderCard instanceof WhiteMarble).findAny().get()).ability());
                        whiteCardUsed = true; //Ha usato la carta leader
                    }
                }
                else if (m.getCol().equals(Marble.color.r)) {
                    game.getPlayers().get(searchTurn().getPlayernumber()).getDashboard().getFaithRoute().moveIndicator(1);
                }
                else marbleList.add(m);
            }
            for (Marble m: marbleList) {
                resources.add(m.transformInRes());
            }
            SelectedResMsg msg = new SelectedResMsg(resources, "f", whiteCardUsed);
            searchTurn().sendMsg(msg);
        }
    }



    /** Counts how many "White Marble" Leader Cards ACTIVE the player has */
    private int numWhiteMarbleCard(){
        int numCards = 0;
        for (LeaderCard l: PlayerTurnisTrue().getLeaderCardsOfPlayer()) {
            if(l instanceof WhiteMarble && l.isActive()) numCards++;
        }
        return numCards;
    }


    /** Method called if and only if there are 2 WhiteMarble Leader Cards active;
     *  is the array of marbles chosen from the market
     *  is an array containing the number of the Leader Card to use on each white marble
     * */
    public void colorForWhite(Marble[] marbles, int[] cardChosen) throws IOException {
        int i=0;
        int j=0;
        Marble[] marbles1 = new Marble[4];

        for (Marble m: marbles) {
            if(m.getCol().equals(Marble.color.w)){
                m = ((WhiteMarble) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(cardChosen[i]-1)).ability();
                i++;
            }
            marbles1[j] = m;
            j++;
        }
        try {
            transformMarbles(marbles1);
        } catch (InvalidInput input) {
            MarketErrorMsg err = new MarketErrorMsg(game.getMarket(), "Invalid resource for a white marble");
            PlayerTurnisTrue().setBaseAction(false);
            searchTurn().sendMsg(err);
        }
    }


    /** Switch levels in Storage; in the meanwhile propagates the resources */
    public void switchLvls(int lv1, int lv2, ArrayList<Resource> resources) throws IOException {
        SwitchLvlsErrorMsg err = null;
        try {
            if (PlayerTurnisTrue().getDashboard().getStorage().getLvl(lv1).isEmpty() && PlayerTurnisTrue().getDashboard().getStorage().getLvl(lv2).isEmpty()) {
                err = new SwitchLvlsErrorMsg(resources, "The levels are empty!");
            } else {
                PlayerTurnisTrue().getDashboard().getStorage().switchLvls(lv1, lv2);
                SendStorageMsg msg = new SendStorageMsg(PlayerTurnisTrue().getDashboard().getStorage());
                msg.setResources(resources);
                searchTurn().sendMsg(msg);
            }
        } catch (CannotSwitchLevelsException e) {
            err = new SwitchLvlsErrorMsg(resources, "Impossible to switch these levels!");
        } catch (LevelDoNotExistsException e) {
            err = new SwitchLvlsErrorMsg(resources, "Level do not exists! You can type 1, 2 or 3");
        }
        if (err!=null) {
            searchTurn().sendMsg(err);
        }
    }

    /** Insert one resource at time */
    public void insertRes(ArrayList<Resource> resources, int level) throws IOException {
        InsertResErrorMsg err = new InsertResErrorMsg("");
        ArrayList<Resource> theResource = new ArrayList<>(1);
        theResource.add(resources.get(0));

        if (!resInExtraDep(theResource.get(0))) {
            try {
                game.getPlayers().get(searchTurn().getPlayernumber()).getDashboard().getStorage().insertResources(theResource, level);
            } catch (ArrayDontFitException e) {
                err.setError("There is no more free space!");
            } catch (LevelDoNotExistsException e) {
                err.setError("The level do not exists! Try again... ");
            } catch (IncorrectMoveException e){
                err.setError("Incorrect move!");
            }
            finally {
                if (!err.getError().equals("")) {
                    err.setResources(resources);
                    err.setAction("ce");
                    searchTurn().sendMsg(err);
                }
                else discardRes(resources, false);
            }
        } else {
            discardRes(resources, false);
        }
    }

    /**
     * Returns true if the resource is in the extradeposit
     */
    private boolean resInExtraDep(Resource res) throws IOException {
        for (LeaderCard l: PlayerTurnisTrue().getLeaderCardsOfPlayer()) {
            if(l instanceof ExtraDeposit && l.isActive()){
                if(res.getResourcename().equals(((ExtraDeposit) l).getType().getResourcename())){
                    try {
                        ((ExtraDeposit) l).addExtraResource(res);
                        return true;
                    } catch (ArrayDontFitException | ExtraDepositException e) {
                        InsertResErrorMsg errMsg = new InsertResErrorMsg(e.getMessage());
                        searchTurn().sendMsg(errMsg);
                        return false;
                    }
                }
                else return false;
            }
            else return false;
        }
        return false;
    }

    /**
     * Method to discard the first resource of the array;
     * It is called when player chooses to discard a resource
     *  or after inserting the resource in storage
     */
    public void discardRes(ArrayList<Resource> resources, boolean dis) throws IOException {
        resources.remove(0);
        if (!resources.isEmpty()) {
            SelectedResMsg resMsg = new SelectedResMsg(resources, "c", false);
            searchTurn().sendMsg(resMsg);
        } else {
            EndActionMsg endActionMsg = new EndActionMsg();
            searchTurn().sendMsg(endActionMsg);
        }
        if (dis) {
            if (game.getPlayers().size()>1) { //MULTI
                for (Player p: game.getPlayers()) {
                    if (p.getNumber()!=PlayerTurnisTrue().getNumber()) {
                        p.getDashboard().getFaithRoute().moveIndicator(1);
                    }
                }
            } else if(game.getPlayers().size()==1){ //SINGLE
                PlayerTurnisTrue().getDashboard().getFaithRoute().moveIndicatorLorenzo(1);
            }
        }
    }


    //-----------------------BuyDevCards methods------------------------------------------------------------------------

    /**method to search the develop cards in the deck(levels)**/
    public DevelopCard searchAndRemoveDevCardMethod(int chosenIndex, int x, int y, int z, int w, ArrayList<DevelopCard> lvlx,
                                                    boolean removeCard, boolean removeRes) {
         /*Grid which will be printed vertically:
                lvl3 [1-green][4-blu][7-yellow][10-purple]
                lvl2 [2-green][5-blu][8-yellow][11-purple]
                lvl1 [3-green][6-blu][9-yellow][12-purple]*/

        DevelopCard chosenCard = null;

        if (chosenIndex == x) {
            //search card lvlx green
            for (int j = 0; j < lvlx.size(); j++) {
                if (lvlx.get(j).getColour().equals(DevelopCard.Colour.G)) {
                    chosenCard = lvlx.get(j);

                    if (CheckCost( chosenCard , removeRes)) {

                        if (removeCard) {
                            if (chosenCard.getLevel()==1)
                                game.getDevelopCardsDeck().getLvl1().remove(indexOfDevCard(1, DevelopCard.Colour.G));
                            else if (chosenCard.getLevel()==2)
                                game.getDevelopCardsDeck().getLvl2().remove(indexOfDevCard(2, DevelopCard.Colour.G));
                            else if (chosenCard.getLevel()==3)
                                game.getDevelopCardsDeck().getLvl3().remove(indexOfDevCard(3, DevelopCard.Colour.G));
                        }

                        return chosenCard;
                    } else {
                        return null;//player hasn't enough resources
                    }


                }
            }

        } else if (chosenIndex == y) {
            //search card lvlx blue
            for (int j = 0; j < lvlx.size(); j++) {
                if (lvlx.get(j).getColour().equals(DevelopCard.Colour.B)) {
                    chosenCard = lvlx.get(j);

                    if (CheckCost( chosenCard, removeRes)) {

                        if (removeCard) {
                            if (chosenCard.getLevel()==1)
                                game.getDevelopCardsDeck().getLvl1().remove(indexOfDevCard(1, DevelopCard.Colour.B));
                            else if (chosenCard.getLevel()==2)
                                game.getDevelopCardsDeck().getLvl2().remove(indexOfDevCard(2, DevelopCard.Colour.B));
                            else if (chosenCard.getLevel()==3)
                                game.getDevelopCardsDeck().getLvl3().remove(indexOfDevCard(3, DevelopCard.Colour.B));
                        }

                        return chosenCard;
                    }else {
                        return null;
                    }

                }
            }


        } else if (chosenIndex == z) {
            //search card lvlx yellow
            for (int j = 0; j < lvlx.size(); j++) {
                if (lvlx.get(j).getColour().equals(DevelopCard.Colour.Y)) {
                    chosenCard = lvlx.get(j);

                    if (CheckCost( chosenCard, removeRes)) {

                        if (removeCard) {
                            if (chosenCard.getLevel()==1)
                                game.getDevelopCardsDeck().getLvl1().remove(indexOfDevCard(1, DevelopCard.Colour.Y));
                            else if (chosenCard.getLevel()==2)
                                game.getDevelopCardsDeck().getLvl2().remove(indexOfDevCard(2, DevelopCard.Colour.Y));
                            else if (chosenCard.getLevel()==3)
                                game.getDevelopCardsDeck().getLvl3().remove(indexOfDevCard(3, DevelopCard.Colour.Y));
                        }

                        return chosenCard;

                    } else {
                        return null;
                    }
                }
            }

        } else if (chosenIndex == w) {
            //search card lvlx purple
            for (int j = 0; j < lvlx.size(); j++) {
                if (lvlx.get(j).getColour().equals(DevelopCard.Colour.P)) {
                    chosenCard = lvlx.get(j);

                    if (CheckCost( chosenCard, removeRes)) {

                        if (removeCard) {
                            if (chosenCard.getLevel()==1)
                                game.getDevelopCardsDeck().getLvl1().remove(indexOfDevCard(1, DevelopCard.Colour.P));
                            else if (chosenCard.getLevel()==2)
                                game.getDevelopCardsDeck().getLvl2().remove(indexOfDevCard(2, DevelopCard.Colour.P));
                            else if (chosenCard.getLevel()==3)
                                game.getDevelopCardsDeck().getLvl3().remove(indexOfDevCard(3, DevelopCard.Colour.P));
                        }

                        return chosenCard;

                    } else {

                        return null; //cost error
                    }
                }
            }

        }

        return chosenCard;//card not found-->return null
    }

    /**
     * Returns the index of the devCard
     */
    public int indexOfDevCard(int lvl, DevelopCard.Colour col){
        if (lvl==1) {
            for (int j = 0; j < game.getDevelopCardsDeck().getLvl1().size(); j++) {
                if (col.equals(DevelopCard.Colour.G)) {
                    if (game.getDevelopCardsDeck().getLvl1().get(j).getColour().equals(DevelopCard.Colour.G))
                        return j;
                } else if (col.equals(DevelopCard.Colour.B)) {
                    if (game.getDevelopCardsDeck().getLvl1().get(j).getColour().equals(DevelopCard.Colour.B))
                        return j;
                } else if (col.equals(DevelopCard.Colour.P)) {
                    if (game.getDevelopCardsDeck().getLvl1().get(j).getColour().equals(DevelopCard.Colour.P))
                        return j;
                } else if (col.equals(DevelopCard.Colour.Y)) {
                    if (game.getDevelopCardsDeck().getLvl1().get(j).getColour().equals(DevelopCard.Colour.Y))
                        return j;
                }
            }
        }
        else if (lvl==2){
            for (int j = 0; j < game.getDevelopCardsDeck().getLvl2().size(); j++) {
                if (col.equals(DevelopCard.Colour.G)) {
                    if (game.getDevelopCardsDeck().getLvl2().get(j).getColour().equals(DevelopCard.Colour.G))
                        return j;
                } else if (col.equals(DevelopCard.Colour.B)) {
                    if (game.getDevelopCardsDeck().getLvl2().get(j).getColour().equals(DevelopCard.Colour.B))
                        return j;
                } else if (col.equals(DevelopCard.Colour.P)) {
                    if (game.getDevelopCardsDeck().getLvl2().get(j).getColour().equals(DevelopCard.Colour.P))
                        return j;
                } else if (col.equals(DevelopCard.Colour.Y)) {
                    if (game.getDevelopCardsDeck().getLvl2().get(j).getColour().equals(DevelopCard.Colour.Y))
                        return j;
                }
            }
        }
        else if (lvl==3){
            for (int j = 0; j < game.getDevelopCardsDeck().getLvl3().size(); j++) {
                if (col.equals(DevelopCard.Colour.G)) {
                    if (game.getDevelopCardsDeck().getLvl3().get(j).getColour().equals(DevelopCard.Colour.G))
                        return j;
                } else if (col.equals(DevelopCard.Colour.B)) {
                    if (game.getDevelopCardsDeck().getLvl3().get(j).getColour().equals(DevelopCard.Colour.B))
                        return j;
                } else if (col.equals(DevelopCard.Colour.P)) {
                    if (game.getDevelopCardsDeck().getLvl3().get(j).getColour().equals(DevelopCard.Colour.P))
                        return j;
                } else if (col.equals(DevelopCard.Colour.Y)) {
                    if (game.getDevelopCardsDeck().getLvl3().get(j).getColour().equals(DevelopCard.Colour.Y))
                        return j;
                }
            }
        }
        return -1;
    }

    /**searches for the chosen card and removes it from the deck if the player has the resources from the
     //deposit or storage if the player has the resources to buy the card it also removes them from his dashBoard
     //returns the chosenCArd in order to add it to the future chosen slot else null**/
    public DevelopCard BuyDevelopCard(ArrayList<DevelopCard> lvl1, ArrayList<DevelopCard> lvl2, ArrayList<DevelopCard> lvl3,  int chosenIndex,
                                      boolean removeCard, boolean removeRes) {
        DevelopCard chosenCard = null;//init
        //search & save card to remove it from lvlx and insert in card slot later

        //lvl3
        if (chosenIndex == 1 || chosenIndex == 4 || chosenIndex ==7 || chosenIndex ==10) {
            chosenCard = searchAndRemoveDevCardMethod(chosenIndex, 1, 4, 7, 10, lvl3, removeCard, removeRes);
        }


        if (chosenCard != null) {
            return chosenCard;
        }

        //lvl2
        if (chosenIndex == 2 || chosenIndex == 5 || chosenIndex ==8 || chosenIndex ==11) {
            chosenCard = searchAndRemoveDevCardMethod(chosenIndex, 2, 5, 8, 11, lvl2, removeCard, removeRes);
        }


        if (chosenCard != null) {
            return chosenCard;
        }

        //lvl1
        if (chosenIndex == 3 || chosenIndex == 6 || chosenIndex ==9 || chosenIndex ==12) {
            chosenCard = searchAndRemoveDevCardMethod(chosenIndex, 3, 6, 9, 12, lvl1, removeCard, removeRes);
        }


        if (chosenCard != null) {
            return chosenCard;
        }


        return null; //error

    }


    /**checks the cost by veryfying if a leaderCard is active
     //if true the player has the resources
     //if false the player doesn't have them**/
    public boolean CheckCost(DevelopCard chosen, boolean removeRes){
        boolean saleCoin =false ;
        boolean saleServant = false;
        boolean saleShield = false;
        boolean saleStone = false;
        Storage storage = PlayerTurnisTrue().getDashboard().getStorage();
        Coffer coffer = PlayerTurnisTrue().getDashboard().getCoffer();

        ArrayList<LeaderCard> leader = PlayerTurnisTrue().getLeaderCardsOfPlayer();

        //only if the player has the leader cards
        if (leader.size()!=0) {

            for (int i = 0; i < leader.size(); i++) {

                if (leader.get(i).isActive() && leader.get(i) instanceof SaleProductionCoin) {
                    saleCoin = true;
                } else if (leader.get(i).isActive() && leader.get(i) instanceof SaleProductionServant) {
                    saleServant = true;
                } else if (leader.get(i).isActive() && leader.get(i) instanceof SaleProductionStone) {
                    saleStone = true;
                } else if (leader.get(i).isActive() && leader.get(i) instanceof SaleProductionShield) {
                    saleShield = true;
                }
            }
        }


        if (!removeRes) {
            return checkResourcestoBuy(TotalResourcesOfPlayer(), saleStone, saleServant, saleShield, saleCoin, false, chosen);
        }
        else{
            return checkResourcestoBuy(TotalResourcesOfPlayer(), saleStone, saleServant, saleShield, saleCoin, true, chosen);
        }
    }

    /**
     *Check if the player has the resource to buy the DevCard
     * Returns true if he has the resources, else it returns false
     * The method removes resources from the player if the boolean removeRes is set to true, else its only a check without removing anything
     */
    public Boolean checkResourcestoBuy(ArrayList<Resource> playerResources, boolean StoneActive,
                                       boolean ServantActive, boolean ShieldActive, boolean CoinActive, boolean removeRes, DevelopCard card){
        int counter_s=0;
        int counter_c=0;
        int counter_ser=0;
        int counter_shield=0;
        int counter_s2=0;
        int counter_c2=0;
        int counter_ser2=0;
        int counter_shield2=0;


        for (int i = 0; i < playerResources.size(); i++) {
            if (playerResources.get(i).getResourcename().equals("stone"))
                counter_s++;
            else if (playerResources.get(i).getResourcename().equals("coin"))
                counter_c++;
            else if (playerResources.get(i).getResourcename().equals("servant"))
                counter_ser++;
            else if (playerResources.get(i).getResourcename().equals("shield"))
                counter_shield++;
        }

        for (int i = 0; i < card.getCost().size(); i++) {
            if (card.getCost().get(i).getResourcename().equals("stone"))
                counter_s2++;
            else if (card.getCost().get(i).getResourcename().equals("coin"))
                counter_c2++;
            else if (card.getCost().get(i).getResourcename().equals("servant"))
                counter_ser2++;
            else if (card.getCost().get(i).getResourcename().equals("shield"))
                counter_shield2++;
        }

        if (!StoneActive && !ServantActive && !ShieldActive && !CoinActive) {
            if (counter_s >= counter_s2 && counter_c >= counter_c2 && counter_ser >= counter_ser2 && counter_shield >= counter_shield2) {
                if (removeRes){
                    RemoveResources(card.getCost());
                }
                return true;
            }
            return false;
        }
        else {                       //Effetti LeaderCard
            if (StoneActive){
                counter_s2--;
                if (removeRes) {
                    for (int i = 0; i < card.getCost().size(); i++){
                        if (card.getCost().get(i).getResourcename().equals("stone")){
                            card.getCost().remove(i);
                            break;}
                    }
                }
            }
            if (ServantActive) {
                counter_ser2--;
                if (removeRes) {
                    for (int i = 0; i < card.getCost().size(); i++) {
                        if (card.getCost().get(i).getResourcename().equals("servant")) {
                            card.getCost().remove(i);
                            break;
                        }
                    }
                }
            }
            if (ShieldActive) {
                counter_shield2--;
                if (removeRes) {
                    for (int i = 0; i < card.getCost().size(); i++) {
                        if (card.getCost().get(i).getResourcename().equals("shield")) {
                            card.getCost().remove(i);
                            break;
                        }
                    }
                }
            }
            if (CoinActive) {
                counter_c2--;
                if (removeRes) {
                    for (int i = 0; i < card.getCost().size(); i++) {
                        if (card.getCost().get(i).getResourcename().equals("coin")) {
                            card.getCost().remove(i);
                            break;
                        }
                    }
                }
            }

            if (counter_s >= counter_s2 && counter_c >= counter_c2 && counter_ser >= counter_ser2 && counter_shield >= counter_shield2) {
                if (removeRes) {
                    RemoveResources(card.getCost());
                }
                return true;
            }
            else
                return false;
        }
    }

    /**
     * See Javadoc of upper method
     * Used for activating a develop card
     */
    public Boolean checkResourcestoProdcution(ArrayList<Resource> playerResources, boolean removeRes, DevelopCard card){
        int counter_s=0;
        int counter_c=0;
        int counter_ser=0;
        int counter_shield=0;
        int counter_s2=0;
        int counter_c2=0;
        int counter_ser2=0;
        int counter_shield2=0;


        for (int i = 0; i < playerResources.size(); i++) {
            if (playerResources.get(i).getResourcename().equals("stone"))
                counter_s++;
            else if (playerResources.get(i).getResourcename().equals("coin"))
                counter_c++;
            else if (playerResources.get(i).getResourcename().equals("servant"))
                counter_ser++;
            else if (playerResources.get(i).getResourcename().equals("shield"))
                counter_shield++;
        }

        for (int i = 0; i < card.getRequires().size(); i++) {
            if (card.getRequires().get(i).getResourcename().equals("stone"))
                counter_s2++;
            else if (card.getRequires().get(i).getResourcename().equals("coin"))
                counter_c2++;
            else if (card.getRequires().get(i).getResourcename().equals("servant"))
                counter_ser2++;
            else if (card.getRequires().get(i).getResourcename().equals("shield"))
                counter_shield2++;
        }

        if (counter_s >= counter_s2 && counter_c >= counter_c2 && counter_ser >= counter_ser2 && counter_shield >= counter_shield2) {
            if (removeRes){
                RemoveResourcesForProduction(card.getRequires());
            }
            return true;
        }
        return false;
    }

    /**
     * See Javadoc of upper method
     * Used for basic production
     */
    public Boolean checkResourcestoBasicProduction(ArrayList<Resource> playerResources, boolean removeRes, ArrayList<Resource> resReq){
        int counter_s=0;
        int counter_c=0;
        int counter_ser=0;
        int counter_shield=0;
        int counter_s2=0;
        int counter_c2=0;
        int counter_ser2=0;
        int counter_shield2=0;


        for (int i = 0; i < playerResources.size(); i++) {
            if (playerResources.get(i).getResourcename().equals("stone"))
                counter_s++;
            else if (playerResources.get(i).getResourcename().equals("coin"))
                counter_c++;
            else if (playerResources.get(i).getResourcename().equals("servant"))
                counter_ser++;
            else if (playerResources.get(i).getResourcename().equals("shield"))
                counter_shield++;
        }

        for (int i = 0; i < resReq.size(); i++) {
            if (resReq.get(i).getResourcename().equals("stone"))
                counter_s2++;
            else if (resReq.get(i).getResourcename().equals("coin"))
                counter_c2++;
            else if (resReq.get(i).getResourcename().equals("servant"))
                counter_ser2++;
            else if (resReq.get(i).getResourcename().equals("shield"))
                counter_shield2++;
        }

        if (counter_s >= counter_s2 && counter_c >= counter_c2 && counter_ser >= counter_ser2 && counter_shield >= counter_shield2) {
            if (removeRes){
                RemoveResourcesForBasicProduction(resReq);
            }
            return true;
        }
        return false;
    }

    /**
     * Return an array ( a copy ) that contains all the resources of the player contained in storage, coffer and Extradeposit
     * The arraylist returned is used as the first parameter of checkResourcetoBuy method
     */
    public ArrayList<Resource> TotalResourcesOfPlayer(){
        ArrayList<Resource> resources = new ArrayList<>();
        Resource stone = new Resource("stone");
        Resource coin = new Resource("coin");
        Resource shield = new Resource("shield");
        Resource servant = new Resource("servant");

        if (!PlayerTurnisTrue().getDashboard().getCoffer().getResources().isEmpty()){
            for (int i=0; i<PlayerTurnisTrue().getDashboard().getCoffer().getResources().size(); i++){
                if (PlayerTurnisTrue().getDashboard().getCoffer().getResources().get(i).equals(coin))
                    resources.add(coin);
                else if (PlayerTurnisTrue().getDashboard().getCoffer().getResources().get(i).equals(servant))
                    resources.add(servant);
                else if (PlayerTurnisTrue().getDashboard().getCoffer().getResources().get(i).equals(shield))
                    resources.add(shield);
                else if (PlayerTurnisTrue().getDashboard().getCoffer().getResources().get(i).equals(stone))
                    resources.add(stone);
            }
        }

        if (!PlayerTurnisTrue().getDashboard().getStorage().getLvl1().isEmpty()){
            if (PlayerTurnisTrue().getDashboard().getStorage().getLvl1().get(0).equals(coin))
                resources.add(coin);
            else if (PlayerTurnisTrue().getDashboard().getStorage().getLvl1().get(0).equals(servant))
                resources.add(servant);
            else if (PlayerTurnisTrue().getDashboard().getStorage().getLvl1().get(0).equals(shield))
                resources.add(shield);
            else if (PlayerTurnisTrue().getDashboard().getStorage().getLvl1().get(0).equals(stone))
                resources.add(stone);
        }

        if (!PlayerTurnisTrue().getDashboard().getStorage().getLvl2().isEmpty()){
            for (int i =0; i<PlayerTurnisTrue().getDashboard().getStorage().getLvl2().size(); i++){
                if (PlayerTurnisTrue().getDashboard().getStorage().getLvl2().get(i).equals(coin))
                    resources.add(coin);
                else if (PlayerTurnisTrue().getDashboard().getStorage().getLvl2().get(i).equals(servant))
                    resources.add(servant);
                else if (PlayerTurnisTrue().getDashboard().getStorage().getLvl2().get(i).equals(shield))
                    resources.add(shield);
                else if (PlayerTurnisTrue().getDashboard().getStorage().getLvl2().get(i).equals(stone))
                    resources.add(stone);
            }
        }

        if (!PlayerTurnisTrue().getDashboard().getStorage().getLvl3().isEmpty()){
            for (int i =0; i<PlayerTurnisTrue().getDashboard().getStorage().getLvl3().size(); i++){
                if (PlayerTurnisTrue().getDashboard().getStorage().getLvl3().get(i).equals(coin))
                    resources.add(coin);
                else if (PlayerTurnisTrue().getDashboard().getStorage().getLvl3().get(i).equals(servant))
                    resources.add(servant);
                else if (PlayerTurnisTrue().getDashboard().getStorage().getLvl3().get(i).equals(shield))
                    resources.add(shield);
                else if (PlayerTurnisTrue().getDashboard().getStorage().getLvl3().get(i).equals(stone))
                    resources.add(stone);
            }
        }

        if (!PlayerTurnisTrue().getLeaderCardsOfPlayer().isEmpty()) {
            for (int i =0; i<PlayerTurnisTrue().getLeaderCardsOfPlayer().size(); i++){
                if (PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i) instanceof ExtraDeposit && PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i).isActive()){
                    if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks() != null && !((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks().isEmpty()){
                        for (int k =0; k<((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks().size(); k++){
                            resources.add(stone);
                        }
                    }
                    else if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraShields() != null && !((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraShields().isEmpty()){
                        for (int k =0; k<((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraShields().size(); k++){
                            resources.add(shield);
                        }
                    }
                    else if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins() != null && !((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins().isEmpty()){
                        for (int k =0; k<((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins().size(); k++){
                            resources.add(coin);
                        }
                    }
                    else if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraServants() != null && !((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraServants().isEmpty()){
                        for (int k =0; k<((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraServants().size(); k++){
                            resources.add(servant);
                        }
                    }
                }
            }
        }
        return resources;
    }


    /**
     * Removes the resources (cost) firstly from coffer, then storage and finally Extradeposit
     * Method used in checkResourcetoBuy if the boolean removeRes is true
     * Used for buying a Develop Card
     */
    private void RemoveResources(ArrayList<Resource> cost){

        if (!PlayerTurnisTrue().getDashboard().getCoffer().getResources().isEmpty()){
            for (int i = 0; i < PlayerTurnisTrue().getDashboard().getCoffer().getResources().size(); i++) {
                for (int j = 0; j < cost.size(); j++) {
                    if (PlayerTurnisTrue().getDashboard().getCoffer().getResources().get(i).equals(cost.get(j))) {  //Rimuovo risorse per comprare DevelopCards (NO LeaderCard)
                        PlayerTurnisTrue().getDashboard().getCoffer().getResources().remove(i);
                        cost.remove(j);
                        i--;
                        break;
                    }
                }
            }
        }
        if (!PlayerTurnisTrue().getDashboard().getStorage().getLvl1().isEmpty()){
            for (int i=0; i<cost.size(); i++) {
                if (PlayerTurnisTrue().getDashboard().getStorage().getLvl1().get(0).equals(cost.get(i))) {
                    PlayerTurnisTrue().getDashboard().getStorage().getLvl1().remove(0);
                    cost.remove(i);
                    break;
                }
            }
        }

        if (!PlayerTurnisTrue().getDashboard().getStorage().getLvl2().isEmpty()){
            for (int j =0; j<PlayerTurnisTrue().getDashboard().getStorage().getLvl2().size(); j++) {
                for (int i=0; i<cost.size(); i++) {
                    if (PlayerTurnisTrue().getDashboard().getStorage().getLvl2().get(j).equals(cost.get(i))) {
                        PlayerTurnisTrue().getDashboard().getStorage().getLvl2().remove(j);
                        cost.remove(i);
                        j--;
                        break;
                    }
                }
            }
        }

        if (!PlayerTurnisTrue().getDashboard().getStorage().getLvl3().isEmpty()){
            for (int j =0; j<PlayerTurnisTrue().getDashboard().getStorage().getLvl3().size(); j++) {
                for (int i=0; i<cost.size(); i++) {
                    if (PlayerTurnisTrue().getDashboard().getStorage().getLvl3().get(j).equals(cost.get(i))) {
                        PlayerTurnisTrue().getDashboard().getStorage().getLvl3().remove(j);
                        cost.remove(i);
                        j--;
                        break;
                    }
                }
            }
        }

        if (!PlayerTurnisTrue().getLeaderCardsOfPlayer().isEmpty()) {
            for (int i =0; i<PlayerTurnisTrue().getLeaderCardsOfPlayer().size(); i++){
                if (PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i) instanceof ExtraDeposit && PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i).isActive()){
                    if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks() != null && !((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks().isEmpty()){
                        for (int k =0; k<((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks().size(); k++){
                            for (int n =0; n<cost.size(); n++){
                                if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks().get(k).equals(cost.get(n))){
                                    ((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks().remove(k);
                                    cost.remove(n);
                                    k--;
                                    break;
                                }
                            }
                        }
                    }
                    else if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraShields() != null && !((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraShields().isEmpty()){
                        for (int k =0; k<((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraShields().size(); k++){
                            for (int n =0; n<cost.size(); n++){
                                if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraShields().get(k).equals(cost.get(n))){
                                    ((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraShields().remove(k);
                                    cost.remove(n);
                                    k--;
                                    break;
                                }
                            }
                        }
                    }
                    else if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins() != null && !((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins().isEmpty()){
                        for (int k =0; k<((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins().size(); k++){
                            for (int n =0; n<cost.size(); n++){
                                if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins().get(k).equals(cost.get(n))){
                                    ((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins().remove(k);
                                    cost.remove(n);
                                    k--;
                                    break;
                                }
                            }
                        }
                    }
                    else if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraServants() != null && !((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraServants().isEmpty()){
                        for (int k =0; k<((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraServants().size(); k++){
                            for (int n =0; n<cost.size(); n++){
                                if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraServants().get(k).equals(cost.get(n))){
                                    ((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraServants().remove(k);
                                    cost.remove(n);
                                    k--;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * See Javadoc for upper method
     * Used for activating a Develop Card
     */
    private void RemoveResourcesForProduction(ArrayList<Resource> resReq){
        Resource stone = new Resource("stone");
        Resource coin = new Resource("coin");
        Resource shield = new Resource("shield");
        Resource servant = new Resource("servant");

        ArrayList<Resource> cost = new ArrayList<>();
        for (int x=0; x<resReq.size(); x++){
            if (resReq.get(x).equals(coin))
                cost.add(coin);
            else if (resReq.get(x).equals(servant))
                cost.add(servant);
            else if (resReq.get(x).equals(shield))
                cost.add(shield);
            else if (resReq.get(x).equals(stone))
                cost.add(stone);
        }
        if (!PlayerTurnisTrue().getDashboard().getCoffer().getResources().isEmpty()){
            for (int i = 0; i < PlayerTurnisTrue().getDashboard().getCoffer().getResources().size(); i++) {
                for (int j = 0; j < cost.size(); j++) {
                    if (PlayerTurnisTrue().getDashboard().getCoffer().getResources().get(i).equals(cost.get(j))) {  //Rimuovo risorse per comprare DevelopCards (NO LeaderCard)
                        PlayerTurnisTrue().getDashboard().getCoffer().getResources().remove(i);
                        cost.remove(j);
                        i--;
                        break;
                    }
                }
            }
        }
        if (!PlayerTurnisTrue().getDashboard().getStorage().getLvl1().isEmpty()){
            for (int i=0; i<cost.size(); i++) {
                if (PlayerTurnisTrue().getDashboard().getStorage().getLvl1().get(0).equals(cost.get(i))) {
                    PlayerTurnisTrue().getDashboard().getStorage().getLvl1().remove(0);
                    cost.remove(i);
                    break;
                }
            }
        }

        if (!PlayerTurnisTrue().getDashboard().getStorage().getLvl2().isEmpty()){
            for (int j =0; j<PlayerTurnisTrue().getDashboard().getStorage().getLvl2().size(); j++) {
                for (int i=0; i<cost.size(); i++) {
                    if (PlayerTurnisTrue().getDashboard().getStorage().getLvl2().get(j).equals(cost.get(i))) {
                        PlayerTurnisTrue().getDashboard().getStorage().getLvl2().remove(j);
                        cost.remove(i);
                        j--;
                        break;
                    }
                }
            }
        }

        if (!PlayerTurnisTrue().getDashboard().getStorage().getLvl3().isEmpty()){
            for (int j =0; j<PlayerTurnisTrue().getDashboard().getStorage().getLvl3().size(); j++) {
                for (int i=0; i<cost.size(); i++) {
                    if (PlayerTurnisTrue().getDashboard().getStorage().getLvl3().get(j).equals(cost.get(i))) {
                        PlayerTurnisTrue().getDashboard().getStorage().getLvl3().remove(j);
                        cost.remove(i);
                        j--;
                        break;
                    }
                }
            }
        }

        if (!PlayerTurnisTrue().getLeaderCardsOfPlayer().isEmpty()) {
            for (int i =0; i<PlayerTurnisTrue().getLeaderCardsOfPlayer().size(); i++){
                if (PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i) instanceof ExtraDeposit && PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i).isActive()){
                    if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks() != null && !((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks().isEmpty()){
                        for (int k =0; k<((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks().size(); k++){
                            for (int n =0; n<cost.size(); n++){
                                if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks().get(k).equals(cost.get(n))){
                                    ((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks().remove(k);
                                    cost.remove(n);
                                    k--;
                                    break;
                                }
                            }
                        }
                    }
                    else if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraShields() != null && !((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraShields().isEmpty()){
                        for (int k =0; k<((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraShields().size(); k++){
                            for (int n =0; n<cost.size(); n++){
                                if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraShields().get(k).equals(cost.get(n))){
                                    ((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraShields().remove(k);
                                    cost.remove(n);
                                    k--;
                                    break;
                                }
                            }
                        }
                    }
                    else if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins() != null && !((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins().isEmpty()){
                        for (int k =0; k<((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins().size(); k++){
                            for (int n =0; n<cost.size(); n++){
                                if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins().get(k).equals(cost.get(n))){
                                    ((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins().remove(k);
                                    cost.remove(n);
                                    k--;
                                    break;
                                }
                            }
                        }
                    }
                    else if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraServants() != null && !((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraServants().isEmpty()){
                        for (int k =0; k<((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraServants().size(); k++){
                            for (int n =0; n<cost.size(); n++){
                                if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraServants().get(k).equals(cost.get(n))){
                                    ((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraServants().remove(k);
                                    cost.remove(n);
                                    k--;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Used for basic production
     */
    private void RemoveResourcesForBasicProduction(ArrayList<Resource> cost){

        if (!PlayerTurnisTrue().getDashboard().getCoffer().getResources().isEmpty()){
            for (int i = 0; i < PlayerTurnisTrue().getDashboard().getCoffer().getResources().size(); i++) {
                for (int j = 0; j < cost.size(); j++) {
                    if (PlayerTurnisTrue().getDashboard().getCoffer().getResources().get(i).equals(cost.get(j))) {  //Rimuovo risorse per comprare DevelopCards (NO LeaderCard)
                        PlayerTurnisTrue().getDashboard().getCoffer().getResources().remove(i);
                        cost.remove(j);
                        i--;
                        break;
                    }
                }
            }
        }
        if (!PlayerTurnisTrue().getDashboard().getStorage().getLvl1().isEmpty()){
            for (int i=0; i<cost.size(); i++) {
                if (PlayerTurnisTrue().getDashboard().getStorage().getLvl1().get(0).equals(cost.get(i))) {
                    PlayerTurnisTrue().getDashboard().getStorage().getLvl1().remove(0);
                    cost.remove(i);
                    break;
                }
            }
        }

        if (!PlayerTurnisTrue().getDashboard().getStorage().getLvl2().isEmpty()){
            for (int j =0; j<PlayerTurnisTrue().getDashboard().getStorage().getLvl2().size(); j++) {
                for (int i=0; i<cost.size(); i++) {
                    if (PlayerTurnisTrue().getDashboard().getStorage().getLvl2().get(j).equals(cost.get(i))) {
                        PlayerTurnisTrue().getDashboard().getStorage().getLvl2().remove(j);
                        cost.remove(i);
                        j--;
                        break;
                    }
                }
            }
        }

        if (!PlayerTurnisTrue().getDashboard().getStorage().getLvl3().isEmpty()){
            for (int j =0; j<PlayerTurnisTrue().getDashboard().getStorage().getLvl3().size(); j++) {
                for (int i=0; i<cost.size(); i++) {
                    if (PlayerTurnisTrue().getDashboard().getStorage().getLvl3().get(j).equals(cost.get(i))) {
                        PlayerTurnisTrue().getDashboard().getStorage().getLvl3().remove(j);
                        cost.remove(i);
                        j--;
                        break;
                    }
                }
            }
        }

        if (!PlayerTurnisTrue().getLeaderCardsOfPlayer().isEmpty()) {
            for (int i =0; i<PlayerTurnisTrue().getLeaderCardsOfPlayer().size(); i++){
                if (PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i) instanceof ExtraDeposit && PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i).isActive()){
                    if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks() != null && !((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks().isEmpty()){
                        for (int k =0; k<((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks().size(); k++){
                            for (int n =0; n<cost.size(); n++){
                                if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks().get(k).equals(cost.get(n))){
                                    ((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraRocks().remove(k);
                                    cost.remove(n);
                                    k--;
                                    break;
                                }
                            }
                        }
                    }
                    else if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraShields() != null && !((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraShields().isEmpty()){
                        for (int k =0; k<((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraShields().size(); k++){
                            for (int n =0; n<cost.size(); n++){
                                if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraShields().get(k).equals(cost.get(n))){
                                    ((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraShields().remove(k);
                                    cost.remove(n);
                                    k--;
                                    break;
                                }
                            }
                        }
                    }
                    else if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins() != null && !((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins().isEmpty()){
                        for (int k =0; k<((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins().size(); k++){
                            for (int n =0; n<cost.size(); n++){
                                if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins().get(k).equals(cost.get(n))){
                                    ((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraCoins().remove(k);
                                    cost.remove(n);
                                    k--;
                                    break;
                                }
                            }
                        }
                    }
                    else if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraServants() != null && !((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraServants().isEmpty()){
                        for (int k =0; k<((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraServants().size(); k++){
                            for (int n =0; n<cost.size(); n++){
                                if (((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraServants().get(k).equals(cost.get(n))){
                                    ((ExtraDeposit) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).getTwoExtraServants().remove(k);
                                    cost.remove(n);
                                    k--;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /*Verify Slot*/

    /**Assuming the player can place a card somewhere in his slots
     // it verifies the chosen position basing on the previous lvl**/
    public Boolean VerifyPositionInSlot(DevelopCard chosenCard,int chosenSlotIndex) {

        boolean validSlot = false;

        //take the slot to verify if I can add the card to the slot
        ArrayList<DevelopCard> chosenSlot = null;

        if (chosenSlotIndex == 1) {

            chosenSlot = PlayerTurnisTrue().getDashboard().getCardSlot().get(0).getSlot();

        } else if (chosenSlotIndex == 2) {

            chosenSlot = PlayerTurnisTrue().getDashboard().getCardSlot().get(1).getSlot();

        } else if (chosenSlotIndex == 3) {

            chosenSlot = PlayerTurnisTrue().getDashboard().getCardSlot().get(2).getSlot();

        }

        //verify levels ( I-II-III ) , if card slot is null and add card to slot
        //(as the chosen card is given above, after the controls, it can be assumed not null

        if (chosenSlot != null && chosenSlot.size() == 0 && chosenCard.getLevel()==1) {
            //card of any level can be added
            chosenSlot.add(chosenCard);
            validSlot = true;

        } else if (chosenSlot.size() > 0 && chosenSlot.get(chosenSlot.size() - 1).getLevel()==1 &&  chosenCard.getLevel()==2) {

            chosenSlot.add(chosenCard);
            validSlot = true;

        }
        else if (chosenSlot.size() > 0 && chosenSlot.get(chosenSlot.size() - 1).getLevel()==2 &&  chosenCard.getLevel()==3) {

            chosenSlot.add(chosenCard);
            validSlot = true;

        }else {
            validSlot = false;
        }


        return validSlot; //boolean used to decide whether it should send the error or no
    }


    /**method that verifies if the player can buy the selected dev card and
     if he can insert it in the chosen slot;
     if any of the two operations above cannot be done the method sends the appropiate error message
     and if necessary repeats the turn**/
    public void ErrorDevCard(ArrayList<DevelopCard> lvl1, ArrayList<DevelopCard> lvl2, ArrayList<DevelopCard> lvl3,
                             int chosenIndex, int chosenSlotIndex) throws IOException {


        boolean sendDevCardError = false;
        boolean sendSlotError = false;

        //check and get card
        DevelopCard chosenCard = BuyDevelopCard(lvl1,lvl2,lvl3,chosenIndex, false, false);


        //if returns null
        if (chosenCard==null){
            sendDevCardError=true;
        }else {

            //second control
            boolean verifySlot = VerifyPositionInSlot(chosenCard, chosenSlotIndex);

            //if returns false
            if (!verifySlot){
                sendSlotError=true;//non puoi inserire la carta
            }
        }

        //if sendDevCardError was set
        if (sendDevCardError){

            //send message to tell the palyer that the message has happend on the develop card(I will verify the slot
            //in teh nxt try)
            ErrorMsg errD = new ErrorMsg("\nYou don't have enough resources to buy this develop card! Try again...");
            searchTurn().sendMsg(errD);
            PlayerTurnisTrue().setBaseAction(false);


        }else if (sendSlotError) {


            //send message to tell the palyer that the message has happend on the slot
            ErrorMsg errS = new ErrorMsg("\nYou can't insert your card in this slot. Try again...");
            searchTurn().sendMsg(errS);
            PlayerTurnisTrue().setBaseAction(false);



        }


        //if none of the boolean was set to true, all of the operations above have been completed successfully
        if (!sendDevCardError && !sendSlotError) {

            //Remove
            BuyDevelopCard(lvl1,lvl2,lvl3,chosenIndex, true, true);

            SuccessMsg sux = new SuccessMsg("\nThe card you selected has been added to your slot and removed from the deck.");
            searchTurn().sendMsg(sux);
            PlayerTurnisTrue().setBaseAction(true);


        }

        EndActionMsg end = new EndActionMsg();
        searchTurn().sendMsg(end);

    }


    //------------------------------------------------------------------------------------------------------------------


    //---------Activate Production methods------------------------------------------------------------------------------

    /**method removes first found resources**/
    public boolean RemoveFirstFoundResources(ArrayList<String> remove, boolean rem){
        ArrayList<Resource> converted = new ArrayList<>();
        Storage playerStorage = PlayerTurnisTrue().getDashboard().getStorage();
        Coffer playerCoffer = PlayerTurnisTrue().getDashboard().getCoffer();

        for (int i= 0; i< remove.size() ; i++){
            converted = chooseResVersion2(remove.get(i),converted); //returns array of converted resources
        }

        if (!rem) {
            if(checkResourcestoBasicProduction(TotalResourcesOfPlayer(), false, converted)) {
                return true;
            }
            else
                return false;
        }
        else {
            checkResourcestoBasicProduction(TotalResourcesOfPlayer(), true, converted);
            return true;
        }
    }



    /**method searches the resources without removing them**/
    public boolean SearchRes(ArrayList<Resource> lookInto, ArrayList<Resource> toRemove){
        int counter = 0;
        int j = 0;
        int i=0;

        //if coffer is not empty
        if (lookInto.size()!=0) {

            while (i < lookInto.size() && j < toRemove.size()){

                if (lookInto.get(i).getResourcename().equals(toRemove.get(j).getResourcename()) && lookInto.size()>0){

                    counter++;
                    j++;
                    i=0;

                }else {
                    i++;
                }
            }

        }

        if (counter == toRemove.size()) {
            return true;
        }else {
            return false;
        }
    }



    /**method searches in levels and coffer without removing**/
    public boolean SearchFirstFoundResources(ArrayList<String> remove){
        ArrayList<Resource> converted = new ArrayList<>();
        Storage playerStorage = PlayerTurnisTrue().getDashboard().getStorage();
        Coffer playerCoffer = PlayerTurnisTrue().getDashboard().getCoffer();

        for (int i= 0; i< remove.size() ; i++){
            converted = chooseResVersion2(remove.get(i),converted);
        }

        if(SearchRes(playerCoffer.getResources(),converted)){
            return true;//found
        }else {
            if(SearchRes(playerStorage.getLvl1(),converted)){
                return true;
            }else if(SearchRes(playerStorage.getLvl2(),converted)){
                return true;
            }else if (SearchRes(playerStorage.getLvl3(), converted)){
                return true;
            }
        }

        //try to search in Storage Coffer and Extradeposit
        ArrayList<Resource> playerTotRes = TotalResourcesOfPlayer();

        if (SearchRes(playerTotRes,converted)){
            return true;
        }

        return false;//not found
    }



    /**transforms array of string in array of resources by using choosRes**/
    public ArrayList<Resource> toResource(ArrayList<String> strings){
        ArrayList<Resource> toRes = new ArrayList<>();

        for (int i = 0; i< strings.size(); i++){
            toRes = chooseResVersion2(strings.get(i), toRes);//returns array of tranformed resources
        }
        return toRes;
    }



    /**method that verifies that the player has the resources in the coffer or in the storage**/
    public boolean checkErrorBasic(ArrayList<String> basic){

        boolean sendError = false;

        //verify error in basic production
        if(basic.size() !=0) {
            boolean searchResult = SearchFirstFoundResources(basic);
            if (!searchResult) {
                sendError = true;
            }
        }

        return sendError;
    }


    /**method that verifies that the player has the requirements of the card in the last position of the
     *  slot memorized in z in order to produce**/
    public boolean checkErrorDevCard( ArrayList<ArrayList<String>> cards, ArrayList<Integer> z){

        boolean sendError = false;

        if (cards.size()!=0 && z.size()!=0) {

            for (int i = 0; i < z.size(); i++) {

                ArrayList<DevelopCard> slot = PlayerTurnisTrue().getDashboard().getCardSlot().get(z.get(i)-1).getSlot();
                DevelopCard devcard = slot.get(slot.size() - 1);//take card in last position in the slot

                ArrayList<Resource> requirements = toResource(cards.get(i));//transform resources
                ArrayList<Resource> production = devcard.checkResourcesforProduction(requirements);//check requirements

                if (production == null) {
                    sendError = true;
                }else {
                    sendError = !SearchFirstFoundResources(cards.get(i));
                }

            }
        }

        return sendError;
    }



    /**method that verifies if the player has the requirements to activated the desired productions
     * does the operations to activate the productions : removes the necessary resources and
     * adds the produced resources to the player or sends error messages if necessary **/
    public void ActivateProduction(ArrayList<String> basic, ArrayList<ArrayList<String>> cards, ArrayList<Integer> z) throws IOException {
        //basic is : [resource paid 1, resource paid 2, resource chosen]
        //z contains the slot of the card in position z.get(i) whose requirements are in cards.getArraylist(i)


        boolean sendErrorBasic=false;
        boolean sendErrorDevCard = false;
        ArrayList<Resource> resGeneratedToAdd = new ArrayList<>();

        if (basic.size()!=0) {

            ArrayList<String> toPay = new ArrayList<>();
            toPay.add(basic.get(0));
            toPay.add(basic.get(1));


            sendErrorBasic = !RemoveFirstFoundResources(toPay, false);
        }


        //error in basic production
        if (sendErrorBasic){
            ErrorMsg errB = new ErrorMsg("\nYou don't have enough resources to activate your basic production.\n");
            searchTurn().sendMsg(errB);
            PlayerTurnisTrue().setBaseAction(false);

        }else {

            if (basic.size() != 0) {
                ArrayList<Resource> basicRes = chooseRes(basic.get(basic.size() - 1));
                resGeneratedToAdd.add(basicRes.get(0));

                ArrayList<String> toPay = new ArrayList<>();
                toPay.add(basic.get(0));
                toPay.add(basic.get(1));
                RemoveFirstFoundResources(toPay, true); //remove the resources paid for basic production
                PlayerTurnisTrue().setBaseAction(true);
                SuccessMsg msg = new SuccessMsg("\nThe basic production has been activated!");
                searchTurn().sendMsg(msg);
            }
        }

        if (!z.isEmpty()) {
            for (int i=0; i< z.size(); i++){
                ArrayList<DevelopCard> slot = PlayerTurnisTrue().getDashboard().getCardSlot().get(z.get(i)).getSlot();//prendo lo slot
                DevelopCard devcard = slot.get(slot.size() - 1);//prendo carta in ultima posizione
                if (!checkResourcestoProdcution(TotalResourcesOfPlayer(), false, devcard)){
                    ErrorMsg errDev = new ErrorMsg("\nYou don't have enough resources to activate your develop cards' production.");
                    searchTurn().sendMsg(errDev);
                }
                else {
                    ArrayList<DevelopCard> slot1 = PlayerTurnisTrue().getDashboard().getCardSlot().get(z.get(i)).getSlot();
                    DevelopCard devcard1 = slot1.get(slot1.size() - 1);//take card in last position of the slot

                    ArrayList<Resource> production = devcard1.checkResourcesforProduction(TotalResourcesOfPlayer());//check req
                    if (production != null) {
                        for (int j = 0; j < production.size(); j++) {
                            if (production.get(j).getResourcename().equals("faith indicator")) {
                                PlayerTurnisTrue().getDashboard().getFaithRoute().moveIndicator(1);
                            } else {
                                resGeneratedToAdd.add(production.get(j));
                            }
                        }
                    }
                    checkResourcestoProdcution(TotalResourcesOfPlayer(), true, devcard1); //remove resources to pay for production of devcard
                    SuccessMsg msg = new SuccessMsg("The production has been activated!");
                    searchTurn().sendMsg(msg);
                    PlayerTurnisTrue().setBaseAction(true);
                }
            }
        }

        //adds to coffer all the resources generated from basic and devcards productions
        if (resGeneratedToAdd.size()!=0){
            for (int i=0; i<resGeneratedToAdd.size(); i++){
                PlayerTurnisTrue().getDashboard().getCoffer().getResources().add(resGeneratedToAdd.get(i));
            }
        }
        EndActionMsg end = new EndActionMsg();
        searchTurn().sendMsg(end);

    }


    /**method that sends a message to the player if he has an active "upgraded production" leader card
     **/
    public void LeaderProduction() throws IOException {
        int counter =0;
        ArrayList<LeaderCard> leaderCards = PlayerTurnisTrue().getLeaderCardsOfPlayer();

        for (int i = 0; i<leaderCards.size(); i++){
            if (leaderCards.get(i) instanceof UpgradedProduction && leaderCards.get(i).isActive()){
                counter++;
            }
        }

        if (counter==1) {
            UpgradedProduction upgradedProduction = null;
            for (int i = 0; i < leaderCards.size(); i++) {

                if (leaderCards.get(i) instanceof UpgradedProduction && leaderCards.get(i).isActive()) {

                    upgradedProduction = (UpgradedProduction) leaderCards.get(i);

                    LeaderProductionMsg msg = new LeaderProductionMsg(upgradedProduction.getResourceForProduction(), PlayerTurnisTrue());
                    searchTurn().sendMsg(msg);
                }
            }
        }else if (counter==2){
            //which card do you want to activate

            //save requirements
            ArrayList<Resource> req= new ArrayList<>();

            for (int i = 0; i < leaderCards.size(); i++) {
                if (leaderCards.get(i) instanceof UpgradedProduction && leaderCards.get(i).isActive()) {

                    UpgradedProduction upgradedProduction = (UpgradedProduction) leaderCards.get(i);

                    req.add(upgradedProduction.getResourceForProduction());

                }
            }

            DoubleLeaderProdmsg msg = new DoubleLeaderProdmsg(PlayerTurnisTrue(),req);
            searchTurn().sendMsg(msg);


        }

    }



    /**method that has to be invoked in order to verify if the player has the necessary resources to activate the upgraded
     * production and in case adds the resourced (included the faith point)**/
    public void VerifyLeaderProduction(ArrayList<String> leaderProduction) throws IOException {
        boolean sendErrorLeader=false;

        //check poi remove e add
        ArrayList<String> toPay = new ArrayList<>();
        toPay.add(leaderProduction.get(0));

        //check
        sendErrorLeader = checkErrorBasic(toPay);

        if (!sendErrorLeader){
            ArrayList<Resource> basicRes = chooseRes(leaderProduction.get(1));
            PlayerTurnisTrue().getDashboard().getCoffer().getResources().add(basicRes.get(0));

            PlayerTurnisTrue().getDashboard().getFaithRoute().moveIndicator(1);

            ArrayList<String> resToRemove = new ArrayList<>();
            resToRemove.add(leaderProduction.get(0));
            RemoveFirstFoundResources(resToRemove, true);
            PlayerTurnisTrue().setBaseAction(true);
        }
    }


    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Method that activates and removes the first signal on the stack
     */
    public void ActivateSignal(Game game) throws IOException {
        ActionSignal actionSignal;
        game.getActionSignalStack().getActionSignals().get(0).effect(game);
        actionSignal = game.getActionSignalStack().getActionSignals().get(0);
        ActionSignalMsg msg1 = new ActionSignalMsg(actionSignal);
        if (actionSignal instanceof BlackCross1Reset){
            game.setActionSignalStack(new ActionSignalStack());
        }
        else
            game.getActionSignalStack().getActionSignals().remove(0);
        searchTurn().sendMsg(msg1);
    }

    /**
     * Sets true boolean of leadercard
     * x-1 because the input(x) is 1 or 2, but index starts from 0
     */
    public void ActivateLeaderCard(int x) throws IOException {
        if (PlayerTurnisTrue().getLeaderCardsOfPlayer().size()==1 && x==2)
            x--;
        if (!PlayerTurnisTrue().getLeaderCardsOfPlayer().get(x-1).isActive()) {
            if (x<3 && PlayerTurnisTrue().getLeaderCardsOfPlayer().get(x-1).ActiveLeaderCard(PlayerTurnisTrue(),
                    PlayerTurnisTrue().getLeaderCardsOfPlayer().get(x-1))){

                if (PlayerTurnisTrue().isLeaderCard1())
                    PlayerTurnisTrue().setLeaderCard2(true);
                if (!PlayerTurnisTrue().isLeaderCard1())
                    PlayerTurnisTrue().setLeaderCard1(true);

                LeaderActivatedMsg msg4 = new LeaderActivatedMsg(x);
                searchTurn().sendMsg(msg4);
            }else {
                LeaderActivatedMsg msg5 = new LeaderActivatedMsg(0);
                searchTurn().sendMsg(msg5);
            }
        }
        else{
            LeaderActivatedMsg msg6 = new LeaderActivatedMsg(5);
            searchTurn().sendMsg(msg6);
        }
        EndActionMsg endActionMsg = new EndActionMsg();
        searchTurn().sendMsg(endActionMsg);

    }

    /**
     * Discard the chosen leaderCard
     */
    public void DiscardLeaderCard(int x) throws IOException {

        if (PlayerTurnisTrue().getLeaderCardsOfPlayer().size()==1 && x == 2)
            x--;
        if (x<3 && !PlayerTurnisTrue().getLeaderCardsOfPlayer().get(x-1).isActive()) {
            PlayerTurnisTrue().getLeaderCardsOfPlayer().remove(x-1);
            if (PlayerTurnisTrue().isLeaderCard1())
                PlayerTurnisTrue().setLeaderCard2(true);
            if (!PlayerTurnisTrue().isLeaderCard1())
                PlayerTurnisTrue().setLeaderCard1(true);
            PlayerTurnisTrue().getDashboard().getFaithRoute().moveIndicator(1);
            DiscardSuccessfulLeaderMsg msg = new DiscardSuccessfulLeaderMsg(1);
            searchTurn().sendMsg(msg);
        }
        else{
            DiscardSuccessfulLeaderMsg msg = new DiscardSuccessfulLeaderMsg(0);
            searchTurn().sendMsg(msg);
        }
        if (x == 3){
            DiscardSuccessfulLeaderMsg msg = new DiscardSuccessfulLeaderMsg(3);
            searchTurn().sendMsg(msg);
        }
        EndActionMsg msg4 = new EndActionMsg();
        searchTurn().sendMsg(msg4);
    }

    public void UseUpgradedProductionLeaderCard(String res) throws IOException {
        ErrorMsg err;
        for (int i=0; i<2; i++) {
            if (PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i) instanceof UpgradedProduction) {
                ((UpgradedProduction) PlayerTurnisTrue().getLeaderCardsOfPlayer().get(i)).ability(res, PlayerTurnisTrue());
                break;
            }
            else {
                err = new ErrorMsg("YOU DON'T HAVE THIS LEADERCARD!\n");
                searchTurn().sendMsg(err);

            }
        }
    }



    //----------------------------End Game methods----------------------------------------------------------------------

    /**verifies if the player has reached the end of the faith track**/
    public boolean EndGameLastPopeCall(){

        int playerIndicator = PlayerTurnisTrue().getDashboard().getFaithRoute().getFaithIndicator();
        int lorenzo = PlayerTurnisTrue().getDashboard().getFaithRoute().getFaithIndicatorLorenzo();

        if (playerIndicator >= (PlayerTurnisTrue().getDashboard().getFaithRoute().getRoute().size()) - 1 ){
            return true;
        }
        if (lorenzo >= (PlayerTurnisTrue().getDashboard().getFaithRoute().getRoute().size()) - 1 ) {
            setLorenzoWin(true);
            return true;
        }
        return false;
    }

    /**method that counts the number of develop cards**/
    public boolean EndGame7DevCard(){

        //slot1-slot2-slot3
        ArrayList<CardSlot> cardSlot = PlayerTurnisTrue().getDashboard().getCardSlot();
        int counter = 0;

        //0-1-2
        for (int i =0; i<cardSlot.size(); i++){
            ArrayList<DevelopCard>  slot = cardSlot.get(i).getSlot();

            for (int j = 0; j<slot.size(); j++){
                counter++;
            }
        }

        if (counter==7){
            return true;
        }

        return false;
    }

    /**method that needs to be called at the end of every turn in order to verify teh prerequisites to end the game**/
    public boolean EndGame(){

        boolean EndGameDevCards;
        boolean EndGameFaithRoute;

        EndGameDevCards= EndGame7DevCard();
        EndGameFaithRoute=EndGameLastPopeCall();

        return EndGameDevCards || EndGameFaithRoute;

    }

    //----------------------------End Game methods (SinglePlayer)----------------------------------------------------------------------

    /**
     * Quando rileva l'assenza di una colonna nella GridDevCards fa partire la fine del gioco
     */
    public boolean EndGameGridCard() {
        int c1=0, c2=0, c3=0, c4=0;
        for (int i=0; i<game.getDevelopCardsDeck().getLvl3().size(); i++) {
            if (game.getDevelopCardsDeck().getLvl3().get(i).getColour() == DevelopCard.Colour.B)
                c1++;
            else if (game.getDevelopCardsDeck().getLvl3().get(i).getColour() == DevelopCard.Colour.Y)
                c2++;
            else if (game.getDevelopCardsDeck().getLvl3().get(i).getColour() == DevelopCard.Colour.P)
                c3++;
            else if (game.getDevelopCardsDeck().getLvl3().get(i).getColour() == DevelopCard.Colour.G)
                c4++;
        }

        if (c1 == 0 || c2==0 || c3==0 || c4==0) {
            setLorenzoWin(true);
            return true;
        }
        else
            return false;
    }


    /**
     * Same for Multiplayer Endgame
     */
    public boolean EndGameSinglePlayer(){
        boolean EndGameDevCards;
        boolean EndGameGridCard;
        boolean EndGameFaithRoute;

        EndGameGridCard = EndGameGridCard();
        EndGameDevCards= EndGame7DevCard();
        EndGameFaithRoute=EndGameLastPopeCall();

        return EndGameDevCards || EndGameFaithRoute || EndGameGridCard;
    }

    //--------------------------------------------------------------------------------------------------

    /**sum VP dev cards**/
    public ArrayList<Integer> SumVPDevelopCards(){
        ArrayList<Integer> VP = new ArrayList<>(4);
        int VPdevCards=0;

        for (int j=0; j<game.getPlayers().size(); j++) {
            VPdevCards = 0;
            for (int i = 0; i < game.getPlayers().get(j).getDashboard().getCardSlot().size(); i++) {
                ArrayList<DevelopCard> slotx = game.getPlayers().get(j).getDashboard().getCardSlot().get(i).getSlot();

                for (int k = 0; k < slotx.size(); k++) {
                    VPdevCards = VPdevCards + slotx.get(k).getVictoryPoints();
                }

            }
            VP.add(j,VPdevCards);
        }
        return VP;
    }

    /**counts faith route's boxes**/
    public ArrayList<Integer> SumVPFaithRoute(){
        ArrayList<Integer> VP = new ArrayList<>(4);
        int VPfaithRoute = 0;
        int box = 0;

        for (int j=0; j<game.getPlayers().size(); j++) {
            box=0;
            VPfaithRoute=0;
            if (game.getPlayers().get(j).getDashboard().getFaithRoute().getFaithIndicator()<24) {
                while (box != game.getPlayers().get(j).getDashboard().getFaithRoute().getFaithIndicator()){
                    box++;

                }
                VPfaithRoute = game.getPlayers().get(j).getDashboard().getFaithRoute().getRoute().get(box).getVictoryPoints();
            }
            else if (game.getPlayers().get(j).getDashboard().getFaithRoute().getFaithIndicator()>=24) {
                VPfaithRoute = 20;
            }
            VP.add(j,VPfaithRoute);
        }
        return VP;
    }

    /**sums Victory points from active leader cards**/
    public ArrayList<Integer> SumVPLeaderCards(){
        ArrayList<Integer> VP = new ArrayList<>(4);
        int VPLeaderCards =0;

        for (int j=0; j<game.getPlayers().size(); j++) {
            VPLeaderCards = 0;
            for (int i = 0; i<game.getPlayers().get(j).getLeaderCardsOfPlayer().size(); i++){
                if(game.getPlayers().get(j).getLeaderCardsOfPlayer().get(i).isActive()) {
                    VPLeaderCards = VPLeaderCards + game.getPlayers().get(j).getLeaderCardsOfPlayer().get(i).getVictory_points();
                }
            }
            VP.add(j,VPLeaderCards);
        }
        return VP;
    }

    /**every 5 resources returns 1 point**/
    public ArrayList<Integer> SumVPResources(){
        ArrayList<Integer> VP = new ArrayList<>(4);
        int j = 0;
        ArrayList<Resource> coffer = game.getPlayers().get(j).getDashboard().getCoffer().getResources();
        ArrayList<Resource> lvl1 = game.getPlayers().get(j).getDashboard().getStorage().getLvl1();
        ArrayList<Resource> lvl2 = game.getPlayers().get(j).getDashboard().getStorage().getLvl2();
        ArrayList<Resource> lvl3 = game.getPlayers().get(j).getDashboard().getStorage().getLvl3();
        int VPResources = 0;
        int totRes =0;

        //count resurces in coffer
        for (j=0; j<game.getPlayers().size(); j++) {
            VPResources = 0;
            totRes =0;
            if (!coffer.isEmpty()) {
                for (int i=0; i<coffer.size(); i++){
                    totRes++;
                }
            }

            //unica res lvl 1
            if (!lvl1.isEmpty()) totRes++;

            if (!lvl2.isEmpty()) {
                for (int i=0; i<lvl2.size(); i++){
                    totRes++;
                }
            }

            if (!lvl3.isEmpty()) {
                for (int i=0; i<lvl3.size(); i++){
                    totRes++;
                }
            }
            VPResources = totRes/5;
            VP.add(j,VPResources);
        }
        return VP;
    }

    /**counts the final points by calling the methods above**/
    public void SumPoints() throws IOException {

        ArrayList<Integer> VPdev = SumVPDevelopCards();
        ArrayList<Integer> VPRoute = SumVPFaithRoute();
        ArrayList<Integer> VPleader = SumVPLeaderCards();
        ArrayList<Integer> VPresources = SumVPResources();

        for (int i =0; i<game.getPlayers().size(); i++){
            VPofPlayers.add(i, VPdev.get(i) + VPRoute.get(i) + VPleader.get(i) + VPresources.get(i) + game.getPlayers().get(i).getVictorypoints());
        }

        EndGameMsg msg = new EndGameMsg(VPofPlayers, game.getPlayers(), isLorenzoWin() , game.getPlayers().size() , winner());
        for (int i=0; i<game.getPlayers().size(); i++) {
            clientHandlers.get(i).sendMsg(msg);
        }
    }

    private String winner(){
        String name = null;
        int max=0;
        for (int i=0; i<game.getPlayers().size(); i++){
            if (game.getPlayers().get(i).getVictorypoints()>max){
                name = game.getPlayers().get(i).getName();
                max = game.getPlayers().get(i).getVictorypoints();
            }
        }
        return name;
    }

//----------------------------Faith Route Methods----------------------------------------------------------------------

    /**
     * Method that activates the RIV and checks the positions of the other players
     * if they are in a vatican slot, the PapalFC is activated and VPs are added to players, else it got discarded without adding VPs
     */
    public void checkRapportoInVaticano(Player player) throws IOException {
        RivActivatedMsg msg = new RivActivatedMsg();

        if (player.getDashboard().getFaithRoute().getFaithIndicator() >= 8 && //MULTI
                !player.getDashboard().getFaithRoute().getRoute().get(8).isReportGotActivated()){
            player.setHasActivatedRIV(true);
            activatePF(1);
            searchTurn().sendMsg(msg);
            for (int i =0; i<clientHandlers.size(); i++){
                clientHandlers.get(i).setRIVactivared(true);
            }
        }
        else if (player.getDashboard().getFaithRoute().getFaithIndicator() >= 16 &&
                !player.getDashboard().getFaithRoute().getRoute().get(16).isReportGotActivated()){
            player.setHasActivatedRIV(true);
            activatePF(2);
            searchTurn().sendMsg(msg);
            for (int i =0; i<clientHandlers.size(); i++){
                clientHandlers.get(i).setRIVactivared(true);
            }
        }
        else if (player.getDashboard().getFaithRoute().getFaithIndicator() >= 24 &&
                !player.getDashboard().getFaithRoute().getRoute().get(24).isReportGotActivated()){
            player.setHasActivatedRIV(true);
            activatePF(3);
            searchTurn().sendMsg(msg);
            for (int i =0; i<clientHandlers.size(); i++){
                clientHandlers.get(i).setRIVactivared(true);
            }
        }

        else if (player.getDashboard().getFaithRoute().getFaithIndicatorLorenzo() >= 8 && //SINGLE
                !player.getDashboard().getFaithRoute().getRoute().get(8).isReportGotActivated()){
            activatePF(1);
            searchTurn().sendMsg(msg);
            for (int i =0; i<clientHandlers.size(); i++){
                clientHandlers.get(i).setRIVactivared(true);
            }
        }
        else if (player.getDashboard().getFaithRoute().getFaithIndicatorLorenzo() >= 16 &&
                !player.getDashboard().getFaithRoute().getRoute().get(16).isReportGotActivated()){
            activatePF(2);
            searchTurn().sendMsg(msg);
            for (int i =0; i<clientHandlers.size(); i++){
                clientHandlers.get(i).setRIVactivared(true);
            }
        }
        else if (player.getDashboard().getFaithRoute().getFaithIndicatorLorenzo() >= 24 &&
                !player.getDashboard().getFaithRoute().getRoute().get(24).isReportGotActivated()){
            activatePF(3);
            searchTurn().sendMsg(msg);
            for (int i =0; i<clientHandlers.size(); i++){
                clientHandlers.get(i).setRIVactivared(true);
            }
        }

    }


    /**
     * Adds VPs to the player (if he's in a vatican slot) and removes the PapalFavCard that got activated
     */
    private void activatePF(int slot){
        int x = game.getPlayers().get(0).getDashboard().getFaithRoute().getPapalFavourCards().get(0).getValue();

        for (int i=0; i<game.getPlayers().size(); i++){
            if (game.getPlayers().get(i).getDashboard().getFaithRoute().isInReportSlot() == slot || game.getPlayers().get(i).getHasActivatedRIV()){
                game.getPlayers().get(i).addVictoryPoints(x);
                game.getPlayers().get(i).getDashboard().getFaithRoute().getPapalFavourCards().remove(0);
                game.getPlayers().get(i).setHasActivatedRIV(false);
            }
            else {
                game.getPlayers().get(i).getDashboard().getFaithRoute().getPapalFavourCards().remove(0);
            }
            rapportoInVaticanoGotActivated(slot);
        }

    }

    /**
     * Prevent a second player from activating Rapporto in Vaticano a second time on the same PopeBox
     */
    private void rapportoInVaticanoGotActivated(int slot){
        for (int i=0; i<game.getPlayers().size(); i++){
            if (slot == 1)
                game.getPlayers().get(i).getDashboard().getFaithRoute().getRoute().get(8).setReportGotActivated(true);
            else if (slot == 2)
                game.getPlayers().get(i).getDashboard().getFaithRoute().getRoute().get(16).setReportGotActivated(true);
            else if (slot == 3)
                game.getPlayers().get(i).getDashboard().getFaithRoute().getRoute().get(24).setReportGotActivated(true);
        }
    }

    /**
     * Checks to activate Riv for each player
     */
    public void checkRIV() throws IOException {
        for (int i=0; i<game.getPlayers().size(); i++){
            checkRapportoInVaticano(game.getPlayers().get(i));
        }
    }
}



