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
    
    private int profunditat=3;    
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
        
        if(prof == 0 || jugades.length==0) return SumaT(d, jugador,prof);
        if(d.pieceAt(jugades[0].fromCol,jugades[0].fromRow)==CheckersData.RED || d.pieceAt(jugades[0].fromCol,jugades[0].fromRow)==CheckersData.RED_KING)njugador=CheckersData.RED;
           else njugador=CheckersData.BLACK;
        for(int i = 0; i < jugades.length; i++){
            
            CheckersMove jugada = jugades[i];
            CheckersData despres = d.simMove(jugada.fromRow, jugada.fromCol, jugada.toRow, jugada.toCol);
            if(ha_guanyat(despres)){
                return -InfinitPositiu;
            }
            if(jugada.fromRow - jugada.toRow == 2 || jugada.fromRow - jugada.toRow == -2){
                
                jugada = despres.getLegalMoves(jugador)[0];
                if(jugada.fromRow - jugada.toRow == 2 || jugada.fromRow - jugada.toRow == -2){
                    despres = d.simMove(jugada.fromRow, jugada.fromCol, jugada.toRow, jugada.toCol);
                }
                else beta = Math.min(beta, maxim(despres.getLegalMoves(jugador),despres,alfa,beta,prof-1,njugador));
            }
            else beta = Math.min(beta, maxim(despres.getLegalMoves(jugador),despres,alfa,beta,prof-1,njugador));
            if(beta <= alfa) return beta;
            
        }
        return beta;
    }
    private int maxim(CheckersMove[] jugades,CheckersData d, int alfa, int beta, int prof, int jugador){
        
        int njugador=0;
        
        if(prof == 0 || jugades.length==0) return SumaT(d, jugador,prof);
        if(d.pieceAt(jugades[0].fromCol,jugades[0].fromRow)==CheckersData.RED || d.pieceAt(jugades[0].fromCol,jugades[0].fromRow)==CheckersData.RED_KING)njugador=CheckersData.RED;
           else njugador=CheckersData.BLACK;
        for(int i = 0; i < jugades.length; i++){
            
            CheckersMove jugada = jugades[i];
            CheckersData despres = d.simMove(jugada.fromRow, jugada.fromCol, jugada.toRow, jugada.toCol);
            if(ha_guanyat(despres)){
                return InfinitPositiu;
            }
            if(jugada.fromRow - jugada.toRow == 2 || jugada.fromRow - jugada.toRow == -2){
                
                jugada = despres.getLegalMoves(jugador)[0];
                if(jugada.fromRow - jugada.toRow == 2 || jugada.fromRow - jugada.toRow == -2){
                    despres = d.simMove(jugada.fromRow, jugada.fromCol, jugada.toRow, jugada.toCol);
                }
                else alfa = Math.max(alfa, minim(despres.getLegalMoves(jugador),despres,alfa,beta,prof-1,njugador));
            }
            else alfa = Math.max(alfa, minim(despres.getLegalMoves(jugador),despres,alfa,beta,prof-1,njugador));
            if(alfa >= beta) return alfa;
            }
        
        return alfa;
    }
    
    
    private int SumaT(CheckersData Tablero, int jugador,int prof){
        
        Pair<Integer, Integer> aux = new Pair<>(0, 0);
        aux=comptaVerm(Tablero);
        int numVerm = aux.getKey();
        int numNegr = aux.getValue();
        int eval = 0;        
        
        if(jugador == CheckersData.RED){
            eval = numVerm - numNegr;
            if(numNegr==0 && player1==CheckersData.RED)eval=eval*(prof+1);
            
        }
        else {
            eval = numNegr - numVerm;
            if(numVerm==0 && player1==CheckersData.BLACK)eval=eval*(prof+1);
        }
        if(jugador==player1) return eval;
        else return-eval;
    }
    
    private Pair<Integer, Integer> comptaVerm(CheckersData Tablero){
        
        
        int comptVerm = 0;
        int comptNegr = 0;
        for(int i = 0; i<Tablero.getBoardColumnCount();++i){
            for(int j = 0; j<Tablero.getBoardRowCount();++j){
                if(Tablero.pieceAt(i,j) == CheckersData.RED){
                    comptVerm+=3;
                    if(i>= Tablero.getBoardColumnCount()-2 )comptVerm+=2;
                }
                else if(Tablero.pieceAt(i,j) == CheckersData.RED_KING){
                    comptVerm += 8; //El rei el compto per 10
                }
                else if(Tablero.pieceAt(i,j) == CheckersData.BLACK){
                    comptNegr+=3;
                    if(i<= 2 )comptNegr+=2;
                }
                else if(Tablero.pieceAt(i,j) == CheckersData.BLACK_KING){
                    comptNegr += 8; //El rei el compto per 10
                }
            }
        }
        Pair<Integer, Integer> aux = new Pair<>(comptVerm, comptNegr);
       
        return aux;
    }
    
    
    
    
}
