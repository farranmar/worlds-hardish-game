package last.screens;

import engine.display.screens.Screen;
import engine.display.screens.ScreenName;
import engine.display.uiElements.*;
import engine.support.FontMetrics;
import engine.support.Vec2d;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class SaveScreen extends Screen {

    private static final int numSaveSlots = 10;
    private Color primaryColor;
    private Button[] saveSlotButtons = new Button[numSaveSlots];
    private String saveFile = null;
    private String filePrefix;
    private SaveType type;
    private boolean typingName = false;
    private int saveIndex = -1;
    private ArrayList<UIElement> typingEles;

    public enum SaveType {
        SAVE_LEVEL,
        SAVE_GAME,
        LOAD_LEVEL,
        LOAD_GAME;

        public boolean isLoading(){
            return this == LOAD_GAME || this == LOAD_LEVEL;
        }
    }

    public SaveScreen(ScreenName name, String prefix){
        super(name);
        assert(name == ScreenName.SAVE_LOAD_LEVEL || name == ScreenName.SAVE_LOAD_GAME);
        this.primaryColor = Color.BLACK;
        this.initializeTypingEles();
        Arrays.fill(saveSlotButtons, null);
        this.filePrefix = prefix;
        this.addElements();
        this.loadSaveFiles();
    }

    public SaveScreen(ScreenName name, Color color, String prefix){
        super(name);
        assert(name == ScreenName.SAVE_LOAD_LEVEL || name == ScreenName.SAVE_LOAD_GAME);
        this.primaryColor = color;
        this.initializeTypingEles();
        this.filePrefix = prefix;
        Arrays.fill(saveSlotButtons, null);
        this.addElements();
        this.loadSaveFiles();
    }

    private void loadSaveFiles(){
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(this.filePrefix+"_fileNames.txt"));
            String line = fileReader.readLine();
            while(line != null){
                int colonIndex = line.indexOf(":");
                int slotNum = Integer.parseInt(line.charAt(colonIndex-1)+"");
                String dateTime = line.substring(colonIndex+1);
                Button button = this.saveSlotButtons[slotNum];
                button.setText(dateTime, "Courier");
                button.setName(button.getName()+" deadbeef"+slotNum);
                line = fileReader.readLine();
            }
        } catch (Exception e){
            System.out.println("***Error reading fileNames file");
            return;
        }
    }

    private void addElements(){
        Background background = new Background(Color.rgb(69,69,69, 0.7));
        this.add(background);

        double height = this.screenSize.y * 0.95;
        double width = this.screenSize.x * 0.75;
        Vec2d size = new Vec2d(width, height);
        Vec2d position = new Vec2d((this.screenSize.x - width)/2, (this.screenSize.y-height)/2);
        RoundRect box = new RoundRect(Color.rgb(56,56,56), Color.rgb(62, 82, 67), size, position);
        this.add(box);

        BackButton backButton = new BackButton(primaryColor, new Vec2d(30), new Vec2d(50,30));
        this.add(backButton);

        double sectionHeight = height / (numSaveSlots+1);
        double padding = (height / (numSaveSlots+1)) * 0.15;
        Font font = new Font("Courier", sectionHeight - 2*padding);
        FontMetrics metrics = new FontMetrics("empty", font);
        String slotType = this.filePrefix.contains("level") ? "level" : (this.filePrefix.contains("game") ? "game" : this.filePrefix);
        Text selectSaveSlot = new Text("select "+slotType+" slot:", primaryColor, position.y+metrics.height+padding/2, font);
        this.add(selectSaveSlot);

        for(int i = 0; i < numSaveSlots; i++){
            Button button = new Button(primaryColor, position.y+sectionHeight*(i+1) + padding/2, new Vec2d(width-padding*4, sectionHeight*0.85));
            button.setText("empty", "Courier");
            button.setName(button.getName() + " deadbeef"+i);
            this.add(button);
            this.saveSlotButtons[i] = button;
        }
    }

    public void activate(SaveType type) {
        super.activate();
        this.type = type;
    }

    public String getFilePrefix() {
        return filePrefix;
    }

    public void setFilePrefix(String filePrefix) {
        this.filePrefix = filePrefix;
    }

    // gets **and clears** saveFile (should only be used once at a time, and the return value stored if needed)
    public String getSaveFile(){
        if(saveFile == null){ return null; }
        String ret = this.saveFile;
        this.saveFile = null;
        return ret;
    }

    private void initializeTypingEles(){
        ArrayList<UIElement> eles = new ArrayList<>();
        Background background = new Background(Color.rgb(48, 54, 51, 0.85));
        eles.add(background);
        Font font = new Font("Courier", this.screenSize.y * 0.07);
        TextBox textBox = new TextBox("enter file name:", primaryColor, this.screenSize.y/2, font, 30);
        eles.add(textBox);
        this.typingEles = eles;
    }

    private void addTypingEles(){
        this.typingName = true;
        this.uiElements.addAll(this.typingEles);
    }

    private void removeTypingEles(){
        this.typingName = false;
        for(UIElement ele : this.typingEles){
            this.uiElements.remove(ele);
        }
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
        super.onKeyPressed(e);
        if(this.typingName && e.getCode() == KeyCode.ENTER){
            boolean doneTyping = false;
            for(UIElement ele : this.uiElements){
                if(ele instanceof TextBox){
                    String fileName = ((TextBox)ele).getValue();
                    this.saveSlotButtons[this.saveIndex].setText(fileName, "Courier");
                    this.saveSlotButtons[this.saveIndex].setName(this.saveSlotButtons[this.saveIndex].getName()+" deadbeef"+this.saveIndex);
                    this.saveFile = filePrefix + this.saveIndex + ".xml";
                    doneTyping = true;
                }
            }
            if(doneTyping){ this.removeTypingEles(); }
        }
    }

    public void onMouseMoved(MouseEvent e){
        if(this.typingName){ return; }
        super.onMouseMoved(e);
    }

    public void onMouseClicked(MouseEvent e){
        if(this.typingName){
            return;
        }
        super.onMouseClicked(e);
        boolean typing = false;
        for(UIElement ele : uiElements) {
            if (!ele.inRange(e)) {
                continue;
            }
            String name = ele.getName();
            if(type.isLoading() && name.contains("Back Button")){
                this.nextScreen = ScreenName.MENU;
            } else if (!type.isLoading() && name.contains("Back Button")) {
                this.nextScreen = ScreenName.PAUSE;
            } else if(!type.isLoading() && name.contains("deadbeef")){
                this.saveIndex = Integer.parseInt(name.charAt(name.length()-1)+"");
                typing = true;
            } else if(type.isLoading() && name.contains("deadbeef") && !name.contains("empty")){
                int index = Integer.parseInt(name.charAt(name.length()-1)+"");
                this.saveFile = this.filePrefix + index + ".xml";
                if(this.name == ScreenName.SAVE_LOAD_GAME || type == SaveType.LOAD_GAME){ this.nextScreen = ScreenName.GAME; }
                else { this.nextScreen = ScreenName.EDITOR; }
            }
        }
        if(typing) { this.addTypingEles(); }
    }

    @Override
    public void onResize(Vec2d newSize) {
        this.windowSize = newSize;
        if(newSize.x/newSize.y == 16.0/9.0){
            this.screenSize = newSize;
        } else if(newSize.x/newSize.y < 16.0/9.0){
            double x = newSize.x;
            double y = newSize.x * (9.0/16.0);
            this.screenSize = new Vec2d(x,y);
        } else {
            double x = newSize.y * (16.0/9.0);
            double y = newSize.y;
            this.screenSize = new Vec2d(x,y);
        }

        for(UIElement ele : uiElements){
            ele.onResize(this.windowSize, this.screenSize);
        }
        for(UIElement ele : typingEles){
            ele.onResize(this.windowSize, this.screenSize);
        }
    }

    public void onShutdown(){
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(this.filePrefix+"_fileNames.txt");
            for(int i = 0; i < numSaveSlots; i++){
                String content = this.filePrefix+i+":"+this.saveSlotButtons[i].getTextString()+"\n";
                fileWriter.write(content);
            }
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("***Error writing to filenames file***");
        }
    }

}
