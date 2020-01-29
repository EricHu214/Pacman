package pacman;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PacmanGame extends JPanel implements KeyListener, ActionListener{
    
    private int counter = 0, ghostNewDirection, livesLeft = 2, score, ghostCounter;
    private boolean dead = false, pause = false, stop = true, ready = false, cleared = false;
    private Maze setUp = new Maze();
    private Player dude;
    private Player[] ghostDude = new Player[6];
    private Timer timer;
    
    public PacmanGame(){
        setSize(680,660);
        setFocusable(true);
        addKeyListener(this);
        for(int i = 0; i < ghostDude.length; i++)
            ghostDude[i] = new Player(setUp, 324, 278);
        dude = new Player(setUp, 326, 401);
        timer = new Timer(1, this);
        timer.start();
    }
    
    @Override
    public void keyPressed(KeyEvent e){
        int newDirection = dude.getIntendedDirection();
        
          if(e.getKeyCode() == KeyEvent.VK_W)
              newDirection = 1;
          else if(e.getKeyCode() == KeyEvent.VK_S)
              newDirection = 2;
          else if(e.getKeyCode() == KeyEvent.VK_D)
              newDirection = 3;
          else if(e.getKeyCode() == KeyEvent.VK_A)
              newDirection = 4;
          else if(e.getKeyCode() == KeyEvent.VK_P){
              if(!pause)
                  pause = true;
              else{
                  pause = false;
                  timer.start();
              }  
          }
          else if(e.getKeyCode() == KeyEvent.VK_ENTER){
              stop = false;
              ready = true;
          }
          
          if (newDirection != dude.getIntendedDirection()) {
              dude.resetIsCollided();
              dude.setIntendedtDirection(newDirection);
          }
        }
    
    public void levelContinue(){
        dude = new Player(setUp, 326, 401);
        for(int i = 0; i < ghostDude.length; i++)
            ghostDude[i] = new Player(setUp,  326, 281);
        
        if(cleared)
            setUp.dotsAssign();
    }
    
    public void moveGhost(){
            boolean chase = true;
            int modulus = (int)(Math.random()*40)+ 20;
            ghostNewDirection = (int)(Math.random()*4)+1;
            
            if(counter % modulus == 0 || ghostDude[ghostCounter].isCollided()){
                if(ghostDude[ghostCounter].getIntendedDirection() != ghostNewDirection){
                    ghostDude[ghostCounter].resetIsCollided();
                    ghostDude[ghostCounter].setIntendedtDirection(ghostNewDirection);
                }
            }
            else
                ghostDude[ghostCounter].motion();
        }
   
    
    
    public void pacmanDies(){
        if(ghostDude[ghostCounter].getCollisionDetector().intersects(dude.getCollisionDetector())){
                dead = true;
                --livesLeft;
                counter = 1;
                
            } 
        }
    
    public void collectDots(){
        for(int i = 0; i < setUp.getDots().size(); i++){
            if(dude.getCollisionDetector().intersects(setUp.getDots().get(i))){
                setUp.setArrayList(i);
                ++score;
            }       
        }        
    }
    
    public int getCounter(){return counter;}
    
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void paintComponent(Graphics g){
            
        super.paintComponent(g);
        setBackground(Color.BLACK);
        if (!dude.isCollided())
            dude.motion();
            
        dude.paint(g);
        setUp.paintMap(g);
        g.setColor(Color.white);
        g.drawString("Score: " + score, 0, 625);
        g.drawString("Lives Left: " + livesLeft, 605, 625);
        
        for(ghostCounter = 0; ghostCounter < ghostDude.length; ghostCounter++ ){
            moveGhost();
            ghostDude[ghostCounter].paintGhost(g);
            pacmanDies();
        }
        
        if(livesLeft < 0){
            g.setColor(Color.red);
            g.drawString("Game Over", 310, 360);
            timer.stop();
        }  
        g.setColor(Color.white);
        
        if(pause){
            g.drawString("Paused" ,320, 360);
            timer.stop();
        }
        if(stop){
            g.drawString("Press Enter to begin", 285, 360);
        }
        if(ready){
            g.setColor(Color.yellow);
            g.drawString("ready!!", 320, 360);
            stop = true;
            counter = 1;
        }
        collectDots();
        
        if(setUp.getDots().size() == 0){
            cleared = true;
            score += 50;
        }
    }        
        
    @Override
    public void actionPerformed(ActionEvent e) {
        if(counter % 6 == 0 && !dead && !stop && !cleared)
            repaint();
        
        if(ready){
            if(counter % 50 == 0){
                ready = false;
                stop = false;
            }
        }
        
        if(dead || cleared){
            if(counter % 50 == 0){
                levelContinue();
                dead = false;
                ready = true;
                cleared = false;
            }
        }
        
        if(counter == 100){
            counter = 1;
        }
        
        dude.animateMouth(counter);
            
        counter++;
    }       
}