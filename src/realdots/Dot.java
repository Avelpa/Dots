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
        LEFT, RIGHT, UP, DOWN
    }
    
    public Dot(int spaceWidth, int spaceHeight)
    {
        width = RealDots.randomInt(5, 100);
        height = RealDots.randomInt(5, 100);
        
//        Direction direction = Direction.values()[RealDots.randomInt(0, Direction.values().length-1)];
        Direction direction = RealDots.randomInt(0, 1) == 0 ? Direction.LEFT : Direction.RIGHT;
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
                
    }
    
    public void move()
    { 
        x += velX;
        y += velY;
    }
}
