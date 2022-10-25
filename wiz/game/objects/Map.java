package wiz.game.objects;

import engine.game.components.Drawable;
import engine.game.components.Keyable;
import engine.game.components.Tickable;
import engine.game.components.TransformComponent;
import engine.game.objects.GameObject;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import engine.support.Vec2i;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import wiz.display.EndScreen;
import wiz.game.helpers.MapGenerator;
import wiz.game.helpers.TileType;

import java.util.ArrayList;
import java.util.Random;

public class Map extends GameObject {

    Tile[][] tiles;
    Vec2d tileSize;
    Vec2i dims;
    ArrayList<GameObject> items;
    Player player;
    Vec2i playerPos;
    GameWorld.Result result = GameWorld.Result.PLAYING;

    public Map(GameWorld world, Vec2i dims, Vec2d tileSize){
        super(world);
        this.add(new Keyable());
        this.add(new Tickable());
        this.add(new Drawable());
        tiles = new Tile[dims.x][dims.y];
        this.dims = dims;
        this.initialize(tileSize);
        this.tileSize = tileSize;
        this.generate();
        this.createPlayer();
    }

    public Map(GameWorld world, Vec2i dims, Vec2d tileSize, long seed){
        super(world);
        this.add(new Keyable());
        this.add(new Tickable());
        this.add(new Drawable());
        tiles = new Tile[dims.x][dims.y];
        this.dims = dims;
        this.initialize(tileSize);
        this.tileSize = tileSize;
        this.generate(seed);
        this.createPlayer();
    }

    private void createPlayer(){
        Vec2d pos = this.getWorldPos(this.getSpawn());
        Player player = new Player(this.gameWorld, this.tileSize, pos, "teddy_bear.png");
        this.playerPos = this.getSpawn();
        this.player = player;
    }

    public Player getPlayer(){
        return this.player;
    }

    public GameWorld.Result getResult(){
        return this.result;
    }

    public Vec2d getPlayerWorldPos(){
        return this.getWorldPos(playerPos);
    }

    public Vec2d getTileSize(){
        return this.tileSize;
    }

    private Vec2i getSpawn(){
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[0].length; j++){
                if(tiles[i][j].getType() == TileType.SPAWN){
                    return new Vec2i(j, i);
                }
            }
        }
        return new Vec2i(0);
    }

    private Vec2d getWorldPos(Vec2i mapPos){
        double newX = this.getPosition().x + (mapPos.x * this.tileSize.x);
        double newY = this.getPosition().y + (mapPos.y * this.tileSize.y);
        return new Vec2d(newX, newY);
    }

    private void initialize(Vec2d tileSize){
        for(int i = 0; i < dims.y; i++){
            for(int j = 0; j < dims.x; j++){
                Vec2d pos = new Vec2d(tileSize.x * j, tileSize.y * i);
                tiles[i][j] = new Tile(this.gameWorld, TileType.IMPASSABLE, tileSize, pos);
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
                tile.setType(type);
            }
        }
    }

    public void onKeyPressed(KeyEvent e){
        if(e.getCode() == KeyCode.SPACE){
            player.fireProjectile();
        }
        if(player.isMoving()){ return; }
        if(e.getCode() == KeyCode.W){
            TileType type = this.tiles[playerPos.y-1][playerPos.x].getType();
            if(type != TileType.IMPASSABLE){
                playerPos = new Vec2i(playerPos.x, playerPos.y-1);
                player.moveTo(this.getWorldPos(playerPos));
                if(type == TileType.EXIT){ this.result = GameWorld.Result.VICTORY; }
            }
        } else if(e.getCode() == KeyCode.S){
            TileType type = this.tiles[playerPos.y+1][playerPos.x].getType();
            if(type != TileType.IMPASSABLE){
                playerPos = new Vec2i(playerPos.x, playerPos.y+1);
                player.moveTo(this.getWorldPos(playerPos));
                if(type == TileType.EXIT){ this.result = GameWorld.Result.VICTORY; }
            }
        } else if(e.getCode() == KeyCode.A){
            TileType type = this.tiles[playerPos.y][playerPos.x-1].getType();
            if(type != TileType.IMPASSABLE){
                playerPos = new Vec2i(playerPos.x-1, playerPos.y);
                player.moveTo(this.getWorldPos(playerPos));
                if(type == TileType.EXIT){ this.result = GameWorld.Result.VICTORY; }
            }
        } else if(e.getCode() == KeyCode.D){
            TileType type = this.tiles[playerPos.y][playerPos.x+1].getType();
            if(type != TileType.IMPASSABLE){
                playerPos = new Vec2i(playerPos.x+1, playerPos.y);
                player.moveTo(this.getWorldPos(playerPos));
                if(type == TileType.EXIT){ this.result = GameWorld.Result.VICTORY; }
            }
        }
    }

    @Override
    public void onDraw(GraphicsContext g) {
        System.out.println("onDraw in map");
        super.onDraw(g);
        for(Tile[] row : tiles){
            for(Tile tile : row){
                tile.onDraw(g);
            }
        }
        player.onDraw(g);
    }

    public void onTick(long nanosSinceLastTick){
        super.onTick(nanosSinceLastTick);
        this.player.onTick(nanosSinceLastTick);
    }
}
