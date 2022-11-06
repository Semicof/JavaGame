package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.PauseButtons.*;
import static utilz.Constants.UI.URMButtons.*;
import static utilz.Constants.UI.VolumeButtons.*;

public class PauseOverlay {

    private BufferedImage backgroundImg;
    private int bgX,bgY,bgW,bgH;
    private SoundButton musicButton,sfxButton;
    private URMButtons homeB,replayB,playB;
    private VolumeButtons volumeButtons;
    private Playing playing;

    public PauseOverlay(Playing playing){
        this.playing=playing;
        loadBackGround();
        createSoundButton();
        createURMButton();
        createVolumeButton();
    }

    private void createVolumeButton() {
        int vX=(int)(309*Game.SCALE);
        int vY=(int)(302*Game.SCALE);
        volumeButtons=new VolumeButtons(vX,vY,SLIDER_WIDTH,VOLUME_HEIGHT);
    }

    private void createURMButton() {
        int urmY=(int)(350*Game.SCALE);
        int homeX=(int)(313*Game.SCALE);
        int replayX=(int)(387*Game.SCALE);
        int playX=(int)(462*Game.SCALE);
        homeB=new URMButtons(homeX,urmY,URM_SIZE,URM_SIZE,2);
        replayB=new URMButtons(replayX,urmY,URM_SIZE,URM_SIZE,1);
        playB=new URMButtons(playX,urmY,URM_SIZE,URM_SIZE,0);
    }

    private void createSoundButton() {
        int soundX=(int)(450*(Game.SCALE));
        int musicY=(int)(165*Game.SCALE);
        int sfxY=(int)(210*Game.SCALE);
        musicButton=new SoundButton(soundX,musicY,SOUND_SIZE,SOUND_SIZE);
        sfxButton=new SoundButton(soundX,sfxY,SOUND_SIZE,SOUND_SIZE);
    }

    private void loadBackGround() {
        backgroundImg= LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW=(int)(backgroundImg.getWidth()* Game.SCALE);
        bgH=(int)(backgroundImg.getHeight()* Game.SCALE);
        bgX=Game.GAME_WIDTH/2-bgW/2;
        bgY=100;
    }

    public void update(){

        musicButton.update();
        sfxButton.update();

        homeB.update();
        replayB.update();
        playB.update();

        volumeButtons.update();
    }
    public void draw(Graphics g){
        g.drawImage(backgroundImg,bgX,bgY,bgW,bgH,null);

        musicButton.draw(g);
        sfxButton.draw(g);

        homeB.draw(g);
        replayB.draw(g);
        playB.draw(g);

        volumeButtons.draw(g);
    }


    public void mousePressed(MouseEvent e) {

        if(isIn(e,musicButton)){
            musicButton.setMousePressed(true);
        }
        else if(isIn(e,sfxButton)){
            sfxButton.setMousePressed(true);
        }

        else if(isIn(e,homeB)){
            homeB.setMousePressed(true);
        }
        else if(isIn(e,replayB)){
            replayB.setMousePressed(true);
        }
        else if(isIn(e,playB)){
            playB.setMousePressed(true);
        }
        else if(isIn(e,volumeButtons)){
            volumeButtons.setMousePressed(true);
        }
    }

    public void mouseReleased(MouseEvent e) {

        if(isIn(e,musicButton)){
           if(musicButton.isMousePressed()){
               musicButton.setMuted(!musicButton.isMuted());
           }
        }
        else if(isIn(e,sfxButton)){
            if(sfxButton.isMousePressed()){
                sfxButton.setMuted(!sfxButton.isMuted());
            }
        }

        else if(isIn(e,homeB)){
            if(homeB.isMousePressed()){
                Gamestate.state=Gamestate.MENU;
                playing.unpauseGame();
            }
        }
        else if(isIn(e,replayB)){
            if(replayB.isMousePressed()){
                System.out.println("replay");
            }
        }
        else if(isIn(e,playB)){
            if(playB.isMousePressed()){
                playing.unpauseGame();
            }
        }


        musicButton.resetBools();
        sfxButton.resetBools();

        homeB.resetBools();
        replayB.resetBools();
        playB.resetBools();

        volumeButtons.resetBools();
    }

    public void mouseDragged(MouseEvent e) {
        if(volumeButtons.isMousePressed()){
            volumeButtons.changeXPos(e.getX());
        }
    }

    public void mouseMoved(MouseEvent e) {
        volumeButtons.setMouseOver(false);

        homeB.setMouseOver(false);
        replayB.setMouseOver(false);
        playB.setMouseOver(false);

        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);

        if(isIn(e,musicButton)){
            musicButton.setMouseOver(true);
        }
        else if(isIn(e,sfxButton)){
            sfxButton.setMouseOver(true);
        }
        else if(isIn(e,homeB)){
            homeB.setMouseOver(true);
        }
        else if(isIn(e,replayB)){
            replayB.setMouseOver(true);
        }else if(isIn(e,playB)){
            playB.setMouseOver(true);
        }
        else if(isIn(e,volumeButtons)){
            volumeButtons.setMouseOver(true);
        }
    }

    private boolean isIn(MouseEvent e,PauseButton b){
        if(b.getBounds().contains(e.getX(),e.getY()))
            return true;
        return false;
    }

}
