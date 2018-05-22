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
 * @author e7844438
 */
public class albale extends Jugador{
    
    private int profunditat=0;    
    private final Integer InfinitPositiu = Integer.MAX_VALUE;
    
    public albale(String nom){
        super(nom,false);
    }
    
    @Override
    public int getJugada(CheckersMove[] jugades, CheckersData d){
        int move = 0;
        int ElMejor=0;
        int actualM=-InfinitPositiu;
        int jugador=0;
        for (int i = 0; i<jugades.length; i++){
            CheckersMove jugada = jugades[i];
            CheckersData despres = d.simMove(jugada.fromRow, jugada.fromCol, jugada.toRow, jugada.toCol);
            if(ha_guanyat(despres)){
                return i;
            }
            if(despres.pieceAt(jugada.fromCol,jugada.fromRow)==CheckersData.RED || despres.pieceAt(jugada.fromCol,jugada.fromRow)==CheckersData.RED_KING)jugador=0;
            else jugador=1;
            ElMejor=minim(despres.getLegalMoves(jugador),despres,-InfinitPositiu,InfinitPositiu,profunditat,jugador);
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
        
        if(prof == 0) return SumaT(d, jugador);
        for(int i = 0; i < jugades.length; i++){
            
            CheckersMove jugada = jugades[i];
            CheckersData despres = d.simMove(jugada.fromRow, jugada.fromCol, jugada.toRow, jugada.toCol);
            if (ha_guanyat(despres)) {
                return -InfinitPositiu;
            }
            beta = Math.min(beta, maxim(despres.getLegalMoves(jugador),despres,-InfinitPositiu,InfinitPositiu,profunditat,jugador));
            if(beta <= alfa) return beta;
            
        }
        return beta;
    }
    private int maxim(CheckersMove[] jugades,CheckersData d, int alfa, int beta, int prof, int jugador){
        
        if(prof == 0) return SumaT(d, jugador);
        for(int i = 0; i < jugades.length; i++){
            
            CheckersMove jugada = jugades[i];
            CheckersData despres = d.simMove(jugada.fromRow, jugada.fromCol, jugada.toRow, jugada.toCol);
            if (ha_guanyat(despres)) {
                return -InfinitPositiu;
            }
                alfa = Math.max(alfa, minim(despres.getLegalMoves(jugador),despres,-InfinitPositiu,InfinitPositiu,profunditat,jugador));
                if(alfa >= beta) return alfa;
            }
        
        return alfa;
    }
    private int SumaT(CheckersData Tablero, int jugador) {

        int suma = 0;
        for (int i = 0; i < Tablero.getBoardColumnCount(); i++)
            for (int j = 0; j < Tablero.getBoardRowCount(); j++)
                suma += EvalPos(Tablero, i, j, jugador);
            

        return suma;
    }
    
    private int EvalPos(CheckersData Tablero, int i, int j, int jugador){
        int numVerm = comptaVerm(Tablero);
        int numNegr = comptaNegr(Tablero);
        int eval = 0;        
        if(jugador == CheckersData.RED){
            eval = numVerm - numNegr;
        }
        else eval = numNegr - numVerm;
    
        return eval;
    }
    
    private int comptaVerm(CheckersData Tablero){
        int comptVerm = 0;
        for(int i = 0; i<Tablero.getBoardColumnCount();++i){
            for(int j = 0; j<Tablero.getBoardRowCount();++j){
                if(Tablero.pieceAt(i,j) == CheckersData.RED){
                    ++comptVerm;
                }
                else if(Tablero.pieceAt(i,j) == CheckersData.RED_KING){
                    comptVerm += 10; //El rei el compto per 10
                }
            }
        }
        return comptVerm;
    }
    
    private int comptaNegr(CheckersData Tablero){
        int comptNegr = 0;
        for(int i = 0; i<Tablero.getBoardColumnCount();++i){
            for(int j = 0; j<Tablero.getBoardRowCount();++j){
                if(Tablero.pieceAt(i,j) == CheckersData.BLACK){
                    ++comptNegr;
                }
                else if(Tablero.pieceAt(i,j) == CheckersData.BLACK_KING){
                    comptNegr += 10; //El rei el compto per 10
                }
            }
        }
        return comptNegr;
    }
    
    
    
}
