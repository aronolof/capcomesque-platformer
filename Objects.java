import greenfoot.*;

/**
 * Write a description of class Objects here.
 * 
 * @author Aron Strandberg
 * @version 2015-08-04
 */
public class Objects extends Actor
{
    /**
     * Act - do whatever the Objects wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }
    
    /**
    * Doubles the size of all objects in the game from 16x16 to 32x32. This applies to all graphic in the game.
    * Assumes that all sprites in /images are 16x16 pixels
    * 
    * @param  graphics  An array with all the images that are used in this class
    **/
    public void rescale(GreenfootImage[] graphics){
    for(int i = 0; i < graphics.length; i++){
        graphics[i].scale(32,32);
    }
    }
    
}
