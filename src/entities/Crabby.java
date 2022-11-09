package entities;

import main.Game;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

public class Crabby extends Enemy{
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH,CRABBY_HEIGHT,CRABBY);
        initHitbox(x,y,(int)(22* Game.SCALE),(int)(29* Game.SCALE));
    }

    private void updateMove(int[][] lvlData,Player player){
        if(firstUpdate){
           firstUpdateCheck(lvlData);
        }
        if(inAir){
            inAirCheck(lvlData);
        }
        else {
            switch (enemyState){
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if(canSeePlayer(lvlData,player))
                        turnTowardsPlayer(player);
                    if(isPlayerCloseForAttack(player))
                        newState(ATTACK);
                    move(lvlData);
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

    public void update(int[][] lvlData,Player player){
        updateMove(lvlData,player);
        updateAnimationTick();
    }
}
