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
    
    private int profunditat=5;    
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
        /*int ContadorR = 0;
        int ContadorB = 0;
        for (int i = 0; i < Tablero.getBoardColumnCount(); i++)
            for (int j = 0; j < Tablero.getBoardRowCount(); j++) {
                if(Tablero.pieceAt(i,j)==CheckersData.RED || Tablero.pieceAt(i,j)==CheckersData.RED_KING)ContadorR++;
                if(Tablero.pieceAt(i,j)==CheckersData.BLACK || Tablero.pieceAt(i,j)==CheckersData.BLACK_KING)ContadorB++;
            }
        */
        Pair<Integer, Integer> aux = new Pair<>(0, 0);
        aux=comptaVerm(Tablero);
        int ContadorR = aux.getKey();
        int ContadorB = aux.getValue();
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
                if(!(despres.getLegalMoves(jugador)==null))jugada = despres.getLegalMoves(jugador)[0];
                
                
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
                if(!(despres.getLegalMoves(player1)==null))jugada = despres.getLegalMoves(player1)[0];
                
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
    
     private int SumaT(CheckersData Tablero, int jugador, int prof){
        
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
        Pair<Integer, Integer> aux = new Pair<>(0, 0);
        for(int i = 0; i<Tablero.getBoardColumnCount();++i){
            for(int j = 0; j<Tablero.getBoardRowCount();++j){
                aux=enbloc(Tablero, i, j);
                switch (Tablero.pieceAt(i,j)) {
                    case CheckersData.RED:
                        if(centre(i,j)) comptVerm += 2;                             //Si està al centre del camp compta 2 (qui controla el centre domina la partida)
                        else if(cantosVerm(i,j)) comptVerm+=10;                     //Si està al cantó, compta 10 (als cantons no et poden matar)
                        else if(aux.getKey()==1) comptVerm += aux.getValue() + 2;   //Si està en bloc, suma el numero de peces al comptador (en manada (de lobos no l'altra pls))
                        else ++comptVerm;                                           //Si compta una fitxa normal només compta 1
                        break;
                    case CheckersData.RED_KING:
                        //Els reis compten més
                        if(centre(i,j)) comptVerm +=12;
                        else if(cantosVerm(i,j))comptVerm += 20;
                        else if(aux.getKey()==1) comptVerm += aux.getValue() + 12;
                        else comptVerm += 10;
                        break;
                    case CheckersData.BLACK:
                        if(centre(i,j)) comptNegr += 2;
                        else if(cantosNegr(i,j)) comptNegr += 10;
                        else if(aux.getKey()==1) comptNegr += aux.getValue() + 2;
                        else ++comptNegr;
                        break;
                    case CheckersData.BLACK_KING:
                        if(centre(i,j)) comptNegr += 12;
                        else if(cantosNegr(i,j)) comptNegr += 20;
                        else if(aux.getKey()==1) comptNegr += aux.getValue() + 12;
                        else comptNegr += 10; //El rei el compto per 10
                        break;
                    default:
                        break;
                }
            }
        }
        Pair<Integer, Integer> aux2 = new Pair<>(comptVerm, comptNegr);
       
        return aux2;
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

    private Pair<Integer, Integer> enbloc(CheckersData Tablero, int i, int j){
        int bloc = 0;
        int numFitxes = 0;
        for(int a = 0; a < Tablero.getBoardColumnCount();++a){
            for(int b = 0; b<Tablero.getBoardRowCount();++b){
                if((((a==i-1)||(a==i+1))&&((b==j+1)||(b==j-1))) && (Tablero.pieceAt(a,b) == Tablero.pieceAt(i,j))){
                    bloc = 1; //Si esta en bloc es posa a 1 (no es pot fer servir un booleà a un pair lol #chapuza)
                    ++numFitxes;
                }
            }
        }
        Pair<Integer, Integer> aux = new Pair<>(bloc, numFitxes);
        return aux;
    }
    /*private boolean enbloc(CheckersData Tablero, int i, int j){
        boolean bloc = false;
        for(int a = 0; a < Tablero.getBoardColumnCount();++a){
            for(int b = 0; b<Tablero.getBoardRowCount();++b){
                if(((a==i-1)||(a==i+1))&&((b==j+1)||(b==j-1))){
                    bloc = true;
                }
                
            }
        }
        return bloc;
    }
    */
}
