/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realdots;

/**
 *
 * @author kobed6328
 */
public class Dot {
    
    public int x, y, width, height;
    public int velX, velY;
    
    public static enum Direction {
        LEFT, RIGHT, UP, DOWN, CHAOS
    }
    
    public Dot(int spaceWidth, int spaceHeight)
    {
        width = RealDots.randomInt(10, 100);
        height = width;
        
        Direction megaDirection = Direction.values()[RealDots.randomInt(0, Direction.values().length-1)];
        Direction direction = Direction.values()[RealDots.randomInt(0, Direction.values().length-2)];
        
        if (direction == Direction.UP || direction == Direction.DOWN)
        {
            x = RealDots.randomInt(0, spaceWidth-width);
            if (direction == Direction.UP)
            {
                y = spaceHeight;
            } else {
                y = -height;
            }
        } else {
            y = RealDots.randomInt(0, spaceHeight-height);
            if (direction == Direction.LEFT)
            {
                x = spaceWidth;
            } else {
                x = -width;
            }
        }
        
        if (megaDirection != Direction.CHAOS)
        {
            int spd = RealDots.randomInt(5, 10);
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
            int spdX = RealDots.randomInt(5, 10);
            int spdY = RealDots.randomInt(5, 10);
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
