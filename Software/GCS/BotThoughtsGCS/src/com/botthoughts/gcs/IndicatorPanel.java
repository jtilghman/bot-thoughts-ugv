/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.botthoughts.gcs;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author mes
 */
public class IndicatorPanel extends JPanel {
    private Image image;
    private int x;
    private int y;
    
    public IndicatorPanel() {
        initComponents();
        x = 5;
        y = 5;
        this.setOpaque(false);
        System.out.println("IndicatorPanel constructor");
    }
    
    public void addIndicator(IndicatorLight indicator) {
        this.add(indicator);
        indicator.setLocation(x, y);
        indicator.setSize(new Dimension(this.getHeight()-10, this.getHeight()-10));
        x += this.getHeight();
        this.setVisible(true);
    }

    /** loads the specified image as an Image
     * 
     * @param filename is the name of the image file
     * @return Image loaded from the filename
     */
    private ImageIcon loadImage(String filename) {
        ImageIcon myImage = null;
        
        if (filename != null) {
            try {              
                URL url = getClass().getResource(filename);
                if (url != null) {
                    myImage = new ImageIcon(url);
                }
            } catch (Exception ex) {
                // handle exception...
                System.out.println("Error loading image: " + ex.getMessage().toString());
            }
        }
        return myImage;
    }
    
       
    /** Creates new form GaugePanel.
     * @param filename is the filename of the image to load
     */
    public void setImage(String filename) {
        image = loadImage(filename).getImage();
    }
    
    
    /**
     * 
     * @param g 
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        super.paintComponent(g);
        if (image != null) {
            g2d.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="initialize components">                          
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>                        
    // Variables declaration - do not modify                     
    // End of variables declaration                   

}
