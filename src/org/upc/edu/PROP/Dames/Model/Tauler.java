package org.upc.edu.PROP.Dames.Model;

/**
 *
 * @author Ignasi GómeSebastià
 * Basat en http://math.hws.edu/eck/cs124/javanotes3/source/Checkers.java
 */



import org.upc.edu.PROP.Dames.Jugadors.Jugador;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.Vector;


public class Tauler extends Applet {
    
    //Els dos jugadors al tauler
    Jugador j1;
    Jugador j2;
    
    public Tauler (Jugador j1, Jugador j2){
        this.j1 = j1;
        this.j2 = j2;
    }

   public void init() {
   
      setLayout(null);  // Crear tauler
   
      setBackground(new Color(0,150,0));  // Coloraines del tauler.
      
      /* Crea els components i els afageix al applet. hic sunt dracones */

      CheckersCanvas board = new CheckersCanvas(this.j1,this.j2);
      add(board);

      board.newGameButton.setBackground(Color.lightGray);
      add(board.newGameButton);

      board.resignButton.setBackground(Color.lightGray);
      add(board.resignButton);

      board.message.setForeground(Color.green);
      board.message.setFont(new Font("Serif", Font.BOLD, 14));
      add(board.message);
      
      /* El metode setBounds() permet posizionar i fixar el tamany dels components
         Punts extra si aconsegiu fer que el tauler tingui un tamany respectable
      */

      board.setBounds(20,20,164,164); // Nota:  O el tamany es 164*164 o catapum
      board.newGameButton.setBounds(210, 60, 100, 30);
      board.resignButton.setBounds(210, 120, 100, 30);
      board.message.setBounds(0, 200, 330, 30);
   }
   
} // end class Tauler




class CheckersCanvas extends Canvas implements ActionListener, MouseListener {
    
     /* El canvas mostra un tauler de dames de 160*160 amb un marge negre de 2
        pixels. S'assumeix que el tamany del tauler es de 164*164 pixels.
        La classe permet fer la partida d'escacs manual o auto i ensenya 
        el tauler
    */
    
    //Els dos jugadors al canvas, els passa el tauler
    Jugador j1;
    Jugador j2;
    
   Button resignButton;   // Per deixar la partida i rendirse (covards!)
   Button newGameButton;  // Crea un joc nou. Només actiu si el joc actual ha acabat                          
   
   Label message;   // Ensenya missatges no ofensius al usuari
   
   CheckersData board;  // Les dades del tauler es guarden aqui. Conte certa
                        // intel·ligencia, com generar la llista de moviments
                        // vàlids

   boolean gameInProgress; // Ens diu si la partida esta en marxa
   
   /* Aquestes variables només es poden accedir si la partida esta en marxa */
   
   int currentPlayer;      // A qui li toca. Els possibles valors son
                           // CheckersData.RED i CheckersData.BLACK.
   int selectedRow, selectedCol;  // Es fa servir per el control manual del tauler
                                  
   CheckersMove[] legalMoves;  // Un array que conte la llista de moviments 
                               // válids per el jugador actual
   /* END: Aquestes variables només es poden accedir si la partida esta en marxa */
   
   public CheckersCanvas(Jugador j1, Jugador j2) {
          // Constructor.  Crea butons i etiquetes. Captura els events de 
          // clicks al tauler i butons.  Crea el tauler amb els jugadors
          // corresponents i llença joc
      //Assigna jugadors
      this.j1 = j1;
      this.j2 = j2;
      //Configura tauler
      setBackground(Color.black);
      addMouseListener(this);
      setFont(new  Font("Serif", Font.BOLD, 14));
      resignButton = new Button("Ser un covard");
      resignButton.addActionListener(this);
      newGameButton = new Button("Nou Joc");
      newGameButton.addActionListener(this);
      message = new Label("",Label.CENTER);
      board = new CheckersData();
      doNewGame();
   }
   

   public void actionPerformed(ActionEvent evt) {
         // Implementa la funcionalitat als butons
      Object src = evt.getSource();
      if (src == newGameButton)
         doNewGame();
      else if (src == resignButton)
         doResign();
   }
   

   void doNewGame() {
         // Comença nou joc
      if (gameInProgress == true) {
             // No hauria de passar mai, pero per si de cas             
         message.setText("Primer acaba el joc anterior!");
         return;
      }
      board.setUpGame();   // Distribuir peçes
      currentPlayer = CheckersData.RED;   //Comença RED (communist power!)
      legalMoves = board.getLegalMoves(CheckersData.RED);  // Agafar llista de moviments legals de RED
      selectedRow = -1;   // RED Encara no ha triat moviment (per joc manual)
      message.setText("RED:  Et toca moure!");
      gameInProgress = true;
      newGameButton.setEnabled(false);
      resignButton.setEnabled(true);
      repaint();
   }
   

   void doResign() {
          // Algu es rendeix, guanya adversari
       if (gameInProgress == false) {
          // No hauria de passar mai, pero per si de cas  
          message.setText("Primer comença un joc!");
          return;
       }
       if (currentPlayer == CheckersData.RED)
          gameOver("RED es rendeix.  BLACK guanya");
       else
          gameOver("BLACK es rendeix.  RED guanya");
   }
   

   void gameOver(String str) {
       //S'acaba el joc. Mostra missatge al usuari i configura butons per
       //poder començar un nou joc
      message.setText(str);
      newGameButton.setEnabled(true);
      resignButton.setEnabled(false);
      gameInProgress = false;
   }
      

   void doClickSquare(int row, int col) {
    /* Aquesta funcion es crica quant es clica sobre una casella especifica
       del tauler. Ja s'ha comprobat que hi ha un joc en marxa
       Si el jugador es auto, clicar en qualsevol casella fa que es mogui
       
    /* Si el joc es manual, quant el jugador selecciona una peça li ensenyem
       la llista de moviments vàlids.
    */      
      
      //Per defecte es vermell, el cambiem si es negre
      Jugador j = this.j1;            
      if (currentPlayer == CheckersData.BLACK){
          j = this.j2;          
      }
      boolean auto = j.isAuto();
      if (auto){          
          doMakeMove(legalMoves[j.getJugada(legalMoves, this.board)]);
      }
      //Abans eren moviments automatics, aqui estàn els manuals
      else{
          for (int i = 0; i < legalMoves.length; i++)
         if (legalMoves[i].fromRow == row && legalMoves[i].fromCol == col) {
            selectedRow = row;
            selectedCol = col;
            if (currentPlayer == CheckersData.RED)
               message.setText("RED:  Make your move.");
            else
               message.setText("BLACK:  Make your move.");
            repaint();
            return;
         }

      /* Si el joc es manual, si el jugador no ha seleccionat cap peça per a moure
          li obliga a seleccionar una peça abans de seguir. Ensenyar un missatge
          mostrant l'estupidessa humana */

      if (selectedRow < 0) {
          message.setText("Selecciona peça a moure idiota.");
          return;
      }
      
      /* Si el joc es manual, si el usuari ha seleccionat una peça que te moviments
        legals, moure i retornar */

      for (int i = 0; i < legalMoves.length; i++)
         if (legalMoves[i].fromRow == selectedRow && legalMoves[i].fromCol == selectedCol
                 && legalMoves[i].toRow == row && legalMoves[i].toCol == col) {
            doMakeMove(legalMoves[i]);
            return;
         }
         
      /* Si arribem aqui, s'ha seleccionat una peça pero una casella vàlida on
         moure, mostrem un missatge resltant la estupidessa humana. */

         message.setText("Selecciona un destí per la peça idiota.");
      }
      

   }  // end doClickSquare()
   

   void doMakeMove(CheckersMove move) {
       //Es crida quant s'ha seleccionat (manual o auto) un moviment
       //El fem i continua o acaba el joc segons calgui
          
      board.makeMove(move);
      
      /* Regles americanes, si es un salt i menja peça li pot tornar a tocar si 
         te més salts. Comproba si hi ha mes salts desde la casella on el salt
         anterior acaba. També si es pot menjar està obligat a fer-ho
      */
      
      if (move.isJump()) {
         legalMoves = board.getLegalJumpsFrom(currentPlayer,move.toRow,move.toCol);
         if (legalMoves != null) {
            if (currentPlayer == CheckersData.RED)
               message.setText("RED:  Has de continuar saltant.");
            else
               message.setText("BLACK:  Has de continuar saltant.");
            selectedRow = move.toRow;  // Només la peça que ha saltat es pot moure
            selectedCol = move.toCol;
            repaint();
            return;
         }
      }
      
      /* El torn del jugador actual s'ha acabat, aixi que li toca al altre jugador
         Agafa la llista de moviments vàlids del següent jugador
         El joc acaba si un jugador no pot moure (no te fitxer o les te bloquejades)
         */
      
      if (currentPlayer == CheckersData.RED) {
         currentPlayer = CheckersData.BLACK;
         legalMoves = board.getLegalMoves(currentPlayer);
         if (legalMoves == null)
            gameOver("BLACK no pot moure.  RED guanya.");
         else if (legalMoves[0].isJump())
            message.setText("BLACK:  Et toca.  Has de saltar.");
         else
            message.setText("BLACK:  Et toca.");
      }
      else {
         currentPlayer = CheckersData.RED;
         legalMoves = board.getLegalMoves(currentPlayer);
         if (legalMoves == null)
            gameOver("RED no pots moure.  BLACK guanya.");
         else if (legalMoves[0].isJump())
            message.setText("RED:   Et toca.  Has de saltar.");
         else
            message.setText("RED:  Et toca.");
      }
      
      /* Si la variable selectedRow = -1 vol dir que el jugador encara no ha
         triat una peça per moure (només joc manual)
      */
      
      selectedRow = -1;
          
      if (legalMoves != null) {
         boolean sameStartSquare = true;
         for (int i = 1; i < legalMoves.length; i++)
            if (legalMoves[i].fromRow != legalMoves[0].fromRow
                                 || legalMoves[i].fromCol != legalMoves[0].fromCol) {
                sameStartSquare = false;
                break;
            }
         if (sameStartSquare) {
            selectedRow = legalMoves[0].fromRow;
            selectedCol = legalMoves[0].fromCol;
         }
      }
      
      /* Tornar a pintar el tauler en aquest punt per reflectir els cambis. */
      
      repaint();
      
   }  // end doMakeMove();
   

   public void update(Graphics g) {
       //Tornar a pintar el tauler per reflectir els cambis.
      paint(g);
   }
   

   public void paint(Graphics g) {
        // Pinta un tauler amb casseles grises i gris clar. Pinta les peçes
        //Si el joc esta en marxa, resalta els moviments vàlids per facilitar
        //joc manual
      
      /* Possem un marge negre de dos pixels perque mola */
      
      g.setColor(Color.black);
      g.drawRect(0,0,getSize().width-1,getSize().height-1);
      g.drawRect(1,1,getSize().width-3,getSize().height-3);
      
      /* Pinta les caselles del tauler i les peçes */
      
      for (int row = 0; row < 8; row++) {
         for (int col = 0; col < 8; col++) {
             if ( row % 2 == col % 2 )
                g.setColor(Color.lightGray);
             else
                g.setColor(Color.gray);
             g.fillRect(2 + col*20, 2 + row*20, 20, 20);
             switch (board.pieceAt(row,col)) {
                case CheckersData.RED:
                   g.setColor(Color.red);
                   g.fillOval(4 + col*20, 4 + row*20, 16, 16);
                   break;
                case CheckersData.BLACK:
                   g.setColor(Color.black);
                   g.fillOval(4 + col*20, 4 + row*20, 16, 16);
                   break;
                case CheckersData.RED_KING:
                   g.setColor(Color.red);
                   g.fillOval(4 + col*20, 4 + row*20, 16, 16);
                   g.setColor(Color.white);
                   g.drawString("K", 7 + col*20, 16 + row*20);
                   break;
                case CheckersData.BLACK_KING:
                   g.setColor(Color.black);
                   g.fillOval(4 + col*20, 4 + row*20, 16, 16);
                   g.setColor(Color.white);
                   g.drawString("K", 7 + col*20, 16 + row*20);
                   break;
             }
         }
      }
    /* Si hi ha un joc en marxa resalta els moviments legals per facilitar joc
      manual. Si legal moves es null vol dir que el joc ha acabat
      */
      
      if (gameInProgress) {
            // Resalta les peçes que es poden moure
         g.setColor(Color.cyan);
         for (int i = 0; i < legalMoves.length; i++) {
            g.drawRect(2 + legalMoves[i].fromCol*20, 2 + legalMoves[i].fromRow*20, 19, 19);
         }
            // Si una peça s'ha seleccionat pinta un marge al seu voltant i resalta
            //les caselles on la podem moure
         if (selectedRow >= 0) {
            g.setColor(Color.white);
            g.drawRect(2 + selectedCol*20, 2 + selectedRow*20, 19, 19);
            g.drawRect(3 + selectedCol*20, 3 + selectedRow*20, 17, 17);
            g.setColor(Color.green);
            for (int i = 0; i < legalMoves.length; i++) {
               if (legalMoves[i].fromCol == selectedCol && legalMoves[i].fromRow == selectedRow)
                  g.drawRect(2 + legalMoves[i].toCol*20, 2 + legalMoves[i].toRow*20, 19, 19);
            }
         }
      }
   }  // end paint()
   
   
   public Dimension getPreferredSize() {
         // Dimensions del tauler. Han de ser 164*164 o kaboom
      return new Dimension(164, 164);
   }


   public Dimension getMinimumSize() {
      return new Dimension(164, 164);
   }
   

   public void mousePressed(MouseEvent evt) {
       /* Captura l'event de que el usuari ha clicat al tauler. Els agents
          automàtics trien el seu moviment. Els manuals resalten peçes.
          Si el joc no esta en marxa ens incita a començar-lo. Troba la fila
          i columna corresponent per moure si el agent es manual
       */         
      if (gameInProgress == false)
         message.setText("Selecciona 'Nou Joc' per començar una nova partida");
      else {
         int col = (evt.getX() - 2) / 20;
         int row = (evt.getY() - 2) / 20;
         if (col >= 0 && col < 8 && row >= 0 && row < 8)
            doClickSquare(row,col);
      }
   }
   

   public void mouseReleased(MouseEvent evt) { }
   public void mouseClicked(MouseEvent evt) { }
   public void mouseEntered(MouseEvent evt) { }
   public void mouseExited(MouseEvent evt) { }


}  // end class SimpleCheckerboardCanvas









