package tech.brito.javafacerecognition.swing;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ModelMenu {

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuType getType() {
        return type;
    }

    public void setType(MenuType type) {
        this.type = type;
    }

    public ModelMenu(String icon, String name, MenuType type) {
        this.icon = icon;
        this.name = name;
        this.type = type;
    }

    public ModelMenu() {
    }

    private String icon;
    private String name;
    private MenuType type;

    public Icon toIcon() {
        var diretorio = new java.io.File("").getAbsolutePath() + "\\src\\main\\resources\\icons\\";
        return new ImageIcon(diretorio + icon + ".png");
    }

    public static enum MenuType {
        TITLE, MENU, EMPTY
    }
}
