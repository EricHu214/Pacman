package pacman;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;

public class Player{

    private int speed, width, height;
    private int intendedDirection, prevDirection, currentX, currentY, prevX, prevY;;
    private boolean isCollidedCheck, isPrevDirection, isMoveX, mouthOpened;
    private Rectangle collisionDetector;
    private Maze instance;
    private BufferedImage pacmanRightClosed, pacmanRightOpen, pacmanUpClosed, 
            pacmanUpOpen, pacmanLeftClosed, pacmanLeftOpen, pacmanDownClosed, 
            pacmanDownOpen, pacmanStill, ghostUp, ghostDown, ghostLeft, 
            ghostRight, spriteSheet;
    
    public Player(Maze k, int x, int y){
        height = 28;
        width = 28;
        instance = k;
        isCollidedCheck = false;
        isPrevDirection = false;
        isMoveX = false;
        collisionDetector = new Rectangle(x, y, height, width);
        prevX = x;
        prevY = y;
        speed = 1;
        currentX = x;
        currentY = y;
        prevDirection = 0;
        intendedDirection = 0;
        imageLoader("pacmanSpriteSheet.png");
        getSprite();
    }
    
    
    public void getSprite(){
        pacmanLeftClosed = spriteSheet.getSubimage(0, 0, 20, 20);
        pacmanLeftOpen = spriteSheet.getSubimage(20, 0, 20, 20);
        pacmanRightClosed = spriteSheet.getSubimage(0, 20, 20, 20);
        pacmanRightOpen = spriteSheet.getSubimage(20, 20, 20, 20);
        pacmanUpClosed = spriteSheet.getSubimage(0, 40, 20, 20);
        pacmanUpOpen = spriteSheet.getSubimage(20, 40, 20, 20);
        pacmanDownClosed = spriteSheet.getSubimage(0, 60, 20, 20);
        pacmanDownOpen = spriteSheet.getSubimage(20, 60, 20, 20);
        pacmanStill = spriteSheet.getSubimage(40, 0, 20, 20);
        ghostLeft = spriteSheet.getSubimage(80, 80, 20, 20);
        ghostRight = spriteSheet.getSubimage(120, 80, 20, 20);
        ghostUp = spriteSheet.getSubimage(0, 80, 20, 20);
        ghostDown = spriteSheet.getSubimage(40, 80, 20, 20);
    }
    
    public void imageLoader(String relativePath){
        try{
            URL url = this.getClass().getResource(relativePath);
            spriteSheet = ImageIO.read(url); 
        }catch(Exception e){System.out.println("An error has occured");}
    }
    
    public void paint(Graphics g){
        g.setColor(Color.yellow);
        if(!mouthOpened){
            if(prevDirection == 1)
                g.drawImage(pacmanUpClosed ,prevX, prevY, 35, 35, null);
            else if(prevDirection == 2)
                g.drawImage(pacmanDownClosed, prevX, prevY, 35, 35, null);
            else if(prevDirection == 3)
                g.drawImage(pacmanRightClosed, prevX, prevY, 35, 35, null);
            else if(prevDirection == 4)
                g.drawImage(pacmanLeftClosed, prevX, prevY, 35, 35, null);       
        }
        else{
            if(prevDirection == 1)
                g.drawImage(pacmanUpOpen ,prevX, prevY, 35, 35, null);
            else if(prevDirection == 2)
                g.drawImage(pacmanDownOpen, prevX, prevY, 35, 35, null);
            else if(prevDirection == 3)
                g.drawImage(pacmanRightOpen, prevX, prevY, 35, 35, null);
            else if(prevDirection == 4)
                g.drawImage(pacmanLeftOpen, prevX, prevY, 35, 35, null);
        }
        if(prevDirection == 0)
            g.drawImage(pacmanStill, prevX, prevY, 35, 35, null);
    }
    
    public void animateMouth(int counter){
        if(counter % 10 == 0 && !isCollided()){
            if(mouthOpened)
                mouthOpened = false;
            else
                mouthOpened = true;
        }
        
    }
    public void paintGhost(Graphics g){
        if(prevDirection == 1)
            g.drawImage(ghostUp, prevX, prevY, 35, 35, null);
        else if(prevDirection == 2)
            g.drawImage(ghostDown, prevX, prevY, 35, 35, null);
        else if(prevDirection == 3)
            g.drawImage(ghostRight, prevX, prevY, 35, 35, null);
        else
            g.drawImage(ghostLeft, prevX, prevY, 35, 35, null);
    }
    
    public void motion(){
       int moveDirection;
       prevX = collisionDetector.x;
       prevY = collisionDetector.y;
       
       if (isPrevDirection){
           isPrevDirection = false;
           speed = 3;
       } 
       else if (intendedDirection == 1 && prevDirection != 2 ||
               intendedDirection == 2 && prevDirection != 1 ||
               intendedDirection == 3 && prevDirection != 4 ||
               intendedDirection == 4 && prevDirection != 3)
           //not alternative if move opposite direction
           isPrevDirection = true;

       if (!isPrevDirection)
           moveDirection = intendedDirection;
       else
           moveDirection = prevDirection;

       if(moveDirection == 1)
           collisionDetector.y += -speed;
       else if(moveDirection == 2)
           collisionDetector.y += speed;
       else if(moveDirection ==3)
           collisionDetector.x += speed;
       else if(moveDirection == 4)
           collisionDetector.x += -speed;
      
       if(collisionDetector.x > 670)
           collisionDetector.x = -30;
       else if(collisionDetector.x < -30)
           collisionDetector.x = 670;
       
    }
    
    public Rectangle getCollisionDetector(){return collisionDetector;}
    
    public boolean isCollided(){
        if(!isCollidedCheck){  
            for(int i = 0; i< instance.getWall().length; i++){
                if(collisionDetector.intersects(instance.getWall()[i]))
                    isCollidedCheck = true;
            }
            if (isCollidedCheck){
                collisionDetector.x = prevX;
                collisionDetector.y = prevY;

                if(distance())
                    isCollidedCheck = false;
            } 
            else {
                if(!distance()){
                    prevDirection = intendedDirection;
                    speed = 1;
                }
            }
        }
        return isCollidedCheck;
    }
    
    public void resetIsCollided() {isCollidedCheck = false;}
        
    public void setIntendedtDirection(int newDirection){
        intendedDirection = newDirection;
        currentX = collisionDetector.x;
        currentY = collisionDetector.y;
        
       if(newDirection == 1 || newDirection == 2) 
           isMoveX = false;
       else
           isMoveX = true;
       }
    public int getIntendedDirection() {return intendedDirection;}
    
    public int getPreviousDirection(){return prevDirection;}
    
    public boolean distance(){
        double distance;
        
        if (isMoveX) 
            distance = Math.sqrt(Math.pow(collisionDetector.x - currentX, 2));
        else
            distance = Math.sqrt(Math.pow(collisionDetector.y - currentY, 2));
        
        if(distance < 5)
            return true;
        else
            return false;
    }
}
