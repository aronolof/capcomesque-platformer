import greenfoot.*;

/**
 * When the player is in the water, the physics are somewhat modified. The jumping strength is lower,
 * as well as the max acceleration. The number of jumps are unlimited, which allows "swimming" by
 * pressing the jump button repeatedly.
 * 
 * @author Aron Strandberg
 * @version 2015-08-04
 */
public class Water extends Objects
{
    // Sprites
    public final GreenfootImage[] graphics = {
        new GreenfootImage("water_01.png"),  // Surface animation sprite 1
        new GreenfootImage("water_02.png"),  // Surface animation sprite 2
        new GreenfootImage("water_03.png"),  // Under surface
    };
    
    // Variables
    private double animationLoop = 0;
    private double animationSpeed = 0.06;
    private boolean isSurface;
    
    /**
    * @param  type  is determined in the level generator. The surface is an animation, but the underwater water
    * is a simple square.
    */
    public Water(int type)
    {
        rescale(graphics);
        setImage(graphics[0]);
        
        if(type == 1) isSurface = true; // if the water is the surface layer
        if(type == 2) setImage(graphics[2]); // if the water is a simple underwater square
    }
    
    /**
     * Act - do whatever the Water wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(isSurface) animLoop();
    }
    
    /**
     * A simple water animation
     */
    public void animLoop()
    {
    if ((int) animationLoop == 0) setImage(graphics[0]);
    if ((int) animationLoop == 1) setImage(graphics[1]);
    
    animationLoop += animationSpeed;
    if (animationLoop >= 2) animationLoop = 0;
    }
}
