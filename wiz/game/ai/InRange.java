package wiz.game.ai;

import engine.game.ai.Condition;
import engine.support.Vec2i;
import wiz.game.helpers.TileType;
import wiz.game.objects.Enemy;
import wiz.game.objects.Map;
import wiz.game.objects.Tile;

public class InRange extends Condition {

    private Blackboard blackboard;
    private Map map;
    private Enemy enemy;

    public InRange(Blackboard blackboard, Map map, Enemy enemy){
        this.blackboard = blackboard;
        this.map = map;
        this.enemy = enemy;
    }

    @Override
    public BTStatus update(float seconds) {
        Tile enemyTile = this.map.getTile(this.enemy.getPosition());
        Vec2i enemyPos = enemyTile.getMapPosition();
        Tile playerTile = this.map.getTile(this.map.getPlayerWorldPos());
        Vec2i playerPos = playerTile.getMapPosition();

        // fail if close (if close, want to attack not defend)
        int manDist = Math.abs(playerPos.x - enemyPos.x) + Math.abs(playerPos.y - enemyPos.y);
        if(manDist < 3){ return BTStatus.FAIL; }

        if(enemyPos.x == playerPos.x){
            for(int i = Math.min(enemyPos.y, playerPos.y); i <= Math.max(enemyPos.y, playerPos.y); i++){
                if(this.map.getTile(new Vec2i(enemyPos.x, i)).getType() == TileType.IMPASSABLE){
                    return BTStatus.FAIL;
                }
            }
            return BTStatus.SUCCESS;
        }
        if(enemyPos.y == playerPos.y){
            for(int i = Math.min(enemyPos.x, playerPos.x); i <= Math.max(enemyPos.x, playerPos.x); i++){
                if(this.map.getTile(new Vec2i(i, enemyPos.y)).getType() == TileType.IMPASSABLE){
                    return BTStatus.FAIL;
                }
            }
            return BTStatus.SUCCESS;
        }
        return BTStatus.FAIL;
    }
}
