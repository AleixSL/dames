/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.upc.edu.PROP.Dames.Model;

import java.util.Vector;

/**
 *
 * @author Ignasi GómeSebastià
 */
public class CheckersData {
      /* Guarda les dades del joc. Sap quines peçes (i tipus) son a cada casella
        També calcula la llista de moviments possibles
    NOTA: RED es mou amunt en el tauler (baixa el numero de fila)
    NOTA: BLACK es mou abaix en el tauler (puja el numero de fila)
    NOTA: Els reis es mouen com volen, pero sempre de un en un
      
   /*  Aquestes constants representen els possibles continguts d'una casella del
        joc, incluent-hi buida.
        RED i BLACK també es fan servir per representar els jugadors
   */

   public static final int
             EMPTY = 0,
             RED = 1,
             RED_KING = 2,
             BLACK = 3,
             BLACK_KING = 4;

   private int[][] board;  // board[fila][columna] ens dona els continguts d'una
                           //casella en particular
   //Consulta numero de files al tauler
   public int getBoardColumnCount(){
       return this.board[0].length;
   }
   //Consulta numero de columnes al tauler
   public int getBoardRowCount(){
       return this.board.length;
   }
   
   public CheckersData() {
         // Constructor.  Crea el tauler i el prepara per al joc
      this.board = new int[8][8];
      setUpGame();
   }
   
   public CheckersData(int[][] boardCopy) {
         // Constructor overide.  Crea el tauler com a copia d'un altre tauler
      this.board = boardCopy;      
   }
   
   public void setUpGame() {
       /* Configura el tauler de dames amb les dames a les seves possicions
            corresponents.
       NOTA: Les peçes nomes poden estar a caselles con fila %2 == columna %2
       Al començar el joc totes les caselles que poden contenir peçes a les 
       primeres 3 files presenten peçes BLACK i les últimes 3 files peçes RED
       */

      for (int row = 0; row < 8; row++) {
         for (int col = 0; col < 8; col++) {
            if ( row % 2 == col % 2 ) {
               if (row < 3)
                  board[row][col] = BLACK;
               else if (row > 4)
                  board[row][col] = RED;
               else
                  board[row][col] = EMPTY;
            }
            else {
               board[row][col] = EMPTY;
            }
         }
      }
   }  // end setUpGame()
   

   public int pieceAt(int row, int col) {
          // Retorna els continguts d'una casella identificada per fila i col.
       return board[row][col];
   }
   

   public void setPieceAt(int row, int col, int piece) {
        /* Assigna el contingut d'una casella identificada per fila i col.
       El contigut ha de ser EMPTY, RED, BLACK , RED_KING o BLACK_KING
       */
       board[row][col] = piece;
   }
   

   public void makeMove(CheckersMove move) {
       /* Executa un moviment, assumeix que el moviment es legal i no null*/
         
      makeMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
   }
   

   public void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
       /* Executa un moviment entre una casella d'origen (fila i columna) i una
       destí (fila i columna). Assumeix que el moviment es legal, Gestiona la
       eliminació de peçes si el moviment menja. Gestiona la transformació en
       reis quant s'arriba al final del tauler
       */         
      board[toRow][toCol] = board[fromRow][fromCol];
      board[fromRow][fromCol] = EMPTY;
      if (fromRow - toRow == 2 || fromRow - toRow == -2) {
         /* El moviment es un salt, elimina la peça menjada del tauler i
            salta sobre ella. Especifica fila i columna de la peça menjada
          */
         int jumpRow = (fromRow + toRow) / 2;  //La fila
         int jumpCol = (fromCol + toCol) / 2;  //La columna
         board[jumpRow][jumpCol] = EMPTY;      //Desapareix
      }
      if (toRow == 0 && board[toRow][toCol] == RED)
         board[toRow][toCol] = RED_KING;
      if (toRow == 7 && board[toRow][toCol] == BLACK)
         board[toRow][toCol] = BLACK_KING;
   }
   
   public CheckersData simMove(int fromRow, int fromCol, int toRow, int toCol){
        /* Similar a makemove pero simula el moviment a una copia del tauler
            en comptes de fer-lo. Després retorna el tauler perque el pugueu
            avaluar
       */           
        //Punts extra si expliqueu perque ho he fet aixi (pista: Java Sucks!)
        int[][] res = new int[board.length][board[0].length];
        for(int i=0; i<board.length; i++) {
            for(int j=0; j<board[i].length; j++){
                res[i][j]=board[i][j];
            }          
        }        
        CheckersData simTauler = new CheckersData(res);
        simTauler.makeMove(fromRow, fromCol, toRow, toCol);
        
        return simTauler;
   }

   public CheckersMove[] getLegalMoves(int player) {
          /* Retorna una llista que conte els moviments legals del jugador
             especificat al tauler. Si el jugador no te moviments legals,
             retorna null. El valor del jugador hauria de ser una de les constants
             RED o BLACK, si no sempre retorna null. Segons les regles (peçes
             obligades a menjar) la llista conte exclusivament moviments normals
             o be exclusivament salts.
       */

      if (player != RED && player != BLACK)
         return null;

      int playerKing;  //Constant que representa un rei (orgullo y satisfaccion)
      if (player == RED)
         playerKing = RED_KING;
      else
         playerKing = BLACK_KING;

      Vector moves = new Vector();  // Guarda els moviments com a vector
      
      /* Comença mirant si existeixen salts que siguin legals. Escaneja el tauler
      buscant peçes adjacents de diferents jugadors. Si la adjacencia es un salt
      válid (es comproba en totes les direccions per si una es un rei).
      Si troba un salt válid l'afageix a la llista de moviments válids)
      */

      for (int row = 0; row < 8; row++) {
         for (int col = 0; col < 8; col++) {
            if (board[row][col] == player || board[row][col] == playerKing) {
               if (canJump(player, row, col, row+1, col+1, row+2, col+2))
                  moves.addElement(new CheckersMove(row, col, row+2, col+2));
               if (canJump(player, row, col, row-1, col+1, row-2, col+2))
                  moves.addElement(new CheckersMove(row, col, row-2, col+2));
               if (canJump(player, row, col, row+1, col-1, row+2, col-2))
                  moves.addElement(new CheckersMove(row, col, row+2, col-2));
               if (canJump(player, row, col, row-1, col-1, row-2, col-2))
                  moves.addElement(new CheckersMove(row, col, row-2, col-2));
            }
         }
      }
      
      /*  Si s'ha trobat un salt (moves.size > 0) ja no mira els moviments normals
        Si no hi ha salts, els mira. Escaneja el tauler buscant peçes del jugador
      i comproba moviments en les 4 direccions (per si el rei). Si alguna de les
      direccions conté un moviment legal l'afageix a la llista          
      */
      
      if (moves.size() == 0) {
         for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
               if (board[row][col] == player || board[row][col] == playerKing) {
                  if (canMove(player,row,col,row+1,col+1))
                     moves.addElement(new CheckersMove(row,col,row+1,col+1));
                  if (canMove(player,row,col,row-1,col+1))
                     moves.addElement(new CheckersMove(row,col,row-1,col+1));
                  if (canMove(player,row,col,row+1,col-1))
                     moves.addElement(new CheckersMove(row,col,row+1,col-1));
                  if (canMove(player,row,col,row-1,col-1))
                     moves.addElement(new CheckersMove(row,col,row-1,col-1));
               }
            }
         }
      }
      
      /* Si no ha trobat moviments vàlids retorna null. Si els ha trobat, crea
        un vector suficientment gran com per guardar-hi els moviments. Copia
        els moviments legals al vector i el retorna */
      
      if (moves.size() == 0)
         return null;
      else {
         CheckersMove[] moveArray = new CheckersMove[moves.size()];
         for (int i = 0; i < moves.size(); i++)
            moveArray[i] = (CheckersMove)moves.elementAt(i);
         return moveArray;
      }

   }  // end getLegalMoves
   

   public CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
        /* Retorna una llista del salts legals associats al jugador especificat
       i a una fila i columna especificada. Retorna null si no hi troba salts
       possibles. Fa servir la mateixa llogica que getLegalMoves()
       */

      if (player != RED && player != BLACK)
         return null;
      int playerKing;  // Representa un rei
      if (player == RED)
         playerKing = RED_KING;
      else
         playerKing = BLACK_KING;
      Vector moves = new Vector();  // Guarda aqui els salts vàlids
      if (board[row][col] == player || board[row][col] == playerKing) {
         if (canJump(player, row, col, row+1, col+1, row+2, col+2))
            moves.addElement(new CheckersMove(row, col, row+2, col+2));
         if (canJump(player, row, col, row-1, col+1, row-2, col+2))
            moves.addElement(new CheckersMove(row, col, row-2, col+2));
         if (canJump(player, row, col, row+1, col-1, row+2, col-2))
            moves.addElement(new CheckersMove(row, col, row+2, col-2));
         if (canJump(player, row, col, row-1, col-1, row-2, col-2))
            moves.addElement(new CheckersMove(row, col, row-2, col-2));
      }
      if (moves.size() == 0)
         return null;
      else {
         CheckersMove[] moveArray = new CheckersMove[moves.size()];
         //A que mola com transforma un vector de tamany variable en un array fixe?
         for (int i = 0; i < moves.size(); i++)
            moveArray[i] = (CheckersMove)moves.elementAt(i);
         return moveArray;
      }
   }  // end getLegalMovesFrom()
   

   private boolean canJump(int player, int r1, int c1, int r2, int c2, int r3, int c3) {
        /* Funcion auxiliar per comprobar que es pot fer un salt vàlid per part 
       d'un jugador player entre un origen (fila r1, columna c1) i un destí (fila
       r3, columna c3) La casella (fila r2, columna c2) esta entre el origen 
       i el desti
       */           
           
      if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8)
         return false;  // Fila r3 columna c3 es surt del tauler
         
      if (board[r3][c3] != EMPTY)
         return false;  // Fila r3 columna c3 ja conté una peça
         
      if (player == RED) {
         if (board[r1][c1] == RED && r3 > r1)
            return false;  // Si no es un rei no pot anar enrere
         if (board[r2][c2] != BLACK && board[r2][c2] != BLACK_KING)
            return false;  // No hi ha una peça BLACK al mitj per menjar
         return true;  // YUPI!! Salt legal
      }
      else {
         if (board[r1][c1] == BLACK && r3 < r1)
            return false;  // Si no es un rei no pot anar enrere
         if (board[r2][c2] != RED && board[r2][c2] != RED_KING)
            return false;  // No hi ha una peça RED al mitj per menjar
         return true;  // YUPI!! Salt legal.
      }

   }  // end canJump()
   

   private boolean canMove(int player, int r1, int c1, int r2, int c2) {
       /* Funció auxiliar que comproba si un jugador player es pot moure
       entre una casella d'origen (fila r1, columna c1) i una destí (fila r2, 
       columna c2)
       */         
         
      if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8)
         return false;  //  Fila r2 columna c2 es surt del tauler
         
      if (board[r2][c2] != EMPTY)
         return false; //  Fila r2 columna c2 ja conte peça

      if (player == RED) {
         if (board[r1][c1] == RED && r2 > r1)
             return false;  // RED només pot baixar si no es rei
          return true;  // YUPI!! Moviment legal.
      }
      else {
         if (board[r1][c1] == BLACK && r2 < r1)
             return false;  // BLACK només pot pujar si no es rei
          return true;  // YUPI!! Moviment legal.
      }
      
   }  // end canMove()
   

} // end class CheckersData