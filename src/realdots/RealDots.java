/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realdots;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
public class RealDots extends JComponent implements MouseListener, KeyListener {
    
    private final int WIDTH = 1200, HEIGHT = 700;
    private JFrame frame;
    
    boolean spawnDot = false;
    int numSpawns = 0;
    final int superSpawns = 1000;
    boolean paused = false;
    boolean clearAll = false;
    boolean auto = false;
    
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
        frame.addKeyListener(this);
    }
    
    public static void main(String[] args){
        RealDots main = new RealDots();
        main.run();
    }
    
    public void spawnDot(){
        
        Dot newDot;
        
        for (int i = 0; i < 100; i ++){
            newDot = new Dot(WIDTH, HEIGHT);
            for (Dot otherDot: dots){
                if (!validSpawn(newDot, otherDot)){
                    newDot = null;
                    break;
                }
            }
            if (newDot != null) {
                dots.add(newDot);
                break;
            }
        }
    }
    
    public boolean validSpawn(Dot newDot, Dot otherDot){
        
        int[] timeBefore, timeAfter;
        
        // never intersect x
        timeBefore = getTimePeriodLessThanOrEqualTo(newDot.x, newDot.velX, otherDot.x + otherDot.width, otherDot.velX);
        if (timeBefore == null)
            return true;
        
        timeAfter = getTimePeriodLessThanOrEqualTo(otherDot.x, otherDot.velX, newDot.x + newDot.width, newDot.velX);
        if (timeAfter == null)
            return true;
        
        int[] timeIntersectX = getIntersection(timeBefore, timeAfter);
        
        // never intersect y
        timeBefore = getTimePeriodLessThanOrEqualTo(newDot.y, newDot.velY, otherDot.y + otherDot.height, otherDot.velY);
        if (timeBefore == null)
            return true;
        
        timeAfter = getTimePeriodLessThanOrEqualTo(otherDot.y, otherDot.velY, newDot.y + newDot.height, newDot.velY);
        if (timeAfter == null)
            return true;
        
        int[] timeIntersectY = getIntersection(timeBefore, timeAfter);
        
        int[] intersectTime = getIntersection(timeIntersectX, timeIntersectY);
        // never intersect
        if (intersectTime == null)
            return true;
        
        
        int[] onScreenIntersectionTime1 = getIntersection(getScreenTime(newDot), intersectTime);
        int[] onScreenIntersectionTime2 = getIntersection(getScreenTime(otherDot), intersectTime);
        
        if (onScreenIntersectionTime1 == null || onScreenIntersectionTime2 == null)
            return true;
        return false;
    }
    
    // returns the time interval at which a point with a speed is before or equal to another point with a speed
    public int[] getTimePeriodLessThanOrEqualTo(int x, int spd, int xTarget, int spdTarget){
        
        if (spd == spdTarget){
            // never before or equal to, since speeds are same
            if (x > xTarget)
                return null;
            return new int[] {0, Integer.MAX_VALUE};
        }
        
        int intersectTime = (xTarget-x)/(spd-spdTarget);
        
        // if it's before in one more game tick
        if ((intersectTime+1)*spd + x < (intersectTime+1)*spdTarget+xTarget)
            return new int[] {intersectTime, Integer.MAX_VALUE};
        return new int[] {-Integer.MAX_VALUE, intersectTime};
    }

    private int[] getScreenTime(Dot dot)
    {
        int[] timeBefore;
        int[] timeAfter;
        
        // get x screen-time
        timeBefore = getTimePeriodLessThanOrEqualTo(dot.x, dot.velX, WIDTH, 0);
        timeAfter = getTimePeriodLessThanOrEqualTo(0, 0, dot.x + dot.width, dot.velX);
        
        int[] timeOnScreenX = getIntersection(timeBefore, timeAfter);
        
        // get y screen-time
        timeBefore = getTimePeriodLessThanOrEqualTo(dot.y, dot.velY, HEIGHT, 0);
        timeAfter = getTimePeriodLessThanOrEqualTo(0, 0, dot.y + dot.height, dot.velY);
        
        int[] timeOnScreenY = getIntersection(timeBefore, timeAfter);
        
        return getIntersection(timeOnScreenX, timeOnScreenY);
    }
            
            
    private int[] getIntersection(int[] arr1, int[] arr2){
        int[] newArr = new int[2];
        newArr[0] = arr1[0] > arr2[0] ? arr1[0] : arr2[0];
        newArr[1] = arr1[1] < arr2[1] ? arr1[1] : arr2[1];
        
        if (newArr[0] > newArr[1])
            return null;
        return newArr;
    }
    
    public static int randomInt(int min, int max)
    {
        return (int)(Math.random()*(max-min+1)) + min;
    }
    
    Iterator<Dot> dotIt;
    Dot curDot;
    
    int trailLength = 9;
    
    Color pausedColor = Color.BLACK;
    Color runColor = new Color(200, 200, 200);
    Color backgroundColor = runColor;
    @Override 
    public void paintComponent(Graphics g)
    {
        g.setColor(backgroundColor);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
//        dotIt = dots.iterator();
//        while (dotIt.hasNext())
//        {
//            curDot = dotIt.next();
//            int trailX = curDot.x;
//            int trailY = curDot.y;
//            for (int i = 1; i <= trailLength; i ++){
//                trailX -= curDot.velX;
//                trailY -= curDot.velY;
//                g.setColor(new Color(200, 200/trailLength*i, 200/trailLength*i));
//                g.fillRect(trailX, trailY, curDot.width, curDot.height);
//            }
//        }
        
        dotIt = dots.iterator();
        while (dotIt.hasNext())
        {
            curDot = dotIt.next();
            g.setColor(Color.BLACK);
            g.fillRect(curDot.x, curDot.y, curDot.width, curDot.height);
            g.setColor(curDot.color);
            g.fillRect(curDot.x+2, curDot.y+2, curDot.width-4, curDot.height-4);
        }
        
        g.setColor(Color.BLUE);
        g.drawString(Integer.toString(dots.size()), 100, 100);
    }
    
    public void run(){
        
        boolean done = false;
        
        while (!done){
            
            if (clearAll){
                dots.clear();
                clearAll = false;
            }
            
            if (spawnDot)
            {
                for (int i = 0; i < numSpawns; i ++)
                    spawnDot();
                if (!auto)
                    spawnDot = false;
            }
            
            if (!paused){
            
                dotIt = dots.iterator();
                while (dotIt.hasNext())
                {
                    curDot = dotIt.next();
                    curDot.move();

                    if (curDot.x+curDot.width <= 0 || curDot.x >= WIDTH || curDot.y+curDot.height <= 0 || curDot.y >= HEIGHT){
                        dotIt.remove();
                    }
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
        numSpawns = superSpawns;
        spawnDot = true;
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

    @Override
    public void keyTyped(KeyEvent ke) {
        
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (Character.isDigit(ke.getKeyChar())){
            numSpawns = Character.getNumericValue(ke.getKeyChar());
            spawnDot = true;
        }
        
        if (ke.getKeyCode() == KeyEvent.VK_SPACE){
            paused = paused ? false : true;
            backgroundColor = paused ? pausedColor : runColor;
        }
        
        if (ke.getKeyChar() == 'r'){
            clearAll = true;
        }
        
        if (ke.getKeyChar() == 'a') {
            spawnDot = true;
            auto = auto ? false : true;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }
}
