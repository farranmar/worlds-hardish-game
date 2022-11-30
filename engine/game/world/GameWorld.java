package engine.game.world;

import engine.display.Viewport;
import engine.game.objects.GameObject;
import engine.game.systems.GameSystem;
import engine.support.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class GameWorld {

    protected Viewport viewport;
    protected ArrayList<GameObject> gameObjects = new ArrayList<>();
    protected GameObject centerObj;
    protected ArrayList<GameObject> additionQueue = new ArrayList<>();
    protected ArrayList<GameObject> removalQueue = new ArrayList<>();
    protected ArrayList<GameObject> systemsQueue = new ArrayList<>();

    protected ArrayList<GameSystem> systems = new ArrayList<>();
    protected TreeSet<GameObject> drawOrder = new TreeSet<>(new DrawOrderHelper());
    protected Vec2d size;
    protected String name;
    protected Result result = Result.PLAYING;

    public enum Result {
        VICTORY("VICTORY"),
        DEFEAT("DEFEAT"),
        PLAYING("PLAYING");

        String string;
        Result(String s){
            this.string = s;
        }
        @Override
        public String toString() {
            return string;
        }
    }

    public static final NodeList emptyNodeList = new NodeList() {
        @Override
        public Node item(int index) {
            return null;
        }

        @Override
        public int getLength() {
            return 0;
        }
    };

    public GameWorld(String name){
        this.name = name;
    }

    public void add(GameObject obj){
        if(this.gameObjects.contains(obj)){ return; }
        this.gameObjects.add(obj);
        if(obj.getWorldDraw()){ drawOrder.add(obj); }
        for(GameSystem system : systems){
            system.attemptAdd(obj);
        }
    }

    public void centerOn(GameObject obj){
        this.centerObj = obj;
        this.add(obj);
    }

    public void setSize(Vec2d size) {
        this.size = size;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Viewport getViewport(){
        return this.viewport;
    }

    public void addToAdditionQueue(GameObject obj){
        this.additionQueue.add(obj);
    }

    public void addToSystems(GameObject obj){
        if(this.gameObjects.contains(obj)){ return; }
        this.systemsQueue.add(obj);
    }

    protected void addQueue(){
        for(GameObject obj : this.additionQueue){
            this.add(obj);
        }
        this.additionQueue.clear();
    }

    public void addToRemovalQueue(GameObject obj){
        this.removalQueue.add(obj);
    }

    protected void removeQueue(){
        for(GameObject obj : this.removalQueue){
            this.remove(obj);
        }
        this.removalQueue.clear();
    }

    public void remove(GameObject obj){
        this.gameObjects.remove(obj);
        this.drawOrder.remove(obj);
        for(GameSystem system : systems){
            system.remove(obj);
        }
    }

    protected void addSystemsQueue(){
        for(GameObject obj : this.systemsQueue){
            for(GameSystem sys : this.systems){
                sys.attemptAdd(obj);
            }
        }
        this.systemsQueue.clear();
    }

    public void addSystem(GameSystem sys){
        this.systems.add(sys);
    }

    public void setViewport(Viewport vp){
        this.viewport = vp;
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void setGameObjects(ArrayList<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }

    public ArrayList<GameSystem> getSystems() {
        return systems;
    }

    public void setSystems(ArrayList<GameSystem> systems) {
        this.systems = systems;
    }
    
    public void reset(){
        for(GameObject obj : gameObjects){
            obj.reset();
        }
    }

    public Vec2d getSize(){
        return this.size;
    }

    public Result getResult(){
        return this.result;
    }

    public void onTick(long nanosSinceLastTick){
        for(GameSystem sys : systems){
            if(sys.isTickable() || sys.isCollidable()){
                sys.onTick(nanosSinceLastTick);
            }
        }
        this.addQueue();
        this.removeQueue();
        this.addSystemsQueue();
    }

    public void onLateTick(){
        if(centerObj != null){
            Vec2d centerPos = new Vec2d(this.centerObj.getPosition().x + this.centerObj.getSize().x/2, this.centerObj.getPosition().y + this.centerObj.getSize().y/2);
            Vec2d displaySize = this.viewport.getDisplaySize();
            double newX = 0;
            double newY = 0;
            if(centerPos.x - displaySize.x/2 < 0){
                newX = 0;
            } else if(centerPos.x + displaySize.x/2 > this.size.x){
                newX = this.size.x - displaySize.x;
            } else {
                newX = centerPos.x - displaySize.x/2;
            }
            if(centerPos.y - displaySize.y/2 < 0){
                newY = 0;
            } else if(centerPos.y + displaySize.y/2 > this.size.y){
                newY = this.size.y - displaySize.y;
            } else {
                newY = centerPos.y - displaySize.y/2;
            }
            this.viewport.setDisplayPosition(new Vec2d(newX, newY));
        }
    }

    public void onDraw(GraphicsContext g){
        for(GameSystem sys : systems){
            if(sys.isDrawable()){
                sys.onDraw(g, this.drawOrder);
            }
        }
    }

    public void onResize(Vec2d newWindowSize, Vec2d newScreenSize){
        for(GameObject obj : gameObjects){
            if(!obj.isFloating()){ continue; }
            resizeFloatingObj(obj, newWindowSize, newScreenSize);
            for(GameObject child : obj.getChildren()){
                resizeFloatingObj(child, newWindowSize, newScreenSize);
            }
        }
    }

    private void resizeFloatingObj(GameObject obj, Vec2d newWindowSize, Vec2d newScreenSize){
        Vec2d oldSize = obj.getSize();
        Vec2d oldPos = obj.getPosition();
        Vec2d oldScreenSize = this.viewport.getScreenSize();
        Vec2d oldWindowSize = this.viewport.getWindowSize();

        double width = newScreenSize.x * (oldSize.x / oldScreenSize.x);
        double height = newScreenSize.y * (oldSize.y / oldScreenSize.y);
        obj.setSize(new Vec2d(width, height));

        double leftSpacing = (this.viewport.getWindowSize().x - oldScreenSize.x) / 2;
        double newLeftSpacing = (newWindowSize.x - newScreenSize.x) / 2;
        double x = (oldPos.x - leftSpacing) / oldScreenSize.x * newScreenSize.x + newLeftSpacing;
        double topSpacing = (oldWindowSize.y - oldScreenSize.y) / 2;
        double newTopSpacing = (newWindowSize.y - newScreenSize.y) / 2;
        double y = (oldPos.y - topSpacing) / oldScreenSize.y * newScreenSize.y + newTopSpacing;
        obj.setPosition(new Vec2d(x,y));
    }

    public void onMousePressed(double x, double y) {
        for (GameSystem sys : systems) {
            if (sys.takesInput()) {
                sys.onMousePressed(x, y);
            }
        }
    }

    public void onMouseReleased(double x, double y){
        for(GameSystem sys : systems){
            if(sys.takesInput()){
                sys.onMouseReleased(x, y);
            }
        }
    }

    public void onMouseDragged(double x, double y){
        for(GameSystem sys : systems){
            if(sys.takesInput()){
                sys.onMouseDragged(x, y);
            }
        }
    }

    public void onKeyPressed(KeyEvent e){
        for(GameSystem sys : systems){
            if(sys.takesInput()){
                sys.onKeyPressed(e);
            }
        }
    }

    public void onKeyReleased(KeyEvent e){
        for(GameSystem sys : systems){
            if(sys.takesInput()){
                sys.onKeyReleased(e);
            }
        }
    }

    protected Document toXml(){
        return this.toXml("GameWorld");
    }

    protected Document toXml(String topLevelName) {
        this.addQueue();
        this.removeQueue();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (Exception e){
            System.out.println("***Error creating document builder***");
            return null;
        }
        Document doc = builder.newDocument();
        Element world = doc.createElement(topLevelName);
        world.setAttribute("name", this.name);
        Element worldSize = this.size.toXml(doc, "Size");
        world.appendChild(worldSize);
        world.setAttribute("result", this.result.toString());

        Element systems = doc.createElement("Systems");
        for(GameSystem sys : this.systems){
            Element ele = sys.toXml(doc);
            systems.appendChild(ele);
        }
        world.appendChild(systems);

        Element gameObjects = doc.createElement("GameObjects");
        for(GameObject obj : this.gameObjects){
            Element ele = obj.toXml(doc);
            if(obj == centerObj){
                ele.setAttribute("centerObj", "true");
            } else {
                ele.setAttribute("centerObj", "false");
            }
            gameObjects.appendChild(ele);
        }
        world.appendChild(gameObjects);

        doc.appendChild(world);
        return doc;
    }

    public static List<Element> getTopElementsByTagName(Element ele, String tagName){
        if(ele == null){
            return new ArrayList<>();
        }
        NodeList children = ele.getChildNodes();
        ArrayList<Element> elements = new ArrayList<>();
        for(int i = 0; i < children.getLength(); i++){
            Node n = children.item(i);
            if(n.getNodeType() != Node.ELEMENT_NODE){
                continue;
            }
            Element child = (Element)children.item(i);
            if(child.getTagName().equals(tagName)){
                elements.add(child);
            }
        }
        return elements;
    }

    public void saveTo(String fileName) {
        Document doc;
        try{
            doc = this.toXml();
        } catch (Exception e){
            System.out.println("***Error creating document***");
            return;
        }
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer;
        try{
            transformer = factory.newTransformer();
        } catch (Exception e){
            System.out.println("***Error creating transformer***");
            return;
        }
        DOMSource source = new DOMSource(doc);
        FileWriter writer;
        try{
            writer = new FileWriter(new File(fileName));
        } catch (Exception e){
            System.out.println("***Error creating file/file writer***");
            return;
        }
        StreamResult result = new StreamResult(writer);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        try{
            transformer.transform(source, result);
        } catch (Exception e){
            System.out.println("***Error transforming***");
            return;
        }
    }

    public GameWorld(String fileName, Map<String, Class<? extends GameObject>> classMap) {
        DocumentBuilder builder;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (Exception e){
            System.out.println("***Error creating document builder***");
            return;
        }
        Document doc;
        try {
            doc = builder.parse(new File(fileName));
        } catch (Exception e){
            System.out.println("***Error parsing file***");
            return;
        }
        Element ele = doc.getDocumentElement();
        this.name = ele.getAttribute("name");
        Vec2d size = Vec2d.fromXml(getTopElementsByTagName(ele, "Size").get(0));
        this.setSize(size);
        this.setResult(Result.valueOf(ele.getAttribute("result")));

        // SYSTEMS
        Element systemsEle = getTopElementsByTagName(ele, "Systems").get(0);
        this.addSystemsXml(systemsEle);

        // GAME OBJECTS
        Element objsEle = getTopElementsByTagName(ele, "GameObjects").get(0);
        this.addGameObjectsXml(objsEle, classMap);
    }

    public void addSystemsXml(Element systemsEle){
        List<Element> systemsList = getTopElementsByTagName(systemsEle, "System");
        for(int i = 0; i < systemsList.size(); i++){
            Element sysEle = systemsList.get(i);
            GameSystem sys = GameSystem.fromXml(sysEle);
            this.addSystem(sys);
        }
    }

    public void addGameObjectsXml(Element objsEle, Map<String, Class<? extends GameObject>> classMap){
        List<Element> objsList = getTopElementsByTagName(objsEle, "GameObject");
        for(int i = 0; i < objsList.size(); i++){
            Element objEle = (objsList.get(i));
            String classStr = objEle.getAttribute("class");
            GameObject obj = null;
            if(classStr.equals("GameObject")) {
                obj = new GameObject(objEle, this);
                this.add(obj);
            } else {
                // get class of this GameObject
                Class<?>[] parameterTypes = new Class[]{Element.class, GameWorld.class};
                Constructor<? extends GameObject> constructor;
                try {
                    // get xml constructor of correct class
                    constructor = classMap.get(classStr).getConstructor(parameterTypes);
                } catch (Exception e){
                    System.out.println("***Error getting constuctor of GameObject subclass2***");
                    try {
                        constructor = classMap.get(classStr).getConstructor(parameterTypes);
                    } catch (Exception ex){

                    }
                    continue;
                }
                try {
                    // construct object
                    obj = constructor.newInstance(objEle, this);
                } catch (Exception e){
                    System.out.println("***Error constructing instance of GameObject subclass2***");
                }
                if(obj != null){ this.add(obj); }
            }
            if(obj != null && Boolean.parseBoolean(objEle.getAttribute("centerObj"))){
                this.centerOn(obj);
            }
        }
    }

}
