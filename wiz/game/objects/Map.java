package wiz.game.objects;

import engine.game.objects.GameObject;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import engine.support.Vec2i;
import javafx.scene.canvas.GraphicsContext;
import wiz.game.helpers.MapGenerator;
import wiz.game.helpers.TileType;

import java.util.Random;

public class Map extends GameObject {

    Tile[][] tiles;
    Vec2i dims;

    public Map(GameWorld world, Vec2i dims, Vec2d tileSize){
        super(world);
        tiles = new Tile[dims.x][dims.y];
        this.dims = dims;
        this.initialize(tileSize);
        this.generate();
    }

    public Map(GameWorld world, Vec2i dims, Vec2d tileSize, long seed){
        super(world);
        tiles = new Tile[dims.x][dims.y];
        this.dims = dims;
        this.initialize(tileSize);
        this.generate(seed);
    }

    private void initialize(Vec2d tileSize){
        for(int i = 0; i < dims.y; i++){
            for(int j = 0; j < dims.x; j++){
                Vec2d pos = new Vec2d(tileSize.x * j, tileSize.y * i);
                tiles[i][j] = new Tile(this.gameWorld, false, tileSize, pos);
            }
        }
    }

    private void generate(){
        long seed = new Random().nextLong();
        this.generate(seed);
    }

    private void generate(long seed){
        MapGenerator mapGen = new MapGenerator(this.dims, 4, 3, 3);
        TileType[][] tileTypes = mapGen.generate(seed);
        for(int i = 0; i < dims.y; i++){
            for(int j = 0; j < dims.x; j++){
                Tile tile = tiles[i][j];
                TileType type = tileTypes[i][j];
                tile.setPassable(type != TileType.IMPASSABLE);
                tile.setSpawn(type == TileType.SPAWN);
                tile.setExit(type == TileType.EXIT);
            }
        }
    }

    @Override
    public void onDraw(GraphicsContext g) {
        super.onDraw(g);
        for(Tile[] row : tiles){
            for(Tile tile : row){
                tile.onDraw(g);
            }
        }
    }
}
