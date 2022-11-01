package wiz.game.ai;

import engine.game.ai.Condition;
import engine.support.Vec2i;
import engine.support.graph.Node;
import wiz.game.helpers.TileType;
import wiz.game.objects.Enemy;
import wiz.game.objects.Map;
import wiz.game.objects.Tile;

import java.util.Stack;

public class CanExitRange extends Condition {

    private Blackboard blackboard;
    private Map map;
    private Enemy enemy;

    public CanExitRange(Blackboard blackboard, Map map, Enemy enemy){
        this.blackboard = blackboard;
        this.map = map;
        this.enemy = enemy;
    }

    @Override
    public BTStatus update(float seconds) {
        Stack<Node<Tile>> path = new Stack<>();
        Tile enemyTile = this.map.getTile(this.enemy.getPosition());
        Vec2i enemyPos = enemyTile.getMapPosition();
        Tile playerTile = this.map.getTile(this.map.getPlayerWorldPos());
        Vec2i playerPos = playerTile.getMapPosition();
        if(enemyPos.x == playerPos.x){ // on same vertical line
            // left or right
            Tile left = this.map.getTile(new Vec2i(enemyPos.x-1, enemyPos.y));
            if(left.getType() != TileType.IMPASSABLE){
                path.push(new Node<>(left));
                this.blackboard.setAwayPath(path);
                return BTStatus.SUCCESS;
            }
            Tile right = this.map.getTile(new Vec2i(enemyPos.x+1, enemyPos.y));
            if(right.getType() != TileType.IMPASSABLE){
                path.push(new Node<>(right));
                this.blackboard.setAwayPath(path);
                return BTStatus.SUCCESS;
            }

            Tile away, awayLeft, awayRight;
            Tile toward, towardLeft, towardRight;

            // away left or away right
            if(enemyPos.y < playerPos.y){
                away = this.map.getTile(new Vec2i(enemyPos.x, enemyPos.y-1));
                awayLeft = this.map.getTile(new Vec2i(enemyPos.x-1, enemyPos.y-1));
                awayRight = this.map.getTile(new Vec2i(enemyPos.x+1, enemyPos.y-1));
            } else {
                away = this.map.getTile(new Vec2i(enemyPos.x, enemyPos.y+1));
                awayLeft = this.map.getTile(new Vec2i(enemyPos.x-1, enemyPos.y+1));
                awayRight = this.map.getTile(new Vec2i(enemyPos.x+1, enemyPos.y+1));
            }
            if(away.getType() != TileType.IMPASSABLE && awayLeft.getType() != TileType.IMPASSABLE){
                path.push(new Node<>(awayLeft));
                path.push(new Node<>(away));
                this.blackboard.setAwayPath(path);
                return BTStatus.SUCCESS;

            } else if(away.getType() != TileType.IMPASSABLE && awayRight.getType() != TileType.IMPASSABLE){
                path.push(new Node<>(awayRight));
                path.push(new Node<>(away));
                this.blackboard.setAwayPath(path);
                return BTStatus.SUCCESS;

            }

            // toward left or toward right
            if(enemyPos.y < playerPos.y){
                toward = this.map.getTile(new Vec2i(enemyPos.x, enemyPos.y+1));
                towardLeft = this.map.getTile(new Vec2i(enemyPos.x-1, enemyPos.y+1));
                towardRight = this.map.getTile(new Vec2i(enemyPos.x+1, enemyPos.y+1));
            } else {
                toward = this.map.getTile(new Vec2i(enemyPos.x, enemyPos.y-1));
                towardLeft = this.map.getTile(new Vec2i(enemyPos.x-1, enemyPos.y-1));
                towardRight = this.map.getTile(new Vec2i(enemyPos.x+1, enemyPos.y-1));
            }
            if(toward.getType() != TileType.IMPASSABLE && towardLeft.getType() != TileType.IMPASSABLE){
                path.push(new Node<>(towardLeft));
                path.push(new Node<>(toward));
                this.blackboard.setAwayPath(path);
                return BTStatus.SUCCESS;

            } else if(toward.getType() != TileType.IMPASSABLE && towardRight.getType() != TileType.IMPASSABLE){
                path.push(new Node<>(towardRight));
                path.push(new Node<>(toward));
                this.blackboard.setAwayPath(path);
                return BTStatus.SUCCESS;
            }
        } else { // on same horizontal line
            // up or down
            Tile up = this.map.getTile(new Vec2i(enemyPos.x, enemyPos.y-1));
            if(up.getType() != TileType.IMPASSABLE){
                path.push(new Node<>(up));
                this.blackboard.setAwayPath(path);
                return BTStatus.SUCCESS;
            }
            Tile down = this.map.getTile(new Vec2i(enemyPos.x, enemyPos.y+1));
            if(down.getType() != TileType.IMPASSABLE){
                path.push(new Node<>(down));
                this.blackboard.setAwayPath(path);
                return BTStatus.SUCCESS;
            }

            Tile away, awayUp, awayDown;
            Tile toward, towardUp, towardDown;

            // away up or away down
            if(enemyPos.x < playerPos.x){
                away = this.map.getTile(new Vec2i(enemyPos.x-1, enemyPos.y));
                awayUp = this.map.getTile(new Vec2i(enemyPos.x-1, enemyPos.y-1));
                awayDown = this.map.getTile(new Vec2i(enemyPos.x-1, enemyPos.y+1));
            } else {
                away = this.map.getTile(new Vec2i(enemyPos.x+1, enemyPos.y));
                awayUp = this.map.getTile(new Vec2i(enemyPos.x+1, enemyPos.y-1));
                awayDown = this.map.getTile(new Vec2i(enemyPos.x+1, enemyPos.y+1));
            }
            if(away.getType() != TileType.IMPASSABLE && awayUp.getType() != TileType.IMPASSABLE){
                path.push(new Node<>(awayUp));
                path.push(new Node<>(away));
                this.blackboard.setAwayPath(path);
                return BTStatus.SUCCESS;

            } else if(away.getType() != TileType.IMPASSABLE && awayDown.getType() != TileType.IMPASSABLE){
                path.push(new Node<>(awayDown));
                path.push(new Node<>(away));
                this.blackboard.setAwayPath(path);
                return BTStatus.SUCCESS;

            }

            // toward up or toward down
            if(enemyPos.x < playerPos.x){
                toward = this.map.getTile(new Vec2i(enemyPos.x+1, enemyPos.y));
                towardUp = this.map.getTile(new Vec2i(enemyPos.x+1, enemyPos.y-1));
                towardDown = this.map.getTile(new Vec2i(enemyPos.x+1, enemyPos.y+1));
            } else {
                toward = this.map.getTile(new Vec2i(enemyPos.x-1, enemyPos.y));
                towardUp = this.map.getTile(new Vec2i(enemyPos.x-1, enemyPos.y-1));
                towardDown = this.map.getTile(new Vec2i(enemyPos.x-1, enemyPos.y+1));
            }
            if(toward.getType() != TileType.IMPASSABLE && towardUp.getType() != TileType.IMPASSABLE){
                path.push(new Node<>(towardUp));
                path.push(new Node<>(toward));
                this.blackboard.setAwayPath(path);
                return BTStatus.SUCCESS;

            } else if(toward.getType() != TileType.IMPASSABLE && towardDown.getType() != TileType.IMPASSABLE){
                path.push(new Node<>(towardDown));
                path.push(new Node<>(toward));
                this.blackboard.setAwayPath(path);
                return BTStatus.SUCCESS;
            }
        }
        return BTStatus.FAIL;
    }
}
