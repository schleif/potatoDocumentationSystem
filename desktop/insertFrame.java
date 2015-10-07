import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import java.net.HttpURLConnection;
import java.net.URL;

import java.io.InputStream;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Iterator;

/**
  *
  * Beschreibung
  *
  * @version 1.0 vom 07.10.2015
  * @author 
  */

public class insertFrame extends JFrame {
  // Anfang Attribute
  int frameWidth = 300; 
  int frameHeight = 300;
  
  // Ende Attribute
  
  public insertFrame(String title, String[] values, String service) { 
    // Frame-Initialisierung
    super(title);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setSize(frameWidth, frameHeight);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (d.width - getSize().width) / 2;
    int y = (d.height - getSize().height) / 2;
    setLocation(x, y);
    setResizable(true);
    Container cp = getContentPane();
    cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));
    // Anfang Komponenten
    String urlstring = "";
    LinkedList<JTextArea> textAreas = new LinkedList<JTextArea>();    
    LinkedList<JTextField> textFields = new LinkedList<JTextField>();
    for (int i = 0 ; i < values.length; i++) {
      JTextArea ta = new JTextArea();
      ta.setText(values[i]);
      ta.setMaximumSize(new Dimension(frameWidth, 30));
      ta.setEditable(false);
      textAreas.add(ta);      
      cp.add(ta);
      
      JTextField tf = new JTextField();  
      tf.setMaximumSize(new Dimension(frameWidth, 30));    
      cp.add(tf);
      textFields.add(tf); 
    } // end of for
    JButton but = new JButton();
    but.setText("Sende Daten");
    cp.add(but);
    JTextArea erg = new JTextArea();
    erg.setEditable(false);
    erg.setMaximumSize(new Dimension(frameWidth, 100));
    cp.add(erg);
    but.addActionListener(new ActionListener() { 
      public void actionPerformed(ActionEvent evt) { 
        String url_str = "http://134.169.47.160/"
        + service + "?";
        Iterator itr1 = textAreas.listIterator();
        Iterator itr2 = textFields.listIterator();
        
        while (itr1.hasNext()) { 
          JTextArea ta = (JTextArea) itr1.next();
          JTextField tf = (JTextField) itr2.next();
          
          url_str = url_str + ta.getText() + "=" + tf.getText();
        } // end of while
        
        try {
          URL url = new URL(url_str);
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
          InputStream res = conn.getInputStream();
          Scanner s = new Scanner(res);
          
          while (s.hasNext()) {
            erg.append(s.next()+ "\n");
          } // end of while
        } catch(Exception e) {
          e.printStackTrace();
        } finally {
          
        } // end of try
        
        
      }
      
      
    });
    
    
    // Ende Komponenten
    
    setVisible(true);
  } // end of public insertFrame
  
  // Anfang Methoden
  
  public static void main(String[] args) {
    String[] values = {
    "eig_name"
    };
    
    new insertFrame("insertFrame", values, "insertEigenschaft.php");
  } // end of main
  
  
  // Ende Methoden
} // end of class insertFrame
