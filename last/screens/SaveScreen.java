package last.screens;

import engine.display.screens.Screen;
import engine.display.screens.ScreenName;
import engine.display.uiElements.*;
import engine.support.FontMetrics;
import engine.support.Vec2d;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.time.LocalDateTime;
import java.util.Arrays;

public class SaveScreen extends Screen {

    private static final int numSaveSlots = 10;
    private Color primaryColor;
    private Button[] saveSlotButtons = new Button[numSaveSlots];
    private String saveFile = null;
    private String filePrefix;
    private SaveType type;

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
        Arrays.fill(saveSlotButtons, null);
        this.filePrefix = prefix;
        this.addElements();
    }

    public SaveScreen(ScreenName name, Color color, String prefix){
        super(name);
        assert(name == ScreenName.SAVE_LOAD_LEVEL || name == ScreenName.SAVE_LOAD_GAME);
        this.primaryColor = color;
        this.filePrefix = prefix;
        Arrays.fill(saveSlotButtons, null);
        this.addElements();
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
        Text selectSaveSlot = new Text("select "+this.filePrefix+" slot:", primaryColor, position.y+metrics.height+padding/2, font);
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

    public void onMouseClicked(MouseEvent e){
        super.onMouseClicked(e);
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
                int index = Integer.parseInt(name.charAt(name.length()-1)+"");
                String fileName = LocalDateTime.now()+"";
                this.saveSlotButtons[index].setText(fileName, "Courier");
                this.saveSlotButtons[index].setName(this.saveSlotButtons[index].getName()+" deadbeef"+index);
                this.saveFile = this.filePrefix + index + ".xml";
            } else if(type.isLoading() && name.contains("deadbeef") && !name.contains("empty")){
                int index = Integer.parseInt(name.charAt(name.length()-1)+"");
                this.saveFile = this.filePrefix + index + ".xml";
                if(this.name == ScreenName.SAVE_LOAD_GAME){ this.nextScreen = ScreenName.GAME; }
                else { this.nextScreen = ScreenName.EDITOR; }
            }
        }
    }

}
