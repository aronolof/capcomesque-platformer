import greenfoot.*;

/**
 * The only enemy object in this game. Kills the player upon contact
 * 
 * @author Aron Strandberg
 * @version 2015-08-04
 */
public class Spikes extends Objects
{
    
    // Sprites
    public final GreenfootImage[] graphics = {
        new GreenfootImage("spikes_00.png"),
        new GreenfootImage("spikes_01.png"),
    };
    
    // Variables
    private int spiketype;
    private boolean moving = false;
    
    /**
     * These enemy objects are either fixed spikes or "mines" moving across the screen
     * @param  type  determines the enemy type
     * @param  rotation determines the initial direction
     */
        public Spikes(int type, int rotation)
    {
        rescale(graphics);
        setRotation(rotation);
        spiketype = type;
        if(type == 1){
            setImage(graphics[0]);
        }else{
            setImage(graphics[1]);
            moving = true;
        }
        if (type == 2) setRotation(0);
        else if (type == 3) setRotation(90);
        
    }
    
     /**
     * The enemies move straight and reverse direction if colliding with a platform
     */
    public void act() 
    {
        if(moving) move(1);
        if (getOneIntersectingObject(Platform.class) != null) turn(180);
    }
    
}
