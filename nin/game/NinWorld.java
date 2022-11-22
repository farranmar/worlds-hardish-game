package nin.game;

import engine.game.objects.GameObject;
import engine.game.systems.*;
import engine.game.world.GameWorld;
import engine.support.Vec2d;
import javafx.scene.paint.Color;
import nin.game.objects.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NinWorld extends GameWorld {

    private Player player;
    private static final Color platformColor = Color.rgb(99, 176, 205);
    private static Map<String, Class<? extends GameObject>> classMap = null;

    public NinWorld(){
        this(false);
    }

    public NinWorld(boolean empty){
        super("Nin");
        this.size = new Vec2d(1920, 1080);
        if(!empty){
            this.addSystems();
            this.createObjects();
        }
    }

    private void createObjects(){
        Player p = new Player(this, new Vec2d(30), new Vec2d(900, 440));
        this.add(p);
        this.player = p;
        this.centerObj = p;
        Platform upper = new Platform(this, new Vec2d(420, 30), new Vec2d(520), platformColor);
        this.add(upper);
        Platform lower = new Platform(this, new Vec2d(420, 30), new Vec2d(980, 560), platformColor);
        this.add(lower);
        Block b1 = new Block(this, new Vec2d(45), new Vec2d(1051, 400), 0.1, Color.rgb(65, 184, 116));
        this.add(b1);
        Block b2 = new Block(this, new Vec2d(45), new Vec2d(1168, 400), 0.75, Color.rgb(188, 115, 232));
        this.add(b2);
        Block b3 = new Block(this, new Vec2d(45), new Vec2d(1284, 400), 1, Color.rgb(81, 66, 245));
        this.add(b3);
    }

    private void addSystems(){
        this.addSystem(new CollisionSystem());
        this.addSystem(new GraphicsSystem());
        this.addSystem(new InputSystem());
        this.addSystem(new TickingSystem());
    }

    private static void initializeClassMap(){
        classMap = new HashMap<>();
        classMap.put("Block", Block.class);
        classMap.put("GravityRay", GravityRay.class);
        classMap.put("Platform", Platform.class);
        classMap.put("Player", Player.class);
        classMap.put("Projectile", Projectile.class);
    }

    public static Map<String, Class<? extends GameObject>> getClassMap(){
        if(classMap == null){ initializeClassMap(); }
        return classMap;
    }

    public static NinWorld loadFrom(String fileName) {
        DocumentBuilder builder;
        try{
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (Exception e){
            System.out.println("***Problem building factory to load file***");
            return null;
        }
        Document doc;
        try{
            doc = builder.parse(new File(fileName));
        } catch (Exception e){
            System.out.println("***Problem parsing save file***");
            return null;
        }
        if(classMap == null){ initializeClassMap(); }
        return NinWorld.fromXml(doc, classMap);
    }

    public static NinWorld fromXml(Document doc, Map<String, Class<? extends GameObject>> classMap){
        Element ele = doc.getDocumentElement();
        NinWorld ninWorld = new NinWorld(true);
        Vec2d size = Vec2d.fromXml((Element)(getTopElementsByTagName(ele, "Size").item(0)));
        ninWorld.setSize(size);
        ninWorld.setResult(Result.valueOf(ele.getAttribute("result")));

        // SYSTEMS
        Element systemsEle = (Element)(getTopElementsByTagName(ele, "Systems").item(0));
        ninWorld.addSystemsXml(systemsEle);

        // GAME OBJECTS
        Element objsEle = (Element)(getTopElementsByTagName(ele, "GameObjects").item(0));
        ninWorld.addGameObjectsXml(objsEle, classMap);

        return ninWorld;
    }

}
