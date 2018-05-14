/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.upc.edu.PROP.Dames.Jugadors;

import org.upc.edu.PROP.Dames.Model.CheckersData;
import org.upc.edu.PROP.Dames.Model.CheckersMove;

/**
 *
 * @author Ignasi GómeSebastià
 */
public class Jugador {
    protected String nom;
    protected boolean manual;
    
    public Jugador(String nom, boolean manual){
        this.nom = nom;
        this.manual = manual;
    }
    
    public int getJugada(CheckersMove[] jugades, CheckersData d){
        return 0;
    }
    
    public boolean isAuto(){
        return !this.manual;
    }
    
     public boolean isManual(){
        return this.manual;
    }
     
     /*
        Decodifica una peça de Int a String. Está com a exemple de que es pot recorrer
        el tauler desde jugador, pero la funció hauria d'anar a Tauler en un
        bon disseny
     */
     
     private char decodifica(int pesa){
         char valor;
         switch (pesa) {
            case CheckersData.BLACK:  valor = 'b';
                     break;
            case CheckersData.BLACK_KING:  valor = 'B';
                     break;
            case CheckersData.RED:  valor = 'r';
                     break;
            case CheckersData.RED_KING:  valor = 'R';
                     break;
            case CheckersData.EMPTY:  valor = '-';
                     break;         
            default: valor = '?';
                     break;
        }
         return valor;
     }
     
     /*
        Recorre e imprimeix un tauler de joc. Está com a exemple de que es pot recorrer
        el tauler desde jugador, pero la funció hauria d'anar a Tauler en un
        bon disseny
     */
     protected void printTauler(CheckersData t){
         String msg = "";
         int nrows = t.getBoardRowCount();
         int ncols = t.getBoardRowCount();
         for (int row = 0; row < nrows; row++){
             for (int col = 0; col < ncols; col++){
                 char pesa = decodifica(t.pieceAt(row, col));
                 msg = msg + pesa;
             }
             msg = msg + "\n";
         }
         
         System.out.print(msg);
    }
}
