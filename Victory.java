import greenfoot.*;

/**
 * The concluding message when all levels have been completed. Shows a congratulations message and reports
 * the number of deaths.
 * 
 * @author Aron Strandberg
 * @version 2019-01-25
 */
public class Victory extends Actor
{
    private int counting = 0;
    
    String line1 = "            Congratulations";
    String line2 = "\nYou have completed the game";
    String line3 = "\n     You died a total of "+ Level.numberOfDeaths + " times";
    String line4 = "\n\n        Press space to restart";
    
    // sound effects
    static GreenfootSound text = new GreenfootSound("text.wav");
    
    
    public Victory(){
        getImage().clear();
    }
    
    /**
     * Act - do whatever the Other wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        textAppear();
        
        if(counting>140 && Greenfoot.isKeyDown("space")){
            Level.currentSong.stop();
            //Greenfoot.stop();
            //System.exit(1);
            Level.whichLevel = 0;
            Level.numberOfDeaths = 0;
            Greenfoot.setWorld(new Level());
            
        }
    }
    
    /**
     * The text is drawn on the screen with a series of steps
     */
    public void textAppear(){
        
        if (counting == 20){ createString(line1); text.play();} // First row
        else if (counting == 80){ createString(line1+line2); text.play();} // Second row
        else if (counting == 140){ createString(line1+line2+line3); text.play();} // Third row
        else if (counting == 180){ createString(line1+line2+line3);}
        else if (counting == 200){ createString(line1+line2+line3+line4);}
        
        if(counting > 200){ counting = 150;}
        counting ++;
    }
    
    /**
     * This method draws the congratulations text
     * @param  text  is extended stepwise with the strings defined at the beginning of this class
     */
    public void createString(String text){
        GreenfootImage img = new GreenfootImage(190,100);
        img.setColor(greenfoot.Color.WHITE);
        img.drawString(text,0,16);
        setImage(img);
        img.scale(img.getWidth()*2,img.getHeight()*2);
        
    }
}
