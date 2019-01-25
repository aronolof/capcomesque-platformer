import greenfoot.*;

/**
 * Platforms are the objects that the player can walk and run on. If they are invisible and !isActive,
 * the player falls through them.
 * 
 * Only the active/non-active status is determined in this class. All other interaction are done in the
 * player class
 * 
 * @author Aron Strandberg
 * @version 2019-01-25
 */
public class Platform extends Obstacle
{
    public boolean isActive = true;
    public boolean isDisappearingBlock = false;
    private int start;
    private int refresh;
    
    // Sprites
    public final GreenfootImage[] graphics = {
        new GreenfootImage("platform_00.png"),
        new GreenfootImage("platform_01.png"),
        new GreenfootImage("platform_02.png"),
        new GreenfootImage("platform_03.png"),
    };
    
    private GreenfootImage thisImage; // If the block can disappear, a private sprite is used

    /**
    * @param  image  integer from the level generator that is used to select sprite
    */
    public Platform(int image)
    {
        rescale(graphics);
        setImage(graphics[image]); 
    }
    
    /**
    * This is the alterantive constructor for platforms that can disappear
    * @param  image  integer from the level generator that is used to select sprite
    * @param  loopstart  vary this number to allow platforms to be timed differently
    * @param  looprefresh  Determines when the block switches from solid to non-solid. Larger number means slower alternation
    */
    public Platform(int image, int loopstart, int looprefresh)
    {
        rescale(graphics);
        
        isDisappearingBlock = true;
        start = loopstart;
        refresh = looprefresh;
        
        thisImage = new GreenfootImage(graphics[image]);
        setImage(thisImage);
    }
    
    /**
     * The only activity of the platform is to check whether it is a normal dead platform or an alternating one.
     */
    public void act() 
    {
        if(isDisappearingBlock) appearDisappear();
    }
    
    /**
    * Alternation between solid and non solid. The player only collides with platforms where isActive = true.
    */
    public void appearDisappear()
    {
        start++;
        if( start >= refresh){
            start = 0;
            isActive = !isActive;
            if(!this.isActive){getImage().setTransparency(55);}
        }
        if(this.isActive && getImage().getTransparency()<255)
        {getImage().setTransparency(getImage().getTransparency()+20);}
    }
    
}
