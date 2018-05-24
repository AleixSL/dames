/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.upc.edu.PROP.Dames.Jugadors;
import java.util.concurrent.ThreadLocalRandom;
import org.upc.edu.PROP.Dames.Model.CheckersData;
import org.upc.edu.PROP.Dames.Model.CheckersMove;
import javafx.util.Pair;
/**
 *
 * @author e7844438
 */
public class albale extends Jugador{
    
    private int profunditat=4;    
    private final Integer InfinitPositiu = Integer.MAX_VALUE;
    private int player1=0;
    public albale(String nom){
        super(nom,false);
    }
    
    @Override
    public int getJugada(CheckersMove[] jugades, CheckersData d){
        int move = 0;
        int ElMejor=0;
        int actualM=-InfinitPositiu;
        int jugador=0;
        if(d.pieceAt(jugades[0].fromCol,jugades[0].fromRow)==CheckersData.RED || d.pieceAt(jugades[0].fromCol,jugades[0].fromRow)==CheckersData.RED_KING){
            jugador=CheckersData.BLACK;
            player1=CheckersData.RED;
            }
           else 
            {
            jugador=CheckersData.RED;
            player1=CheckersData.BLACK;
            }
        for (int i = 0; i<jugades.length; i++){
            CheckersMove jugada = jugades[i];
            CheckersData despres = d.simMove(jugada.fromRow, jugada.fromCol, jugada.toRow, jugada.toCol);
            if(ha_guanyat(despres)){
                return i;
            }
           
            ElMejor=minim(despres.getLegalMoves(jugador),despres,-InfinitPositiu,InfinitPositiu,profunditat,player1);
                if(ElMejor>actualM){
                    move=i;
                    actualM=ElMejor;
                }
        }
        return move;
    }
    
    
    private boolean ha_guanyat(CheckersData Tablero){
        boolean fin=false;
        int ContadorR=0;
        int ContadorB=0;
         for (int i = 0; i < Tablero.getBoardColumnCount(); i++)
            for (int j = 0; j < Tablero.getBoardRowCount(); j++) {
                if(Tablero.pieceAt(i,j)==CheckersData.RED || Tablero.pieceAt(i,j)==CheckersData.RED_KING)ContadorR++;
                if(Tablero.pieceAt(i,j)==CheckersData.BLACK || Tablero.pieceAt(i,j)==CheckersData.BLACK_KING)ContadorB++;
            }
        if(ContadorR==0 || ContadorB==0)fin=true;
        
    
        return fin;
    }
    private int minim(CheckersMove[] jugades,CheckersData d, int alfa, int beta, int prof, int jugador)
    {
        int njugador=0;
        if(d.pieceAt(jugades[0].fromCol,jugades[0].fromRow)==CheckersData.RED || d.pieceAt(jugades[0].fromCol,jugades[0].fromRow)==CheckersData.RED_KING)njugador=CheckersData.RED;
           else njugador=CheckersData.BLACK;
        if(prof == 0) return SumaT(d, jugador);
        for(int i = 0; i < jugades.length; i++){
            
            CheckersMove jugada = jugades[i];
            CheckersData despres = d.simMove(jugada.fromRow, jugada.fromCol, jugada.toRow, jugada.toCol);
            if(ha_guanyat(despres)){
                return -InfinitPositiu;
            }
            
            beta = Math.min(beta, maxim(despres.getLegalMoves(jugador),despres,-InfinitPositiu,InfinitPositiu,prof-1,njugador));
            if(beta <= alfa) return beta;
            
        }
        return beta;
    }
    private int maxim(CheckersMove[] jugades,CheckersData d, int alfa, int beta, int prof, int jugador){
        
        int njugador=0;
        if(d.pieceAt(jugades[0].fromCol,jugades[0].fromRow)==CheckersData.RED || d.pieceAt(jugades[0].fromCol,jugades[0].fromRow)==CheckersData.RED_KING)njugador=CheckersData.RED;
           else njugador=CheckersData.BLACK;
        if(prof == 0) return SumaT(d, jugador);
        for(int i = 0; i < jugades.length; i++){
            
            CheckersMove jugada = jugades[i];
            CheckersData despres = d.simMove(jugada.fromRow, jugada.fromCol, jugada.toRow, jugada.toCol);
            if(ha_guanyat(despres)){
                return InfinitPositiu;
            }
            alfa = Math.max(alfa, minim(despres.getLegalMoves(jugador),despres,-InfinitPositiu,InfinitPositiu,prof,njugador));
            if(alfa >= beta) return alfa;
            }
        
        return alfa;
    }
    
     private int SumaT(CheckersData Tablero, int jugador){
        
        Pair<Integer, Integer> aux = new Pair<>(0, 0);
        aux=comptaVerm(Tablero);
        int numVerm = aux.getKey();
        int numNegr = aux.getValue();
        int eval = 0;        
        
        if(jugador == CheckersData.RED){
            eval = numVerm - numNegr;
        }
        else eval = numNegr - numVerm;
        if(jugador==player1) return eval;
        else return-eval;
    }
    
    private Pair<Integer, Integer> comptaVerm(CheckersData Tablero){
        int comptVerm = 0;
        int comptNegr = 0;
        for(int i = 0; i<Tablero.getBoardColumnCount();++i){
            for(int j = 0; j<Tablero.getBoardRowCount();++j){
                if(Tablero.pieceAt(i,j) == CheckersData.RED){
                    if(centre(i,j)) comptVerm += 2;
                    else if(cantosVerm(i,j)) comptVerm+=5;
                    else ++comptVerm;
                }
                else if(Tablero.pieceAt(i,j) == CheckersData.RED_KING){
                    if(centre(i,j)) comptVerm +=12;
                    else if(cantosVerm(i,j))comptVerm += 15;
                    else comptVerm += 10; //El rei el compto per 10
                }
                else if(Tablero.pieceAt(i,j) == CheckersData.BLACK){
                    if(centre(i,j)) comptNegr += 2;
                    else if(cantosNegr(i,j)) comptNegr += 5;
                    else ++comptNegr;
                }
                else if(Tablero.pieceAt(i,j) == CheckersData.BLACK_KING){
                    if(centre(i,j)) comptNegr += 12;
                    else if(cantosNegr(i,j)) comptNegr += 15;
                    else comptNegr += 10; //El rei el compto per 10
                }
            }
        }
        Pair<Integer, Integer> aux = new Pair<>(comptVerm, comptNegr);
       
        return aux;
    }
    
    /*
    private int EvalPos(CheckersData Tablero, int jugador){
        int numVerm = 0;
        int numNegr = 0;
        int eval = 0;
        for(int i = 0; i < Tablero.getBoardColumnCount();++i){
            for(int j = 0; j < Tablero.getBoardRowCount(); ++j){
                if(Tablero.pieceAt(i,j) == CheckersData.RED){
                    if(centre(i,j)){
                        numVerm += 5;
                    }
                    else ++numVerm;
                }
                else if(Tablero.pieceAt(i,j) == CheckersData.RED_KING){
                    if(centre(i,j)){
                        numVerm += 15;
                    }
                    else numVerm += 10; //El rei el compto per 10
                }
                else if(Tablero.pieceAt(i,j) == CheckersData.BLACK){
                    if(centre(i,j)){
                        numNegr += 5;
                    }
                    else ++numNegr;
                }
                else if(Tablero.pieceAt(i,j) == CheckersData.BLACK_KING){
                    if(centre(i,j)){
                        numNegr += 15;
                    }
                    else numNegr += 10; //El rei el compto per 10
                }
            }
        }
        if(jugador == CheckersData.RED){
            eval = numVerm - numNegr;
        }
        else eval = numNegr - numVerm;
        return eval;
    }
*/
    private boolean centre(int i, int j) {
        boolean centre = false;
        if(((i==3 || i==5) && j==3)  
                || ((i==2 || i==4)&& j==4)){
            centre = true;
        }
        return centre;
    }
    //Mira si està al darrere de tot i als costats el tauler. 
    private boolean cantosNegr(int i, int j){
        boolean fi = false;
        if(((i==0 || i==2 || i==4 || i==6 ) && j==1) ||
                (i==0 && (j==0 || j==2 || j==4 ||j==6)) ||
                    (i==7 && (j==1 || j==3 || j==5 || j==7))){
            fi = true;
        }
        return fi;
    }
    
    private boolean cantosVerm(int i, int j){
        boolean fi = false;
        if(((i==1 || i==3 || i==5 || i==7) && j==7) ||
                (i==0 && (j==0 || j==2 || j==4 ||j==6)) ||
                    (i==7 && (j==1 || j==3 || j==5 || j==7))){
            fi = true;
        }
        return fi;
    }

}
