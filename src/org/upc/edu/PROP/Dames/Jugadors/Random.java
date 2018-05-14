/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.upc.edu.PROP.Dames.Jugadors;
import java.util.concurrent.ThreadLocalRandom;
import org.upc.edu.PROP.Dames.Model.CheckersData;
import org.upc.edu.PROP.Dames.Model.CheckersMove;
/**
 *
 * @author Ignasi GómeSebastià
 */
public class Random extends Jugador{
    
    public Random(String nom) {
        super(nom, false);
    }
    
    public int getJugada(CheckersMove[] jugades, CheckersData d){
        System.out.println("-ABANS-");      
        this.printTauler(d);
        System.out.println("--------");      
        int min = 0;
        int max = jugades.length - 1;
        int move = ThreadLocalRandom.current().nextInt(min, max + 1);          
        String msg = String.format("El Agent '%s' selecciona el moviment '%d'",this.nom,move);
        System.out.println(msg);     
        System.out.println("-DESPR-");   
        CheckersMove jugada = jugades[move];
        CheckersData despres = d.simMove(jugada.fromRow, jugada.fromCol, jugada.toRow, jugada.toCol);
        this.printTauler(despres);
        System.out.println("--------");  
        return move;
    }
    
}
