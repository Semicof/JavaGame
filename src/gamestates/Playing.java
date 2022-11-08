package gamestates;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.PauseOverlay;
import utilz.LoadSave;
import static utilz.Constants.Environment.*;

public class Playing extends State implements Statemethods {
	private Player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private PauseOverlay pauseOverlay;
	private boolean paused=false;
	private int xLvlOffset,leftBorder=(int)(0.2*Game.GAME_WIDTH),rightBorder=(int)(0.8*Game.GAME_WIDTH),lvlTitlesWide= LoadSave.GetLevelData()[0].length;
	private int maxTitlesOffset=lvlTitlesWide-Game.TILES_IN_WIDTH;
	private int maxLvlOffsetX=maxTitlesOffset*Game.TILES_SIZE;

	private BufferedImage backgroundImg,bigCloudsImg,smallCloudsImg;
	private int[] smallCloudPos;
	private Random random=new Random();

	public Playing(Game game) {
		super(game);
		initClasses();
		backgroundImg=LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
		bigCloudsImg=LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS_IMG);
		smallCloudsImg=LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS_IMG);
		smallCloudPos=new int[8];
		for(int i=0;i<smallCloudPos.length;i++){
			smallCloudPos[i]=(int)(90*Game.SCALE)+random.nextInt((int)(100*Game.SCALE));
		}
	}

	private void initClasses() {
		levelManager = new LevelManager(game);
		enemyManager=new EnemyManager(this);
		player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE));
		player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		pauseOverlay=new PauseOverlay(this);
	}

	public void unpauseGame(){
		paused=false;
	}

	@Override
	public void update() {
		if(!paused)
		{
			levelManager.update();
			player.update();
			enemyManager.update(levelManager.getCurrentLevel().getLevelData());
			checkCloseToBorder();
		}else {
			pauseOverlay.update();
		}

	}

	private void checkCloseToBorder() {
		int playerX=(int)(player.getHitbox().x);
		int diff=playerX-xLvlOffset;


		if(diff>rightBorder){
			xLvlOffset+=diff-rightBorder;
		}
		else if(diff<leftBorder){
			xLvlOffset+=diff-leftBorder;
		}

		if(xLvlOffset>maxLvlOffsetX)
			xLvlOffset=maxLvlOffsetX;
		else if(xLvlOffset<0)
			xLvlOffset=0;
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg,0,0,Game.GAME_WIDTH,Game.GAME_HEIGHT,null);
		drawClouds(g);
		levelManager.draw(g,xLvlOffset);
		player.render(g,xLvlOffset);
		enemyManager.draw(g,xLvlOffset);
		if(paused){
			g.setColor(new Color(0,0,0,150));
			g.fillRect(0,0,Game.GAME_WIDTH,Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		}
	}

	private void drawClouds(Graphics g) {
		for(int i=0;i<3;i++)
			g.drawImage(bigCloudsImg,i*BIG_CLOUDS_WIDTH-(int)(xLvlOffset*0.3),(int)(204*Game.SCALE),BIG_CLOUDS_WIDTH,BIG_CLOUDS_HEIGHT,null);
		for(int i=0;i<smallCloudPos.length;i++)
			g.drawImage(smallCloudsImg,SMALL_CLOUDS_WIDTH*4*i-(int)(xLvlOffset*0.7),smallCloudPos[i],SMALL_CLOUDS_WIDTH,SMALL_CLOUDS_HEIGHT,null);

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
			player.setAttacking(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(true);
				break;
			case KeyEvent.VK_D:
				player.setRight(true);
				break;
			case KeyEvent.VK_W:
				player.setJump(true);
				break;
			case KeyEvent.VK_ESCAPE:
				Gamestate.state = Gamestate.MENU;
				break;
			case KeyEvent.VK_SPACE:
				paused=!paused;
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			player.setLeft(false);
			break;
		case KeyEvent.VK_D:
			player.setRight(false);
			break;
		case KeyEvent.VK_W:
			player.setJump(false);
			break;
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(paused){
			pauseOverlay.mousePressed(e);
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(paused){
			pauseOverlay.mouseReleased(e);
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(paused){
			pauseOverlay.mouseMoved(e);
		}

	}

	public void mouseDragged(MouseEvent e){
		if(paused){
			pauseOverlay.mouseDragged(e);
		}
	}

	public void windowFocusLost() {
		player.resetDirBooleans();
	}

	public Player getPlayer() {
		return player;
	}

}
