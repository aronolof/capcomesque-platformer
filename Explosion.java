import greenfoot.*;

/**
 * These objects are used for the explosion animation when the player dies. It is also used in reverse when
 * spawning.
 * 
 * @author Aron Strandberg
 * @version 2015-08-04
 */
public class Explosion extends Objects
{
    
    // Sprites
    private GreenfootImage[] graphics = {
        new GreenfootImage("explo_01.png"),
    };
    
    private int whichType; // Not currently used
    private int lifespan = 22;
    
    /**
     * By creating a number of objects with different directions, an "explosion" is generated when dying.
     * @param  dir  determines the direction
     * @param  type  to be used in later developemtn
     */
    public Explosion(int dir, int type)
    {
        rescale(graphics);
        setRotation(dir);
        whichType = type; // Not currently used
    }
    
    /**
     * Act - do whatever the Explosion wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        explosion();
    }
    
    /**
     * More specific rules for the explosion animation
     */
    public void explosion(){
        if (whichType == 1){
            move(2);
            if(lifespan <= 0){
                Level.playerRespawn = true;
                getWorld().removeObject(this);
                }
            lifespan--;
            getImage().setTransparency(getImage().getTransparency()-2);
    }
    }
    
}