/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realdots;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFrame;


/**
 *
 * @author kobed6328
 */
public class RealDots extends JComponent implements MouseListener {
    
    private final int WIDTH = 800, HEIGHT = 800;
    private JFrame frame;
    
    boolean mouseClicked = false;
    
    private ArrayList<Dot> dots = new ArrayList();
    
    private final int FPS = 60;
    
    public RealDots(){
        frame = new JFrame("Dots");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.pack();
        frame.setVisible(true);
        this.addMouseListener(this);
    }
    
    public static void main(String[] args){
        RealDots main = new RealDots();
        main.run();
    }
    
    public void spawnDot(){
        
        Dot newDot = null;
        for (int i = 0; i < 100; i ++){
            newDot = new Dot(WIDTH, HEIGHT);
            for (Dot otherDot: dots){
                if (!validSpawn(newDot, otherDot))
                    return;
            }
        }
        dots.add(newDot);
    }
    
    public boolean validSpawn(Dot newDot, Dot otherDot){
        
        
        /// checking for 0 speeds.... what the fuk man
        // if their speeds are the same, it's either always intersect or never
        if (newDot.velX == otherDot.velX){
            System.out.println(newDot.velX);
            if (otherDot.x > newDot.x + newDot.width)
                return true;
            if (newDot.x > otherDot.x + otherDot.width)
                return true;
            return false;
        }
        if (newDot.velY == otherDot.velY){
            if (newDot.y + newDot.height < otherDot.y)
                return true;
            if (newDot.y > otherDot.y + otherDot.height)
                return true;
            return false;
        }
        
        int t;
        
        // finding X1 <= X2 + width2
        int[] timeXBefore = new int[2];
       
        // finding the time of intersection between x1 and x2+width2
        t = (otherDot.x - newDot.x + otherDot.width)/(newDot.velX - otherDot.velX);
        // if the dots are initially intersecting
        if (t == 0)
            return false;
        // since the movement is linear, it's simple to solve for the inequality
        if (newDot.x < otherDot.x + otherDot.width){
            timeXBefore[0] = -Integer.MAX_VALUE;
            timeXBefore[1] = t;
        } else {
            timeXBefore[0] = t;
            timeXBefore[1] = Integer.MAX_VALUE;
        }
        
        
        // finding X2 >= X1 + width1
        int[] timeXAfter = new int[2];
        
        // finding the time of intersection between x1 and x2+width2
        t = (newDot.x - otherDot.x + newDot.width)/(otherDot.velX - newDot.velX);
        // if the dots are initially intersecting
        if (t == 0)
            return false;
        // since the movement is linear, it's simple to solve for the inequality
        if (otherDot.x > newDot.x + newDot.width){
            timeXBefore[0] = -Integer.MAX_VALUE;
            timeXBefore[1] = t;
        } else {
            timeXBefore[0] = t;
            timeXBefore[1] = Integer.MAX_VALUE;
        }
        
        
        
        int[] timeX = getIntersection(timeXBefore, timeXAfter);
        System.out.println(timeX[0] + " " + timeX[1]);
        
        
        return true;
    }
    
    private int[] getIntersection(int[] arr1, int[] arr2){
        int[] newArr = new int[2];
        newArr[0] = arr1[0] > arr2[0] ? arr1[0] : arr2[0];
        newArr[1] = arr1[1] < arr2[1] ? arr1[1] : arr2[1];
        
        return newArr;
    }
    
    public static int randomInt(int min, int max)
    {
        return (int)(Math.random()*(max-min+1)) + min;
    }
    
    Iterator<Dot> dotIt;
    Dot curDot;
    
    @Override
    public void paintComponent(Graphics g)
    {
        g.clearRect(0, 0, WIDTH, HEIGHT);
        dotIt = dots.iterator();
        while (dotIt.hasNext())
        {
            curDot = dotIt.next();
            g.fillRect(curDot.x, curDot.y, curDot.width, curDot.height);
        }
    }
    
    public void run(){
        
        boolean done = false;
        
        while (!done){
            if (mouseClicked)
            {
                spawnDot();
                mouseClicked = false;
            }
            
            dotIt = dots.iterator();
            while (dotIt.hasNext())
            {
                curDot = dotIt.next();
                curDot.move();
                
                if (curDot.x+curDot.width <= 0 || curDot.x >= WIDTH || curDot.y+curDot.height <= 0 || curDot.y >= HEIGHT){
                    dotIt.remove();
                }
            }
            
            repaint();
            try {
                Thread.sleep(1000/FPS);
            } catch (InterruptedException ex) {
                Logger.getLogger(RealDots.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseClicked = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
