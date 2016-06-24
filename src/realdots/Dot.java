/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realdots;

import java.awt.Color;

/**
 *
 * @author kobed6328
 */
public class Dot {
    
    public int x, y, width, height;
    public int velX, velY;
    public Color color;
    
    public static enum Direction {
        LEFT, RIGHT, UP, DOWN, CHAOS
    }
    
    public Dot(int spawnWidth, int spawnHeight)
    {
        // random color
        color = new Color(RealDots.randomInt(1, 255), RealDots.randomInt(1, 255), RealDots.randomInt(1, 255));
        
        // size
        width = RealDots.randomInt(5, 100);
        height = width;
        
//        Direction megaDirection = Direction.values()[RealDots.randomInt(0, Direction.values().length-1)];
        Direction megaDirection = Direction.CHAOS;
        Direction direction = Direction.values()[RealDots.randomInt(0, Direction.values().length-2)];
        
        if (direction == Direction.UP || direction == Direction.DOWN)
        {
            x = RealDots.randomInt(0, spawnWidth-width);
            if (direction == Direction.UP)
            {
                y = spawnHeight;
            } else {
                y = -height;
            }
        } else {
            y = RealDots.randomInt(0, spawnHeight-height);
            if (direction == Direction.LEFT)
            {
                x = spawnWidth;
            } else {
                x = -width;
            }
        }
        
        if (megaDirection != Direction.CHAOS)
        {
            int spd = generateSpeed();
            switch (direction){
                case LEFT:
                    velX = -spd;
                    break;
                case RIGHT:
                    velX = spd;
                    break;
                case UP:
                    velY = -spd;
                    break;
                case DOWN:
                    velY = spd;
                    break;
            }
        } else {
            int spdX = 0, spdY = 0;
            while (spdX == 0 && spdY == 0)
            {
                spdX = generateSpeed();
                spdY = generateSpeed();
            }
            
            switch (direction){
                case LEFT:
                    velX = -spdX;
                    velY = RealDots.randomInt(0, 1) == 1 ? spdY : -spdY;
                    break;
                case RIGHT:
                    velX = spdX;
                    velY = RealDots.randomInt(0, 1) == 1 ? spdY : -spdY;
                    break;
                case UP:
                    velY = -spdY;
                    velX = RealDots.randomInt(0, 1) == 1 ? spdX : -spdX;
                    break;
                case DOWN:
                    velY = spdY;
                    velX = RealDots.randomInt(0, 1) == 1 ? spdX : -spdX;
            }
        }
    }
    
    private int generateSpeed(){
        int chance = RealDots.randomInt(1, 10);
        // 90%
        if (chance <= 8)
            return RealDots.randomInt(1, 5);
        if (chance <= 9)
            return RealDots.randomInt(6, 10);
        return RealDots.randomInt(11, 20);
    }
    
    public void move()
    { 
        x += velX;
        y += velY;
    }
    
    @Override
    public String toString(){
        return "pos:(" + x + "," + y + ") size:(" + width + "," + height + ") vel:(" + velX + "," + velY + ")";
    }
}
