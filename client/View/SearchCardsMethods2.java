package it.polimi.ingsw.client.View;

import it.polimi.ingsw.server.Model.DevelopCard;

import java.util.ArrayList;

public class SearchCardsMethods2 {

    /**
     * Method to search level 3 cards and returns index
     */
    public int searchCardLvl3(ArrayList<DevelopCard> cards, int cardNum) {

        //cards==lvl3

        if (cards.size() == 0) {
            return -1;//array vuoto
        }


        //level not empty
        if (cardNum == 1) {

            //searching for green lv3 card
            for (int j = 0; j < cards.size(); ) {
                if (cards.get(j).getColour().equals(DevelopCard.Colour.G) ) {
                    return j;//return index
                }else{
                    j++;
                }
            }


        } else if (cardNum == 4) {

            //searching for blue lv3 card
            for (int j = 0; j < cards.size();) {
                if (cards.get(j).getColour().equals(DevelopCard.Colour.B)) {
                    return j;
                }else{
                    j++;
                }
            }


        } else if (cardNum == 7) {

            //searching for yellow lv3 card
            for (int j = 0; j < cards.size();) {
                if (cards.get(j).getColour().equals(DevelopCard.Colour.Y)) {
                    return j;
                }else{
                    j++;
                }
            }


        } else if (cardNum == 10) {

            //searching for purple lv3 card
            for (int j = 0; j < cards.size();) {
                if (cards.get(j).getColour().equals(DevelopCard.Colour.P)) {
                    return j;
                }else{
                    j++;
                }
            }

        }

        return -1;//cards finished


    }

    /**
     * Method to search level 2 cards and returns index
     */
    public int searchCardLvl2(ArrayList<DevelopCard> cards, int cardNum) {

        if (cards.size() == 0) {
            return -1;
        }
        if (cardNum == 2) {

            for (int j = 0; j < cards.size(); ) {
                if (cards.get(j).getColour().equals(DevelopCard.Colour.G)) {
                    return j;
                }else{
                    j++;
                }
            }

        } else if (cardNum == 5) {

            for (int j = 0; j < cards.size();) {
                if (cards.get(j).getColour().equals(DevelopCard.Colour.B)) {
                    return j;
                }else{
                    j++;
                }
            }

        } else if (cardNum == 8) {

            for (int j = 0; j < cards.size();) {
                if (cards.get(j).getColour().equals(DevelopCard.Colour.Y)) {
                    return j;
                }else{
                    j++;
                }
            }


        } else if (cardNum == 11) {

            for (int j = 0; j < cards.size();) {
                if (cards.get(j).getColour().equals(DevelopCard.Colour.P)) {
                   return j;
                }else {
                    j++;
                }
            }

        }
        return -1;
    }

    /**
     * Method to search level 1 cards and returns index
     */
    public int searchCardLvl1(ArrayList<DevelopCard> cards,int cardNum) {

        if (cards.size() == 0) {
            return -1;
        }

        if (cardNum == 3) {

            for (int j = 0; j < cards.size();) {
                if (cards.get(j).getColour().equals(DevelopCard.Colour.G)) {
                    return j;

                }else {
                    j++;
                }
            }

        } else if (cardNum == 6) {

            for (int j = 0; j < cards.size(); ) {
                if (cards.get(j).getColour().equals(DevelopCard.Colour.B)) {
                    return j;
                }else {
                    j++;
                }
            }

        } else if (cardNum == 9) {

            for (int j = 0; j < cards.size(); ) {
                if (cards.get(j).getColour().equals(DevelopCard.Colour.Y)) {
                    return j;
                }else {
                    j++;
                }
            }

        } else if (cardNum == 12) {

            for (int j = 0; j < cards.size(); ) {
                if (cards.get(j).getColour().equals(DevelopCard.Colour.P)) {
                    return j;
                }else {
                    j++;
                }
            }
        }
        return -1;
    }

}


