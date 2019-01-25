import greenfoot.*;

/**
 * This game was created as a part of the examination of the Uppsala University
 * summer course Introduction to Programming with Java
 * 
 * Originally created 2015-08-04. Updated 2019-01-25 to allow HTML5 conversion.
 * 
 * The game is a platformer inspired by the old NES classics.
 * The sounds effects have been generated at http://www.bfxr.net/
 * The music is created by me in FamiTracker.
 * The sprites have been drawn in Paint and on http://www.piskelapp.com/
 * 
 * There is only one level class, where all the individual levels are gathered. They are generated
 * by the arrays found at the bottom of the document.
 * 
 * @author Aron Strandberg
 * @version 2019-01-25
 */
public class Level extends World
{
    // Cheat button to allow easier testing. If set to true, levels can be skipped by pressing "s".
    private boolean allowLevelSkip = false; 

    // The number of deaths are kept track of and presented on the final screen.
    public static int numberOfDeaths = 0; 

    // The positions of the player and the key are saved to allow respawning.
    public static int[] spawnPos = {0,0};
    public static int[] keyPos = {0,0};

    // Is the player holding the key?
    public static boolean levelHasKey = false;
    // Which level should be generated?
    public static int whichLevel = 0;
    public static String[][] currentLevel;
    // A countdown that allows a delay between dying and respawning.
    public static int respawnCountdown;
    // When true, the player respawns
    public static boolean playerRespawn = false;

    // Level soundtrack
    static GreenfootSound currentSong;
    public final GreenfootSound[] soundtrack =
        {
            new GreenfootSound("soundtrack/stage_01.mp3"),
            new GreenfootSound("soundtrack/stage_02.mp3"),
            new GreenfootSound("soundtrack/stage_03.mp3"),
            new GreenfootSound("soundtrack/stage_04.mp3"),
            new GreenfootSound("soundtrack/stage_05.mp3"),
            new GreenfootSound("soundtrack/victory.mp3")
        };

    // Sound effects
    public final GreenfootSound respawn = new GreenfootSound("respawn.wav");

    // Level sprites
    public final GreenfootImage[] graphics =
        {
            new GreenfootImage("level_00.png"),
            new GreenfootImage("level_01.png"),
            new GreenfootImage("level_02.png"),
            new GreenfootImage("black.png")
        };

    /**
     * Constructor for objects of class Level.
     */
    public Level()
    {    
        super(384, 256, 2);
        Greenfoot.setSpeed(48);
        setPaintOrder(Victory.class,Explosion.class,Water.class,Key.class,Spikes.class,Platform.class,
            Player.class,Door.class);
        rescale(graphics);

        Level.currentSong = soundtrack[0];
        createMap(); // Generate the level
    }

    public void act()
    {
        checkRespawn(playerRespawn);
        skipLevel(); //Cheat button that works if allowLevelSkip is set to true
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

    /**
     * Method that initiates the music for the current level
     * @param  i  refers to the song order in the soundtrack array
     */
    public void setSong(int i)
    {
        if(currentSong.isPlaying()) currentSong.stop();
        currentSong = soundtrack[i];
        currentSong.playLoop();
        currentSong.setVolume(100);
    }

    /**
     * The player respawn is found in this class. Allows a delay between death and respawn with no player
     * on the screen
     * @param  TF  boolean that is true after the delay counter has counted down after the player death
     */
    public void checkRespawn(boolean TF)
    {
        if(playerRespawn){
            respawnCountdown --;
            if(respawnCountdown <= 0){
                addObject(new Player(),Level.spawnPos[0],Level.spawnPos[1]);
                playerRespawn = false;
                respawn.play();
            }
        }
    }

    /**
     * Cheat button "s" that works if allowLevelSkip has been set to true. Allows faster testing and debugging.
     */
    public void skipLevel(){
        if ("s".equals(Greenfoot.getKey()) && allowLevelSkip){
            Level.currentSong.stop();
            Level.whichLevel++;
            Greenfoot.setWorld(new Level());
            playerRespawn = false;
        }
    }

    /**
     * Loop that generates the levels according to the arrays maps at the bottom.
     */
    public void createMap()
    { 
        levelHasKey = false;  //This is to know whether the key should be respawned when dying

        if(whichLevel > 4) {whichLevel = 0;} //In case one tries to cheat past the final level
        if(whichLevel == 0){currentLevel = Level0; setBackground(graphics[0]); setSong(0);}
        else if(whichLevel == 1){currentLevel = Level1; setBackground(graphics[1]); setSong(1);}
        else if(whichLevel == 2){currentLevel = Level2; setBackground(graphics[2]); setSong(2);}
        else if(whichLevel == 3){currentLevel = Level3; setBackground(graphics[1]); setSong(3);}
        else if(whichLevel == 4){currentLevel = Level4; setBackground(graphics[3]); setSong(5);}

        // Below is the loop that generates the current level
        // Additional objects can easily be added in the future with more if/else functions

        for (int j=0; j<16; j = j + 1)
        {  
            for (int i=0; i<24; i = i + 1)
            {
                char letter = currentLevel[j][0].charAt(i);
                int I = 8+(16*i);
                int J = 8+(16*j);

                if(letter == ' '){continue;}

                // Normal platforms
                else if(letter == '0'){ addObject(new Platform(0),I ,J);}
                else if(letter == '2'){ addObject(new Platform(2),I ,J);}
                else if(letter == '1'){ addObject(new Platform(1),I ,J);} 

                // Temporary platforms
                else if(letter == '4'){ addObject(new Platform(1,0,150),I ,J);}
                else if(letter == '5'){ addObject(new Platform(1,75,150),I ,J);}
                else if(letter == '6'){ addObject(new Platform(1,150,150),I ,J);}

                // Water
                else if(letter == 'q'){ addObject(new Water(1),I ,J);} 
                else if(letter == 'w'){ addObject(new Water(2),I ,J);}

                // Spikes/enemies
                else if(letter == 'z'){ addObject(new Spikes(1,0),I, J );}
                else if(letter == 'x'){ addObject(new Spikes(2,0),I, J );}
                else if(letter == 'c'){ addObject(new Spikes(3,0),I, J );}

                // Multiple objects at the same square (water and spikes)
                else if(letter == 'v'){addObject(new Water(2),I, J ); addObject(new Spikes(1,0),I, J );}
                else if(letter == 'b'){addObject(new Water(2),I,J ); addObject(new Spikes(3,0),I,J );}
                else if(letter == 'n'){addObject(new Water(2),I,J ); addObject(new Spikes(2,0),I,J );}

                // Player
                else if(letter == 'P'){createPlayer(I,J);}
                // Door
                else if(letter == 'J'){addObject(new Door(),I,J ); continue; }  
                // Key
                else if(letter == 'K'){createKey(I,J);}
                else if(letter == 'k'){addObject(new Water(2),I, J );createKey(I,J);}
                //When this is created, the result is shown and the game can be restarted.
                else if(letter == 'O'){addObject(new Victory(),I, J );}
            }
        }
    }

    /**
     * @param  I  Player x position in the createMap() for loop
     * @param  J  Player y position in the createMap() for loop
     */
    public void createPlayer(int I, int J)
    {
        spawnPos[0] = I; spawnPos[1] = J; // Saves initial player x and y for later respawn
        addObject(new Player(),spawnPos[0],spawnPos[1]);
    }

    /**
     * @param  I  Key x position in the createMap() for loop
     * @param  J  Key y position in the createMap() for loop
     */    
    public void createKey(int I, int J)
    {
        levelHasKey = true;
        keyPos[0] = I; keyPos[1] = J;
        addObject(new Key(),keyPos[0] ,keyPos[1] );
    }

    String[][] Level0 = {
            {"111111111111111111111111"},
            {"1                      1"},
            {"1           c          1"},
            {"1                1     1"},
            {"1       c        1  J  1"},
            {"1    1111111111111111111"},
            {"1   11                 1"},
            {"11   c                 1"},
            {"11        1111111111   1"},
            {"111       1        1   1"},
            {"111       1        11  1"},
            {"11111111111            1"},
            {"1                    111"},
            {"1 P                  111"},
            {"1             1   111111"},
            {"111111111111111zzz111111"}};

    String[][] Level1 = {
            {"000000000000000000000000"},
            {"0                      0"},
            {"0          c         K 0"},
            {"0                   0000"},
            {"0 J                    0"},
            {"0 00  00  00x     0    0"},
            {"0  0 x     0           0"},
            {"0          0           0"},
            {"0 P        00000 00    0"},
            {"0          0           0"},
            {"0000qqqqqqq0qqqqqqqqq000"},
            {"000wwwwwwww0wwwwwwwwwww0"},
            {"000wwwwwwwwwwwwwwwwwwww0"},
            {"000wwwwwwwwwwwwwwwwwwww0"},
            {"000wwwwwwww00vvvvvvvvvv0"},
            {"000000000000000000000000"}};

    String[][] Level2 = {
            {"222222222222222222222222"},
            {"2    2 x      2        2"},
            {"2          2           2"},
            {"2         22     2222  2"},
            {"2 P        2      2    2"},
            {"2    2x          x2x   2"},
            {"2222222222222222222    2"},
            {"2                      2"},
            {"2                      2"},
            {"2                      2"},
            {"2 J                    2"},
            {"211144665566445566551112"},
            {"211                  112"},
            {"21                    12"},
            {"2zzzzzzzzzzzzzzzzzzzzzz2"},
            {"222222222222222222222222"}};

    String[][] Level3 = {
            {"222222222222222222222222"},
            {"2     c                2"},
            {"2  P     c             2"},
            {"2           c     111  2"},
            {"2 1111111111111  11    2"},
            {"2  1         1    1    2"},
            {"21 1 145645641   x1    2"},
            {"21 1 1z z    1x   1    2"},
            {"21   11 1  111  x 1  zz2"},
            {"211  1qqqqqqqqqqqq1  112"},
            {"211111wwwwwwbwwwww1    2"},
            {"2wwwwwwwwwwwwwwwww1 J  2"},
            {"2wwwwwwwwwwwwwwwww1111q2"},
            {"2wwkwwbwwwwwwwwwwwwwwww2"},
            {"2v111vvvvvvvvvvv11wwwww2"},
            {"222222222222222222222222"}};

    String[][] Level4 = {
            {"121212121212121212121212"},
            {"2                      1"},
            {"1                      2"},
            {"2                      1"},
            {"1                      2"},
            {"2                      1"},
            {"1                      2"},
            {"2                      1"},
            {"1           O          2"},
            {"2                      1"},
            {"1                      2"},
            {"2                      1"},
            {"1                      2"},
            {"2                      1"},
            {"1                      2"},
            {"212121212121212121212121"}};

}
