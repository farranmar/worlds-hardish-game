package wiz.game.objects;

import engine.game.components.*;
import engine.game.objects.GameObject;
import engine.game.objects.shapes.AAB;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import engine.support.Vec2i;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import wiz.resources.Resource;

import java.util.ArrayList;
import java.util.HashMap;

public class Player extends GameObject {

    private PlayerState state;
    private double speed;
    private Vec2d destination;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private int animationIndex = 0; // index of where we are in animation
    private int indexMax = 8;

    public enum PlayerState {
        FACING_LEFT,
        FACING_RIGHT,
        FACING_UP,
        FACING_DOWN,
        WALKING_LEFT,
        WALKING_RIGHT,
        WALKING_UP,
        WALKING_DOWN,
    }

    private static class SubImage {
        Vec2d position;
        Vec2d size;
        public SubImage(Vec2d size, Vec2d position){
            this.position = position;
            this.size = size;
        }
    }

    private HashMap<PlayerState, ArrayList<SubImage>> subImages = constructSubImageMap(300);

    public Player(GameWorld gameWorld, Vec2d size, Vec2d position, String spriteFile){
        super(gameWorld, size, position);
        this.worldDraw = false;
        this.state = PlayerState.FACING_DOWN;
        this.speed = 5.0;
        this.add(new Collidable(new AAB(size, position)));
        this.add(new Drawable());
        this.add(new HasSprite(new Resource().get(spriteFile)));
        this.add(new Tickable());
        this.setSubImage(subImages.get(this.state).get(0));
    }

    public void setSpeed(double s){
        this.speed = s;
    }

    public PlayerState getState(){
        return this.state;
    }

    public void setState(PlayerState state){
        this.state = state;
    }

    public boolean isMoving(){
        return this.state == PlayerState.WALKING_LEFT || this.state == PlayerState.WALKING_RIGHT || this.state == PlayerState.WALKING_UP || this.state == PlayerState.WALKING_DOWN;
    }

    public void moveTo(Vec2d newPos){
        if(this.isMoving()){
            return;
        }
        this.destination = newPos;
        Vec2d curPos = this.getPosition();
        if(newPos.x < curPos.x){
            this.state = PlayerState.WALKING_LEFT;
        } else if(newPos.x > curPos.x){
            this.state = PlayerState.WALKING_RIGHT;
        } else if(newPos.y < curPos.y){
            this.state = PlayerState.WALKING_UP;
        } else if(newPos.y > curPos.y){
            this.state = PlayerState.WALKING_DOWN;
        }
    }

    public void fireProjectile(){
        Projectile.Direction direction;
        Vec2d curPos = this.getPosition();
        Vec2d curSize = this.getSize();
        Vec2d projSize = new Vec2d(curSize.x/2, curSize.y/2);
        Vec2d projPos;
        if(this.state == PlayerState.FACING_LEFT || this.state == PlayerState.WALKING_LEFT){
            direction = Projectile.Direction.LEFT;
            projPos = new Vec2d(curPos.x - projSize.x, (curPos.y+curSize.y/2) - (projSize.y/2));
        } else if (this.state == PlayerState.FACING_RIGHT || this.state == PlayerState.WALKING_RIGHT){
            direction = Projectile.Direction.RIGHT;
            projPos = new Vec2d(curPos.x+curSize.x, (curPos.y+curSize.y/2) - (projSize.y/2));
        } else if (this.state == PlayerState.FACING_DOWN || this.state == PlayerState.WALKING_DOWN){
            direction = Projectile.Direction.DOWN;
            projPos = new Vec2d((curPos.x + curSize.x/2) - projSize.x/2, curPos.y+curSize.y);
        } else {
            direction = Projectile.Direction.UP;
            projPos = new Vec2d((curPos.x + curSize.x/2) - projSize.x/2, curPos.y);
        }
        Projectile proj = new Projectile(this.gameWorld, this, projSize, projPos, direction);
        this.projectiles.add(proj);
        this.gameWorld.addToAdditionQueue(proj);
    }

    public void removeProjectile(Projectile proj){
        this.gameWorld.addToRemovalQueue(proj);
        this.projectiles.remove(proj);
    }

    private void setSubImage(SubImage subImage){
        for(GameComponent component : components){
            if(component.getTag() == Tag.HAS_SPRITE){
                ((HasSprite)component).setSubImage(subImage.size, subImage.position);
            }
        }
    }

    private HashMap<PlayerState, ArrayList<SubImage>> constructSubImageMap(double hw){
        HashMap<PlayerState, ArrayList<SubImage>> ret = new HashMap<>();
        Vec2d size = new Vec2d(300);
        ArrayList<SubImage> walkingDown = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            Vec2d position = new Vec2d(300*i, 0);
            walkingDown.add(new SubImage(size, position));
        }
        ret.put(PlayerState.WALKING_DOWN, walkingDown);
        ArrayList<SubImage> walkingRight = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            Vec2d position = new Vec2d(300*i, 300);
            walkingRight.add(new SubImage(size, position));
        }
        ret.put(PlayerState.WALKING_RIGHT, walkingRight);
        ArrayList<SubImage> walkingLeft = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            Vec2d position = new Vec2d(300*i, 600);
            walkingLeft.add(new SubImage(size, position));
        }
        ret.put(PlayerState.WALKING_LEFT, walkingLeft);
        ArrayList<SubImage> walkingUp = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            Vec2d position = new Vec2d(300*i, 900);
            walkingUp.add(new SubImage(size, position));
        }
        ret.put(PlayerState.WALKING_UP, walkingUp);
        ArrayList<SubImage> facingDown = new ArrayList<>();
        facingDown.add(new SubImage(size, new Vec2d(0,0)));
        ret.put(PlayerState.FACING_DOWN, facingDown);
        ArrayList<SubImage> facingRight = new ArrayList<>();
        facingRight.add(new SubImage(size, new Vec2d(0,300)));
        ret.put(PlayerState.FACING_RIGHT, facingRight);
        ArrayList<SubImage> facingLeft = new ArrayList<>();
        facingLeft.add(new SubImage(size, new Vec2d(0,600)));
        ret.put(PlayerState.FACING_LEFT, facingLeft);
        ArrayList<SubImage> facingUp = new ArrayList<>();
        facingUp.add(new SubImage(size, new Vec2d(0,900)));
        ret.put(PlayerState.FACING_UP, facingUp);

        return ret;
    }

    public void onDraw(GraphicsContext g){
        super.onDraw(g);
        for(Projectile proj : projectiles){
            proj.onDraw(g);
        }
    }

    public void onTick(long nanosSinceLastTick){

        for(Projectile proj : projectiles){
            proj.onTick(nanosSinceLastTick);
        }

        Vec2d curPos = this.getPosition();
        if(this.state == PlayerState.WALKING_LEFT){
            Vec2d newPos = new Vec2d(curPos.x-speed, curPos.y);
            this.setPosition(newPos);
            if(newPos.x <= destination.x){
                this.state = PlayerState.FACING_LEFT;
                this.animationIndex = 0;
                this.setSubImage(subImages.get(PlayerState.FACING_LEFT).get(animationIndex));
            }
            this.animationIndex = (this.animationIndex+1) % indexMax;
            this.setSubImage(subImages.get(PlayerState.WALKING_LEFT).get((animationIndex - (animationIndex % 2))/2));
        } else if(this.state == PlayerState.WALKING_RIGHT){
            Vec2d newPos = new Vec2d(curPos.x+speed, curPos.y);
            this.setPosition(newPos);
            if(newPos.x >= destination.x){
                this.state = PlayerState.FACING_RIGHT;
                this.animationIndex = 0;
                this.setSubImage(subImages.get(PlayerState.FACING_RIGHT).get(animationIndex));
            }
            this.animationIndex = (this.animationIndex+1) % indexMax;
            this.setSubImage(subImages.get(PlayerState.WALKING_RIGHT).get((animationIndex - (animationIndex % 2))/2));
        } else if(this.state == PlayerState.WALKING_UP){
            Vec2d newPos = new Vec2d(curPos.x, curPos.y-speed);
            this.setPosition(newPos);
            if(newPos.y <= destination.y){
                this.state = PlayerState.FACING_UP;
                this.animationIndex = 0;
                this.setSubImage(subImages.get(PlayerState.FACING_UP).get(animationIndex));
            }
            this.animationIndex = (this.animationIndex+1) % indexMax;
            this.setSubImage(subImages.get(PlayerState.WALKING_UP).get((animationIndex - (animationIndex % 2))/2));
        } else if(this.state == PlayerState.WALKING_DOWN){
            Vec2d newPos = new Vec2d(curPos.x, curPos.y+speed);
            this.setPosition(newPos);
            if(newPos.y >= destination.y){
                this.state = PlayerState.FACING_DOWN;
                this.animationIndex = 0;
                this.setSubImage(subImages.get(PlayerState.FACING_DOWN).get(animationIndex));
            }
            this.animationIndex = (this.animationIndex+1) % indexMax;
            this.setSubImage(subImages.get(PlayerState.WALKING_DOWN).get((animationIndex - (animationIndex % 2))/2));
        }
    }

}
