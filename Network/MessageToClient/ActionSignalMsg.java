package it.polimi.ingsw.Network.MessageToClient;

import it.polimi.ingsw.client.View.ViewCLI;
import it.polimi.ingsw.server.Model.ActionSignal;

public class ActionSignalMsg extends MessageToClient{

    private ActionSignal actionSignal;

    /**
     * Constructor
     */
    public ActionSignalMsg(ActionSignal actionSignal) {
        this.actionSignal = actionSignal;
    }

    /**
     * To process the message in the view
     */
    @Override
    public void process(ViewCLI view) {
        view.ShowActionSignal(actionSignal);  //solo per visualizzarlo
    }


}
