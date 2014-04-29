/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorfulwolf.webcamapplet.gui;

import com.colorfulwolf.webcamapplet.CVImageProcessor;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael Shimniok
 */
@SuppressWarnings("serial")
public class OpenCVCamPanel extends javax.swing.JPanel {

    private Image img;
    private boolean center = true;
    private boolean running = false;
    private Thread runner = null;
    private OpenCVFrameGrabber grabber;
    private CVImageProcessor imageProcessor = null;
    
    /**
     * Creates new form ImgPanel
     */
    public OpenCVCamPanel() {
        initComponents();
        this.grabber = new OpenCVFrameGrabber(0);
        grabber.setImageWidth(this.getWidth());
        grabber.setImageHeight(this.getHeight());
        grabber.setFrameRate(15.0);
        this.setBackground(Color.white);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (img == null) {
            return;
        }

        if (center) {
            Dimension size = this.getSize();
            int width = img.getWidth(this);
            int height = img.getHeight(this);

            g.drawImage(img, size.width / 2 - width / 2, size.height / 2 - height / 2, this);
        } else {
            g.drawImage(img, 0, 0, this);
        }
    }
    
    public double getFps() {
        return grabber.getFrameRate();
    }
    
    public void setFps(int fps) {
        grabber.setFrameRate((double) fps);
    }

    public void setImage(Image img) {
        this.img = img;
    }

    public void setImageProcessor(CVImageProcessor imageProcessor) {
        this.imageProcessor = imageProcessor;
    }

    public CVImageProcessor getImageProcessor() {
        return imageProcessor;
    }

    private void clearAndPaint() {
        this.setImage(null);
        this.repaint();
    }
    
    private void grabAndPaint() {
        BufferedImage out = null;

        //grab the raw image from the webcam
        opencv_core.IplImage frame = null;

        try {
            frame = grabber.grab();
            //output the final result as a bufferedimage
            out = frame.getBufferedImage();
        } catch (FrameGrabber.Exception ex) {
            Logger.getLogger(OpenCVCamPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setImage(out);
        this.repaint();
    }

    /**
     * Start grabbing frames from the webcam.
     *
     * @throws Exception
     */
    public void start() throws Exception {
        if (running) {
            return;
        }

        grabber.start();

        running = true;
        runner = new Thread() {

            @Override
            public void run() {
                System.out.println("runner is running");
                while (running) {
                    grabAndPaint();
                }

                clearAndPaint();
                try {
                    grabber.stop();
                } catch (FrameGrabber.Exception ex) {
                    Logger.getLogger(OpenCVCamPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                runner = null;
            }
        };
        runner.start();
    }

    public boolean isRunnning() {
        return runner != null;
    }

    public void stop() {
        running = false;
    }    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));

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
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

