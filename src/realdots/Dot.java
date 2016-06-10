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
    public int speed;
    public Direction direction;
    
    public static enum Direction {
        LEFT, RIGHT, UP, DOWN
    }
    
    public Dot(int spaceWidth, int spaceHeight)
    {
        width = RealDots.randomInt(5, 100);
        height = RealDots.randomInt(5, 100);
        
        direction = Direction.values()[RealDots.randomInt(0, Direction.values().length-1)];
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
        
        speed = RealDots.randomInt(10, 20);
    }
    
    public void move()
    {
        switch (direction){
            case LEFT:
                x -= speed;
                break;
            case RIGHT:
                x += speed;
                break;
            case UP:
                y -= speed;
                break;
            case DOWN:
                y += speed;
                break;
        }
    }
}
