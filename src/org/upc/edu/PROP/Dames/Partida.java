/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.upc.edu.PROP.Dames;

import org.upc.edu.PROP.Dames.Model.Tauler;
import org.upc.edu.PROP.Dames.Jugadors.Manual;
import org.upc.edu.PROP.Dames.Jugadors.Jugador;
import java.applet.Applet;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import org.upc.edu.PROP.Dames.Jugadors.Random;

/**
 *
 * @author Ignasi Góme Sebastià
Wrapper per configurar partides de dames en java
*/


public class Partida {
    public static void main(String[] args){
    //Configura interfície gràfica del tauler    
    JFrame frame = new JFrame();
    frame.setSize(400, 300);
    //Configura els jugadors
    Jugador j1 = new Random("Jugador1");
    //Jugador j2 = new MegaZero("Jugador2");
    //Jugador j2 = new Manual("Jugador2");
    Jugador j2 = new Random("Jugador2");
    
    //Crea el tauler i executa l'applet que l'implementa
    final Applet applet = new Tauler(j1,j2);
    frame.getContentPane().add(applet);
    frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent we) {
            applet.stop();
            applet.destroy();
            System.exit(0);
        }
    });
    frame.setVisible(true);
    applet.init();
    applet.start();
    }
}
