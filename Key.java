import greenfoot.*;

/**
 * When the key is picked up, the door to the next level is unlocked. If the player dies, the key is dropped
 * and respawns.
 * 
 * @author Aron Strandberg
 * @version 2015-08-04
 */
public class Key extends Objects
{
    public final GreenfootImage[] graphics = {
        new GreenfootImage("key_00.png"),
    };
    
    public Key(){
        rescale(graphics);
        setImage(graphics[0]);
    }
    
    /**
     * Act - do whatever the Key wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }    
}
