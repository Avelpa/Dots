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
    
    Dot newDot;
    public void spawnDot(){
        
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
        
        // finding X1 <= X2 + width2
        int[] timeXBefore = new int[2];
        // finding X2 >= X1 + width1
        int[] timeXAfter = new int[2];
        
        int[] timeX = new int[2];
        
        // finding Y1 <= Y2 + height2
        int[] timeYBefore = new int[2];
        // finding Y2 >= Y1 + height1
        int[] timeYAfter = new int[2];
        
        int[] timeY = new int[2];
        
        // if their speeds are the same, it's either always intersect or never, so check for that first
        if (newDot.velX == otherDot.velX){
            if (newDot.x > otherDot.x + otherDot.width)
                return true;
            if (otherDot.x > newDot.x + newDot.width)
                return true;
            
            timeX[0] = 0;
            timeX[1] = Integer.MAX_VALUE;
            
        }
        if (newDot.velY == otherDot.velY){
            if (newDot.y > otherDot.y + otherDot.height)
                return true;
            if (otherDot.y > newDot.y + newDot.height)
                return true;
            
            timeY[0] = 0;
            timeY[1] = Integer.MAX_VALUE;
        }
        
        
        int t;
        
        ///////// X
        if (newDot.velX != otherDot.velX){
            // finding the time of intersection between x1 and x2+width2
            t = getIntersectTime(newDot.x, newDot.velX, otherDot.x+otherDot.width, otherDot.velX);
            
            // if in one more tick the x values are still intersecting, then it's from t onwards
            if ((t+1)*newDot.velX + newDot.x < (t+1)*otherDot.velX + otherDot.x + otherDot.width){
                timeXBefore[0] = t;
                timeXBefore[1] = Integer.MAX_VALUE;
            } else {
                timeXBefore[0] = -Integer.MAX_VALUE;
                timeXBefore[1] = t;
            }
            
            // finding the time of intersection between x2 and x1+width1
            t = (newDot.x - otherDot.x + newDot.width)/(otherDot.velX - newDot.velX);
            // if in one more tick the x values are still intersecting, then it's from t onwards
            if ((t+1)*otherDot.velX + otherDot.x < (t+1)*newDot.velX + newDot.x + newDot.width){
                timeXAfter[0] = t;
                timeXAfter[1] = Integer.MAX_VALUE;
            } else {
                timeXAfter[0] = -Integer.MAX_VALUE;
                timeXAfter[1] = t;
            }
            
            timeX = getIntersection(timeXBefore, timeXAfter);
        }
        
       
        ////////////////// Y
        if (newDot.velY != otherDot.velY){
            // finding the time of intersection between x1 and x2+width2
            t = (otherDot.y - newDot.y + otherDot.height)/(newDot.velY - otherDot.velY);
            // if in one more tick the x values are still intersecting, then it's from t onwards
            if ((t+1)*newDot.velY + newDot.y < (t+1)*otherDot.velY + otherDot.y + otherDot.height){
                timeYBefore[0] = t;
                timeYBefore[1] = Integer.MAX_VALUE;
            } else {
                timeYBefore[0] = -Integer.MAX_VALUE;
                timeYBefore[1] = t;
            }
            
            // finding the time of intersection between x2 and x1+width1
            t = (newDot.y - otherDot.y + newDot.height)/(otherDot.velY - newDot.velY);
            // if in one more tick the x values are still intersecting, then it's from t onwards
            if ((t+1)*otherDot.velY + otherDot.y < (t+1)*newDot.velY + newDot.y + newDot.height){
                timeYAfter[0] = t;
                timeYAfter[1] = Integer.MAX_VALUE;
            } else {
                timeYAfter[0] = -Integer.MAX_VALUE;
                timeYAfter[1] = t;
            }
            
            timeY = getIntersection(timeYBefore, timeYAfter);
        }
        
//        System.out.println(timeY[0] + " " + timeY[1]);
        
        int[] time = getIntersection(timeX, timeY);
        
//        System.out.println("ergo: " + time[0] + " " + time[1]);
        
        if (time[1] < time[0]) 
            return true;
        if (time[0] == 0)
            return false;
        
        int[] validIntersectTime1 = getIntersection(getScreenTime(newDot), time);
        int[] validIntersectTime2 = getIntersection(getScreenTime(otherDot), time);
        
        
        if (validIntersectTime1[0] > validIntersectTime1[1] || validIntersectTime2[0] > validIntersectTime2[1])
            return true;
        return false;
        
        /*
        if (time[0]*newDot.velX + newDot.x <= WIDTH && time[0]*newDot.velX + newDot.x + newDot.width >= 0 &&
            time[0]*newDot.velY + newDot.y <= HEIGHT && time[0]*newDot.velY + newDot.y + newDot.height >= 0 &&
            time[0]*otherDot.velX + otherDot.x <= WIDTH && time[0]*otherDot.velX + otherDot.x + otherDot.width >= 0 && 
            time[0]*otherDot.velY + otherDot.y <= HEIGHT && time[0]*otherDot.velY + otherDot.y + otherDot.height >= 0){
            
            return false;
        }
        if (time[1]*newDot.velX + newDot.x <= WIDTH && time[1]*newDot.velX + newDot.x + newDot.width >= 0 &&
            time[1]*newDot.velY + newDot.y <= HEIGHT && time[1]*newDot.velY + newDot.y + newDot.height >= 0 &&
            time[1]*otherDot.velX + otherDot.x <= WIDTH && time[1]*otherDot.velX + otherDot.x + otherDot.width >= 0 && 
            time[1]*otherDot.velY + otherDot.y <= HEIGHT && time[1]*otherDot.velY + otherDot.y + otherDot.height >= 0){
            
            return false;
        }*/
        
        
        //System.out.println(newDot + " ||| " + otherDot + " " + time[0] + " " + time[1]);
        
    }
    
    // returns the time interval at which a point with a speed is before or equal to another point with a speed
    public int[] getTimePeriodLessThanOrEqualTo(int xBefore, int spdBefore, int xAfter, int spdAfter){
        
        if (spdBefore == spdAfter){
            // never before or equal to, since speeds are same
            if (xBefore > xAfter)
                return null;
            return new int[] {0, Integer.MAX_VALUE};
        }
        
        int intersectTime = (xAfter-xBefore)/(spdBefore-spdAfter);
        
        if (xBefore < xAfter)
            return new int[] {-Integer.MAX_VALUE, intersectTime};
        if (xBefore > xAfter)
            return new int[] {intersectTime, Integer.MAX_VALUE};
        // if it's before in one more game tick
        if ((intersectTime+1)*spdBefore + xBefore < (intersectTime+1)*spdAfter+xAfter)
            return new int[] {intersectTime, Integer.MAX_VALUE};
        return new int[] {-Integer.MAX_VALUE, intersectTime};
    }

    /// probably need to delete!
    public int getIntersectTime(int x1, int spd1, int x2, int spd2)
    {
        return (x2-x1)/(spd1-spd2);
    }
    
    private int[] getScreenTime(Dot dot)
    {
        int[] xTime = null;
        if (dot.velX != 0) {
            xTime = new int[2];
            xTime[0] = (-dot.x - dot.width)/dot.velX;
            xTime[1] = (WIDTH-dot.x)/dot.velX;
            if (xTime[0] > xTime[1]){
                int larger = xTime[0];
                xTime[0] = xTime[1];
                xTime[1] = larger;
            }
        }
        int[] yTime = null;
        if (dot.velY != 0) {
            yTime = new int[2];
            yTime[0] = (-dot.y - dot.height)/dot.velY;
            yTime[1] = (HEIGHT-dot.y)/dot.velY;
            if (yTime[0] > yTime[1]){
                int larger = yTime[0];
                yTime[0] = yTime[1];
                yTime[1] = larger;
            }
        }
        int[] screenTime = new int[2];
        if (xTime == null){
            if (yTime == null){
                screenTime[0] = 1;
                screenTime[1] = -1;
            } else {
                screenTime = yTime;
            }
        } else  if (yTime == null){
            screenTime = xTime;
        } else {
            screenTime = getIntersection(xTime, yTime);
        }
        
        return screenTime;
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
    
    int trailLength = 9;
    
    Color backgroundColor = new Color(200, 200, 200);
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
                spawnDot = false;
            }
            
            if (!paused){
            
                for (int i = 0; i < numSpawns; i ++)
                    spawnDot();
                
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
        }
        
        if (ke.getKeyChar() == 'r'){
            clearAll = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }
}
