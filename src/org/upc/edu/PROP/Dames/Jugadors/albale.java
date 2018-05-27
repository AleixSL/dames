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
    private int player1=0,jugador=0;
    public albale(String nom){
        super(nom,false);
    }
    
    @Override
    public int getJugada(CheckersMove[] jugades, CheckersData d){
        
        int move = 0;
        int ElMejor=0;
        int actualM=-InfinitPositiu;
        
        if(d.pieceAt(jugades[0].fromRow,jugades[0].fromCol)==CheckersData.RED || d.pieceAt(jugades[0].fromRow,jugades[0].fromCol)==CheckersData.RED_KING){
            jugador=CheckersData.BLACK;
            player1=CheckersData.RED;
            }
           else 
            {
            jugador=CheckersData.RED;
            player1=CheckersData.BLACK;
            }
        System.out.println(SumaT(d, player1,0));
        System.out.println(player1);
        for (int i = 0; i<jugades.length; i++){
            CheckersMove jugada = jugades[i];
            CheckersData despres=d;
            
            
            while(jugada.fromRow - jugada.toRow == 2 || jugada.fromRow - jugada.toRow == -2 && !(despres.getLegalMoves(player1)==null)){
                
                despres = despres.simMove(jugada.fromRow, jugada.fromCol, jugada.toRow, jugada.toCol);
                jugada = despres.getLegalMoves(player1)[0];
                
                }
            despres= despres.simMove(jugada.fromRow, jugada.fromCol, jugada.toRow, jugada.toCol);
            if(ha_guanyat(despres)){
                return i;
            }
            ElMejor=minim(despres.getLegalMoves(jugador),despres,-InfinitPositiu,InfinitPositiu,profunditat);
            
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
    private int minim(CheckersMove[] jugades,CheckersData d, int alfa, int beta, int prof)
    {
        
        if(prof == 0) return SumaT(d, player1,prof);
        if(jugades==null)return beta;
        for(int i = 0; i < jugades.length; i++){
            CheckersMove jugada = jugades[i];
            CheckersData despres=d;
            
            
            while(jugada.fromRow - jugada.toRow == 2 || jugada.fromRow - jugada.toRow == -2 && !(despres.getLegalMoves(jugador)==null)){
                
                despres = despres.simMove(jugada.fromRow, jugada.fromCol, jugada.toRow, jugada.toCol);
                 jugada = despres.getLegalMoves(jugador)[0];
                
                
                }
            despres= despres.simMove(jugada.fromRow, jugada.fromCol, jugada.toRow, jugada.toCol);
            if(ha_guanyat(despres)){
                return -InfinitPositiu;
            }
            beta = Math.min(beta, maxim(despres.getLegalMoves(player1),despres,alfa,beta,prof-1));
            if(beta <= alfa) return beta;
            
        }
        return beta;
    }
    private int maxim(CheckersMove[] jugades,CheckersData d, int alfa, int beta, int prof){
        
        if(prof == 0) return SumaT(d, jugador,prof);
        if(jugades==null)return alfa;
        for(int i = 0; i < jugades.length; i++){
            
            CheckersMove jugada = jugades[i];
            CheckersData despres=d;
            
            while(jugada.fromRow - jugada.toRow == 2 || jugada.fromRow - jugada.toRow == -2 && !(despres.getLegalMoves(player1)==null)){
                
                despres = despres.simMove(jugada.fromRow, jugada.fromCol, jugada.toRow, jugada.toCol);
                jugada = despres.getLegalMoves(player1)[0];
                
                }
            despres= despres.simMove(jugada.fromRow, jugada.fromCol, jugada.toRow, jugada.toCol);
            if(ha_guanyat(despres)){
                return InfinitPositiu;
            }
            alfa = Math.max(alfa, minim(despres.getLegalMoves(jugador),despres,alfa,beta,prof));
            if(alfa >= beta) return alfa;
            }
        
        return alfa;
    }
    
    
    private int SumaT(CheckersData Tablero, int jug,int prof){
        
        Pair<Integer, Integer> aux = new Pair<>(0, 0);
        aux=comptaVerm(Tablero);
        int numVerm = aux.getKey();
        int numNegr = aux.getValue();
        int eval = 0;        
        
        if(jug == CheckersData.RED || jug == CheckersData.RED_KING){
            eval = numVerm - numNegr;
             
        }
        else {
            eval = numNegr - numVerm;
        }
       
        if(jug==player1) return eval;
        else return-eval;
    }
    
    private Pair<Integer, Integer> comptaVerm(CheckersData Tablero){
        
        
        int comptVerm = 0;
        int comptNegr = 0;
        for(int i = 0; i<Tablero.getBoardColumnCount();++i){
            for(int j = 0; j<Tablero.getBoardRowCount();++j){
                if(Tablero.pieceAt(i,j) == CheckersData.RED){
                    comptVerm+=3;
                   
                }
                else if(Tablero.pieceAt(i,j) == CheckersData.RED_KING){
                    comptVerm += 10; //El rei el compto per 10
                }
                else if(Tablero.pieceAt(i,j) == CheckersData.BLACK){
                    comptNegr+=3;
                   
                }
                else if(Tablero.pieceAt(i,j) == CheckersData.BLACK_KING){
                    comptNegr += 10; //El rei el compto per 10
                }
            }
        }
        Pair<Integer, Integer> aux = new Pair<>(comptVerm, comptNegr);
       
        return aux;
    }
    
    
    
    
}
