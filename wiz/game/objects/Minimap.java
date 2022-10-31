package wiz.game.objects;

import engine.game.objects.GameObject;
import engine.support.Vec2d;
import engine.support.Vec2i;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import wiz.game.helpers.TileType;

public class Minimap extends GameObject {

    private Vec2d tileSize;
    private Vec2i dims;
    private TileColors[][] tiles;
    private Map map;
    private Vec2i playerPos;

    public enum TileColors{
        PASSABLE(Color.rgb(119,78,120)),
        IMPASSABLE(Color.rgb(72, 54, 86)),
        PLAYER(Color.rgb(102, 222, 102)),
        OUTLINE(Color.rgb(255, 255, 255)),
        EXIT(Color.rgb(29, 19, 33));

        public Color color;
        TileColors(Color c){
            this.color = c;
        }
    }

    public Minimap(Map map, Vec2d size, Vec2d position){
        super(map.getGameWorld(), size, position);
        this.floating = true;
        this.map = map;
        this.dims = new Vec2i(map.getDims().x+2, map.getDims().y+2);
        this.tileSize = new Vec2d(size.x/this.dims.x, size.y/this.dims.y);
        this.construct();
    }

    private void construct(){
        tiles = new TileColors[dims.x][dims.y];
        for(int j = 0; j < dims.y; j++){
            for(int i = 0; i < dims.x; i++){
                if(i == 0 || j == 0 || i == dims.x-1 || j == dims.y-1){
                    tiles[j][i] = TileColors.OUTLINE;
                } else if(this.map.getType(i-1, j-1) == TileType.IMPASSABLE){
                    tiles[j][i] = TileColors.IMPASSABLE;
                } else if(this.map.getType(i-1, j-1) == TileType.EXIT){
                    tiles[j][i] = TileColors.EXIT;
                } else {
                    tiles[j][i] = TileColors.PASSABLE;
                }
            }
        }
    }

    public void setPlayerLoc(Vec2i pos){
        if(playerPos != null){ tiles[playerPos.y][playerPos.x] = TileColors.PASSABLE; }
        tiles[pos.y+1][pos.x+1] = TileColors.PLAYER;
        this.playerPos = new Vec2i(pos.x+1, pos.y+1);
    }

    public void onDraw(GraphicsContext g){
        for(int j = 0; j < this.dims.y; j++){
            for(int i = 0; i < this.dims.x; i++){
                Vec2d pos = new Vec2d(i * tileSize.x, j * tileSize.y).plus(this.getPosition());
                g.setFill(tiles[j][i].color);
                g.fillRect(pos.x, pos.y, this.tileSize.x, this.tileSize.y);
            }
        }
    }

}
