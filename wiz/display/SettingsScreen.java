package wiz.display;

import engine.display.screens.Screen;
import engine.display.screens.ScreenName;
import engine.display.uiElements.*;
import engine.support.FontMetrics;
import engine.support.Vec2d;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Random;

public class SettingsScreen extends Screen {

    private Color primaryColor;
    private ArrayList<Checkbox> checkboxes = new ArrayList<>();

    public SettingsScreen(Color primaryColor) {
        super(ScreenName.SETTINGS);
        this.primaryColor = primaryColor;
        this.addStandardElements();
    }

    private void addStandardElements(){
        Font titleFont = new Font("Courier", 48);
        double centerY = this.screenSize.y / 4;
        double centerX = this.screenSize.x / 2;
        double spacing = 45;
        Text settings = new Text("settings", primaryColor, centerY, titleFont);
        this.add(settings);
        BackButton backButton = new BackButton(primaryColor, new Vec2d(30), new Vec2d(50,30));
        this.add(backButton);
        Text seed = new Text("select seed:", primaryColor, centerY+60, new Font("Courier", 30));
        this.add(seed);
        Checkbox check0 = new Checkbox("check 0", primaryColor, new Vec2d(centerX - 40, centerY+60+spacing), new Vec2d(30));
        this.add(check0);
        this.checkboxes.add(check0);
        Text zero = new Text("0", primaryColor, new Vec2d(centerX, centerY+60+spacing+25), new Font("Courier", 30));
        this.add(zero);
        Checkbox check1 = new Checkbox("check 1", primaryColor, new Vec2d(centerX - 40, centerY+60+2*spacing), new Vec2d(30));
        this.add(check1);
        this.checkboxes.add(check1);
        Text one = new Text("1", primaryColor, new Vec2d(centerX, centerY+60+2*spacing+25), new Font("Courier", 30));
        this.add(one);
        Checkbox check2 = new Checkbox("check 2", primaryColor, new Vec2d(centerX-40, centerY+60+3*spacing), new Vec2d(30));
        this.add(check2);
        this.checkboxes.add(check2);
        Text two = new Text("2", primaryColor, new Vec2d(centerX, centerY+60+3*spacing+25), new Font("Courier", 30));
        this.add(two);
    }

    public long getSeed(){
        for(Checkbox checkbox : checkboxes){
            if(!checkbox.isChecked()){ continue; }
            if(checkbox.getName().contains("0")){
                return 0L;
            } else if(checkbox.getName().contains("1")){
                return 1L;
            } else if(checkbox.getName().contains("2")){
                return 2L;
            }
        }
        return new Random().nextLong();
    }

    public void onMouseClicked(MouseEvent e){
        super.onMouseClicked(e);
        for(UIElement ele : uiElements){
            if(!ele.inRange(e)){ continue; }
            if(ele.getName().contains("Back Button")){
                this.nextScreen = ScreenName.MENU;
            } else if(ele.getName().contains("check")){
                if(((Checkbox)ele).isChecked()){
                    for(Checkbox checkbox : checkboxes){
                        if(checkbox != ele){
                            checkbox.setChecked(false);
                        }
                    }
                }

            }
        }
    }

}
