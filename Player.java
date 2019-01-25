import greenfoot.*;

/**
 * The player. Has a number of internal variables that are used
 * to determine position, collisions, gravity, and so on.
 * 
 * @author Aron Strandberg
 * @version 2019-01-25
 */
public class Player extends Mover
{
    // Variabler:
    private double velocity = 0;                // the velocity of the player
    private double terminalVelocity;        // the maximum velocity used at the moment (alternating)
    private double termVelo_norm = 5;       // the maximum velocity when free falling
    private double termVelo_underw = -3.4; //decrease of terminal velocity due to being in water
    
    private double jumpStrength = -7;       // initial jumping velocity when pressing space
    private double jumpStr_underw = 1.7;        // underwater decrease of strength
    
    private double jumpCutoff = -1;         // the cut-off for cancelling the jump mid-air
    private double acceleration = 0.42;      // the acceleration per frame towards terminal velocity
    private boolean canMove = true;         // allows a paralyzed player. Not currently in use
    private boolean canJump = false;        // set to false when in air
    private boolean notHold = false;        // used to disallow repeat-jumping by holding space
    private boolean direction = true;        // left or right direction. true = R, false = L
    private boolean isMoving = false;       // is the player moving horizontally?
 
    // Grafik
    private int animationFrame = 0;
    private int animationLoop = 0;
    private int animationRefresh = 9;
    
    public final GreenfootImage[] graphics =
    {
        new GreenfootImage("playergraphics/player_rstand.png"),
        new GreenfootImage("playergraphics/player_lstand.png"),
        new GreenfootImage("playergraphics/player_rjump.png"),
        new GreenfootImage("playergraphics/player_ljump.png"),
        new GreenfootImage("playergraphics/player_rrun1.png"),
        new GreenfootImage("playergraphics/player_rrun2.png"),
        new GreenfootImage("playergraphics/player_rrun3.png"),
        new GreenfootImage("playergraphics/player_rrun4.png"),
        new GreenfootImage("playergraphics/player_lrun1.png"),
        new GreenfootImage("playergraphics/player_lrun2.png"),
        new GreenfootImage("playergraphics/player_lrun3.png"),
        new GreenfootImage("playergraphics/player_lrun4.png"),
    };
    
    // Ljudeffekter:
    static GreenfootSound jump = new GreenfootSound("jump.wav");
    static GreenfootSound die = new GreenfootSound("explosion.wav");
    static GreenfootSound swim = new GreenfootSound("swim.wav");
    static GreenfootSound win = new GreenfootSound("finish.wav");
    static GreenfootSound keyPickup = new GreenfootSound("key.wav");
    
    /**
    */
    public Player()
    {
        setImage(graphics[1]);
        rescale(graphics);
    }
    
    
    /**
     * All the player physics and interaction with surrounding are kept track of here.
     */
    public void act() 
    {
        xMovement(); //1
        gravity();   //2
        yMovement();  //3
        checkJumping();
        preventStuck();
        animation();
        collectKey();
        levelGoal();
        checkDeath();
    }
    
    /**
    * Method for horizontal collision detection. The loop makes sure that all pixels next to the player are checked.
    * 
    * @param  offset  the x position used to determine on which side of the player to scan.
    */
    public boolean noLRCollision(int offset)
    {
        for (int i = -8; i < 7; i++)
        {
            Platform platform = (Platform) getOneObjectAtOffset(offset,i,Platform.class);
            if (platform != null && platform.isActive){
                return false; }
        }
        return true;
    }
    
    /**
    * Method for vertical collision detection. The loop makes sure that all pixels next to the player are checked.
    * 
    * @param  offset  the y position used to determine on which side of the player to scan.
    */
    public boolean noTDCollision(int offset)
    {
        for (int i = -7; i < 7; i++)
        {
            Platform platform = (Platform) getOneObjectAtOffset(i,offset,Platform.class);
            if (platform != null && platform.isActive){
                return false; }
        }
        return true;
    }
    
    /**
    * Method used to move the player horizontally if possible
    */
    public void xMovement()
    {
        if ( Greenfoot.isKeyDown("left") || Greenfoot.isKeyDown("right") )
        {
            if ( Greenfoot.isKeyDown("left") && canMove == true && noLRCollision(-8) )
            {
                moveLeft(2);
                direction = false;
                isMoving = true;
            }
            
            if ( Greenfoot.isKeyDown("right") && canMove == true & noLRCollision(8) ) 
            {           
                moveRight(2);
                direction = true;
                isMoving = true;
            }
        } else {isMoving = false;}
    }
    
    /**
    * Moves the player to the right. @param speed determines how many pixels.
    */
    public void moveRight(int speed)
    {
        for (int i = 0; i < speed; i++){if(noLRCollision(7)) setLocation(getX()+1,getY());}
    }
    
    /**
    * Moves the player to the left. @param speed determines how many pixels.
    */
    public void moveLeft(int speed)
    {
        for (int i = 0; i < speed; i++){if(noLRCollision(-8)) setLocation(getX()-1,getY());}
    }
    
    /**
     * Checks if the player is able to and wants to jump and then performs it
     */
    public void checkJumping()
    {
        if(!Greenfoot.isKeyDown("space") && canJump) notHold = true;
        
        if (Greenfoot.isKeyDown("space") && canMove && canJump && notHold)
        {
            velocity = jumpStrength + (isUnderwater() * jumpStr_underw);
            notHold = false;
            
            if(isUnderwater() == 1){swim.play();}
            else{jump.play();}
        }
        if (!Greenfoot.isKeyDown("space") && velocity < jumpCutoff) velocity = jumpCutoff;
    }
   
    /**
    * Sets the new vertical position, calculated from the vertical velocity.
    */
    public void gravity()
    {
        if (velocity < terminalVelocity) velocity +=acceleration;
        if (velocity >= terminalVelocity) velocity = terminalVelocity;
        setLocation(getX(),getY() + (int) velocity);
    }
    
    /**
    * Checks if there is an obstacle under the player, and if so, sets the terminal velocity to 0 
    */
    public void yMovement()
    {
        
        if( !noTDCollision(-8) && velocity < 0)
        {
            velocity = 0;
            while(!noTDCollision(-8)) setLocation(getX(),getY()+1);
        }
        
        if( !noTDCollision(8) )
        {
            terminalVelocity = 0;
            while(!noTDCollision(7)) setLocation(getX(),getY()-1);
            canJump = true;
        } else { 
            terminalVelocity = termVelo_norm + ( isUnderwater()* termVelo_underw ) ;
            if(isUnderwater()==0){canJump = false;} else {canJump = true;}
            
        }
    }
    
    /** 
     * This method moves the player out of obstacles, if necessary
    */
    public void preventStuck()
    {  
    while(!noLRCollision(6)) setLocation(getX()-1,getY());
    while(!noLRCollision(-7)) setLocation(getX()+1,getY());
    }
    
    /**
    * Determines which animation to use with which action.
    */
    public void animation()
    {
        if(direction && !isMoving) setImage(graphics[0]);
        if(!direction && !isMoving) setImage(graphics[1]);
        
        if(direction && velocity!=0) setImage(graphics[2]);
        if(!direction && velocity!=0) setImage(graphics[3]);
        

        if(direction && isMoving && velocity==0){
            if (animationFrame == 0){setImage(graphics[4]);}
            else if (animationFrame == 1){ setImage(graphics[5]);}
            else if (animationFrame == 2){ setImage(graphics[6]);}
            else if (animationFrame == 3){ setImage(graphics[7]);}
        }
        if(!direction && isMoving && velocity==0){
            if (animationFrame == 0) setImage(graphics[8]);
            else if (animationFrame == 1) setImage(graphics[9]);
            else if (animationFrame == 2) setImage(graphics[10]);
            else if (animationFrame == 3) setImage(graphics[11]);
        }
        
        animationLoop ++;
        if(animationLoop>= animationRefresh){
          animationLoop = 0;
          animationFrame++;
        }
        if (animationFrame > 3) animationFrame = 0;
        
    }
    
    /**
    * Checks if the player is located in water, which changes the physics.
    */
    public int isUnderwater()
    {
        if (getOneIntersectingObject(Water.class) != null){ return 1; }
        return 0;
    }
    
    /**
    * Checks if something is hurting (killing) the player. For now, there are only spike objects.
    */
    public void checkDeath() 
    {
        if (getOneIntersectingObject(Spikes.class) != null){
        
            for(int i = 0; i<360; i+=45){
                getWorld().addObject(new Explosion(i,1),getX(),getY());
            }
                  
            if(getWorld().getObjects(Key.class).size() == 0 && Level.levelHasKey)
            {
                getWorld().addObject(new Key(),Level.keyPos[0] ,Level.keyPos[1] );
            }
            
            die.play();
            getWorld().removeObject(this);
            Level.numberOfDeaths++;
            Level.respawnCountdown = 35;
            
        }
    }
    
    /**
    * Picking up the key that opens the door to the next level.
    */
    public void collectKey()
    {
        Actor key;
        key = getOneObjectAtOffset(0, 0, Key.class);
        if (key != null)
        {
            getWorld().removeObject(key);
            keyPickup.play();
        }
    }
    
    /**
    * Advanced the game to the next level if the player touches an open door
    */
    public void levelGoal()
    {
        if (getOneIntersectingObject(Door.class) != null && getWorld().getObjects(Key.class).size() == 0){
            win.play();
            Level.currentSong.stop();
            Level.whichLevel++;Greenfoot.setWorld(new Level());
            Level.playerRespawn = false;
        }
    }
 }
