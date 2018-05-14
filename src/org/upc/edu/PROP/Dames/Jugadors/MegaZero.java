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
public class MegaZero extends Jugador{
    
    public MegaZero(String nom) {
        super(nom, false);
    }
    
    public int getJugada(CheckersMove[] jugades, CheckersData d){
        int move = 0;
        String msg = String.format("El Agent '%s' selecciona el moviment '%d'",this.nom,move);
        System.out.println(msg);
        return move;
    }
    
}
