import greenfoot.*;

/**
 * The door that takes the player to the next level. Is either locked or open depending on whether
 * there is a key on the screen. Only the graphics are handled in this class.
 * 
 * @author Aron Strandberg
 * @version 2019-01-25
 */
public class Door extends Objects
{
    public final GreenfootImage[] graphics = {
        new GreenfootImage("door_00.png"),
        new GreenfootImage("door_01.png"), 
    };
    
    public Door()
    {
        rescale(graphics);
        setImage(graphics[0]);
    }
    
    /**
     * Act - do whatever the Door wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        lockedOrOpen();
    }
    
    public void lockedOrOpen(){
        if(getWorld().getObjects(Key.class).size() > 0 && getImage().equals(graphics[0]))
        {
            setImage(graphics[1]);
        }
        
        if(getWorld().getObjects(Key.class).size() == 0 && getImage().equals(graphics[1]))
        {
            setImage(graphics[0]);
        }
    }

}
