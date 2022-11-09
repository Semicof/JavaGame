package entities;

import gamestates.Playing;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] crabbyImgs;
    private ArrayList<Crabby> crabbies=new ArrayList<>();

    public EnemyManager(Playing playing){
        this.playing=playing;
        loadEnemyImages();
        addEnemies();
    }

    private void addEnemies() {
        crabbies=LoadSave.GetCrabs();
    }

    public void update(int[][] lvlData,Player player){
        for (Crabby crabby:crabbies){
            crabby.update(lvlData,player);
        }
    }

    public void draw(Graphics g,int xLevelOffset){
        drawCrabs(g,xLevelOffset);
    }

    private void drawCrabs(Graphics g,int xLevelOffset) {
        for(Crabby crabby:crabbies){
            g.drawImage(crabbyImgs[crabby.getEnemyState()][crabby.getAniIndex()],(int)(crabby.getHitbox().x)-xLevelOffset,(int)(crabby.getHitbox().y),CRABBY_WIDTH,CRABBY_HEIGHT,null);
        }
    }

    private void loadEnemyImages() {
        crabbyImgs=new BufferedImage[5][9];
        BufferedImage tmp= LoadSave.GetSpriteAtlas(LoadSave.CRAB_SPRITE);
        for(int j=0;j<crabbyImgs.length;j++){
            for(int i=0;i<crabbyImgs[j].length;i++){
                crabbyImgs[j][i]=tmp.getSubimage(i*CRABBY_WIDTH_DEFAULT,j*CRABBY_HEIGHT_DEFAULT,CRABBY_WIDTH_DEFAULT,CRABBY_HEIGHT_DEFAULT);
            }
        }
    }
}
