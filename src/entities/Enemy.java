package entities;

import main.Game;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;

public abstract class Enemy extends Entity{
    private int aniIndex,enemyState=RUNNING,enemyType,aniTick,aniSpeed=25,walkDir=LEFT;
    private boolean firstUpdate=true,inAir=false;
    private float fallSpeed,gravity=0.04f* Game.SCALE,walkSpeed=0.5f*Game.SCALE;
    public Enemy(float x,float y,int width,int height,int enemyType){
        super(x,y,width,height);
        this.enemyType=enemyType;
        initHitbox(x,y,width,height);
    }

    private void updateAnimationTick(){
        aniTick++;
        if(aniTick>=aniSpeed){
            aniTick=0;
            aniIndex++;
            if(aniIndex>=getSpriteAmount(enemyType,enemyState)){
                aniIndex=0;
            }
        }
    }

    public void update(int[][] lvlData){
        updateMove(lvlData);
        updateAnimationTick();
    }

    private void updateMove(int[][] lvlData){
        if(firstUpdate){
            if(!IsEntityOnFloor(hitbox,lvlData)){
                inAir=true;
            }
            firstUpdate=false;
        }
        if(inAir){
            if(CanMoveHere(hitbox.x,hitbox.y, hitbox.width,hitbox.height,lvlData)){
                hitbox.y+=fallSpeed;
                fallSpeed+=gravity;
            }else{
                inAir=false;
                hitbox.y=GetEntityYPosUnderRoofOrAboveFloor(hitbox,fallSpeed);
            }
        }
        else {
                switch (enemyState){
                    case IDLE:
                        enemyState=IDLE;
                        break;
                    case RUNNING:
                        float xSpeed=0;
                        if(walkDir==LEFT)
                            xSpeed-=walkSpeed;
                        else xSpeed+=walkSpeed;
                        if(CanMoveHere(hitbox.x+xSpeed, hitbox.y,hitbox.width,hitbox.height,lvlData)){
                            if(IsFloor(hitbox,xSpeed,lvlData)){
                                hitbox.x+=xSpeed;
                                return;
                            }
                        }
                        changeWalkDir();
                        break;
                    case ATTACK:
                        break;
                    case HIT:
                        break;
                    case DEAD:
                        break;
                }
        }
    }

    private void changeWalkDir() {
        if(walkDir==LEFT)
            walkDir=RIGHT;
        else walkDir=LEFT;
    }

    public int getAniIndex(){
        return aniIndex;
    }

    public int getEnemyState(){
        return enemyState;
    }

}
