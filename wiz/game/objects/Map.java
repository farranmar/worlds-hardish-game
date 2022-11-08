package wiz.game.objects;

import engine.game.components.DrawComponent;
import engine.game.components.KeyComponent;
import engine.game.components.TickComponent;
import engine.game.objects.GameObject;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import engine.support.Vec2i;
import engine.support.graph.Graph;
import engine.support.graph.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import wiz.game.helpers.MapGenerator;
import wiz.game.helpers.TileType;

import java.util.ArrayList;
import java.util.Random;

public class Map extends GameObject {

    private Tile[][] tiles;
    private Vec2d tileSize;
    private Vec2i dims;
    private int depth;
    private Player player;
    private Vec2i playerPos;
    private GameWorld.Result result = GameWorld.Result.PLAYING;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Minimap minimap;
    private Graph<Tile> graph;

    public Map(GameWorld world, Vec2i dims, int depth, Vec2d tileSize, long seed){
        super(world);
        this.add(new KeyComponent());
        this.add(new TickComponent());
        this.add(new DrawComponent());
        tiles = new Tile[dims.x][dims.y];
        this.dims = dims;
        this.depth = depth;
        this.initialize(tileSize);
        this.tileSize = tileSize;
        this.generate(seed);
        this.createPlayer();
        this.minimap = new Minimap(this, new Vec2d(200,200), new Vec2d(760, 0));
        this.minimap.setPlayerLoc(this.playerPos);
        this.gameWorld.add(minimap);
        this.graph = this.toGraph();
    }

    private void createPlayer(){
        Player player = new Player(this.gameWorld, this, new Vec2d(tileSize.x-2, tileSize.y-2), this.getSpawn(), "blob_sprite_sheet.png");
        this.playerPos = this.getSpawn();
        this.player = player;
        this.gameWorld.addToAdditionQueue(player);
    }

    public Player getPlayer(){
        return this.player;
    }

    public Tile getTile(Vec2d worldPos){
        int x = (int)Math.floor(worldPos.x / tileSize.x);
        int y = (int)Math.floor(worldPos.y / tileSize.y);
        if(x >= 0 && x < this.dims.x && y >= 0 && y < this.dims.y){
            return this.tiles[y][x];
        }
        return null;
    }

    public Tile getTile(Vec2i mapPos){
        return this.tiles[mapPos.y][mapPos.x];
    }

    public Graph<Tile> getGraph(){
        return this.graph;
    }

    public GameWorld.Result getResult(){
        return this.result;
    }

    public void setResult(GameWorld.Result result){
        this.result = result;
    }

    public Vec2i getDims(){
        return this.dims;
    }

    public TileType getType(int i, int j){
        if(i < 0 || j < 0 || i >= dims.x || j >= dims.y){
            return null;
        }
        return tiles[j][i].getType();
    }

    public void setNumEnemies(int nE){
        this.enemies.clear();
        this.addEnemies(nE);
    }

    private void addEnemies(int num){
        Random random = new Random();
        for(int i = 0; i < num; i++){
            Vec2i pos = new Vec2i(-1);
            while(pos.x < 0){
                int x = random.nextInt(tiles[0].length);
                int y = random.nextInt(tiles.length);
                if(tiles[y][x].getType() == TileType.PASSABLE){
                    pos = new Vec2i(x, y);
                }
            }
            Enemy enemy = new Enemy(this.gameWorld, this, new Vec2d(tileSize.x-1, tileSize.y-1), pos, "enemy.png");
            this.enemies.add(enemy);
            this.gameWorld.addToAdditionQueue(enemy);
        }
    }

    public void removeEnemy(Enemy enemy){
        this.enemies.remove(enemy);
        this.gameWorld.addToRemovalQueue(enemy);
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

    public Vec2d getWorldPos(Vec2i mapPos){
        double newX = this.getPosition().x + (mapPos.x * this.tileSize.x);
        double newY = this.getPosition().y + (mapPos.y * this.tileSize.y);
        return new Vec2d(newX+1, newY+1);
    }

    private void initialize(Vec2d tileSize){
        for(int i = 0; i < dims.y; i++){
            for(int j = 0; j < dims.x; j++){
                Vec2d pos = new Vec2d(tileSize.x * j, tileSize.y * i);
                tiles[i][j] = new Tile(this.gameWorld, TileType.IMPASSABLE, tileSize, pos);
            }
        }
    }

    private void generate(long seed){
        MapGenerator mapGen = new MapGenerator(this.dims, this.depth, 3, 3);
        TileType[][] tileTypes = mapGen.generate(seed);
        for(int j = 0; j < dims.y; j++){
            for(int i = 0; i < dims.x; i++){
                Tile tile = tiles[j][i];
                TileType type = tileTypes[j][i];
                tile.setType(type);
                tile.setMapPosition(new Vec2i(i, j));
            }
        }
    }

    private Graph<Tile> toGraph(){
        Graph<Tile> graph = new Graph<>();
        for(int j = 0; j < dims.y; j++){
            for(int i = 0; i < dims.x; i++){
                if(this.tiles[j][i].getType() == TileType.IMPASSABLE){ continue; }
                Node<Tile> main = new Node(this.tiles[j][i]);
                graph.addNode(main);
                if(j > 0 && this.tiles[j-1][i].getType() != TileType.IMPASSABLE){
                    Node<Tile> up = new Node(this.tiles[j-1][i]);
                    graph.connect(main, up);
                    graph.addNode(up);
                }
                if(j < dims.y-1 && this.tiles[j+1][i].getType() != TileType.IMPASSABLE){
                    Node<Tile> down = new Node(this.tiles[j+1][i]);
                    graph.connect(main, down);
                    graph.addNode(down);
                }
                if(i > 0 && this.tiles[j][i-1].getType() != TileType.IMPASSABLE){
                    Node<Tile> left = new Node(this.tiles[j][i-1]);
                    graph.connect(main, left);
                    graph.addNode(left);
                }
                if(i < dims.x-1 && this.tiles[j][i+1].getType() != TileType.IMPASSABLE){
                    Node<Tile> right = new Node(this.tiles[j][i+1]);
                    graph.connect(main, right);
                    graph.addNode(right);
                }
            }
        }
        return graph;
    }

    public void onKeyPressed(KeyEvent e){
        if(e.getCode() == KeyCode.SPACE){
            player.fireProjectile();
        }
        if(player.isMoving()){ return; }
        if(e.getCode() == KeyCode.W){
            if(e.isShiftDown()){
                player.setState(Player.PlayerState.FACING_UP);
                return;
            }
            TileType type = this.tiles[playerPos.y-1][playerPos.x].getType();
            if(type != TileType.IMPASSABLE){
                playerPos = new Vec2i(playerPos.x, playerPos.y-1);
                player.moveTo(playerPos);
                this.minimap.setPlayerLoc(playerPos);
                if(type == TileType.EXIT){ this.result = GameWorld.Result.VICTORY; }
            }
        } else if(e.getCode() == KeyCode.S){
            if(e.isShiftDown()){
                player.setState(Player.PlayerState.FACING_DOWN);
                return;
            }
            TileType type = this.tiles[playerPos.y+1][playerPos.x].getType();
            if(type != TileType.IMPASSABLE){
                playerPos = new Vec2i(playerPos.x, playerPos.y+1);
                player.moveTo(playerPos);
                this.minimap.setPlayerLoc(playerPos);
                if(type == TileType.EXIT){ this.result = GameWorld.Result.VICTORY; }
            }
        } else if(e.getCode() == KeyCode.A){
            if(e.isShiftDown()){
                player.setState(Player.PlayerState.FACING_LEFT);
                return;
            }
            TileType type = this.tiles[playerPos.y][playerPos.x-1].getType();
            if(type != TileType.IMPASSABLE){
                playerPos = new Vec2i(playerPos.x-1, playerPos.y);
                player.moveTo(playerPos);
                this.minimap.setPlayerLoc(playerPos);
                if(type == TileType.EXIT){ this.result = GameWorld.Result.VICTORY; }
            }
        } else if(e.getCode() == KeyCode.D){
            if(e.isShiftDown()){
                player.setState(Player.PlayerState.FACING_RIGHT);
                return;
            }
            TileType type = this.tiles[playerPos.y][playerPos.x+1].getType();
            if(type != TileType.IMPASSABLE){
                playerPos = new Vec2i(playerPos.x+1, playerPos.y);
                player.moveTo(playerPos);
                this.minimap.setPlayerLoc(playerPos);
                if(type == TileType.EXIT){ this.result = GameWorld.Result.VICTORY; }
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
        for(Enemy enemy : enemies){
            enemy.onDraw(g);
        }
        player.onDraw(g);
    }

    public void onTick(long nanosSinceLastTick){
        super.onTick(nanosSinceLastTick);
        this.player.onTick(nanosSinceLastTick);
    }
}
