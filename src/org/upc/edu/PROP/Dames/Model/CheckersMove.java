/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.upc.edu.PROP.Dames.Model;

/**
 *
 * @author Ignasi GómeSebastià
 */
public class CheckersMove {
    /* Aquest objecte representa un moviment al tauler. Guarda la fila i columna
        corresponents a les caselles de origen i destí de la peça. No garanteix
        que el moviment sigui vàlid, pero per simplificar els vostres agents ja
        tracten només amb moviments vàlids
    */    
   public int fromRow, fromCol;  // Position of piece to be moved.
   public int toRow, toCol;      // Square it is to move to.
   CheckersMove(int r1, int c1, int r2, int c2) {
        // Constructor.  Asigna els valors a les variables
      fromRow = r1;
      fromCol = c1;
      toRow = r2;
      toCol = c2;
   }
   boolean isJump() {
       // Mira si el moviment es un salt. Assumeix que el moviment es legal
      return (fromRow - toRow == 2 || fromRow - toRow == -2);
   }
   
   //Ensenya el moviment per consola
   void print(){
       String msg = String.format("fromRow '%d' fromCol '%d' toRow '%d' toCol '%d' ", fromRow, fromCol, toRow, toCol);
       System.out.println(msg);      
   }
   
}  // end class CheckersMove.