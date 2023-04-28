package oogasalad.frontend.components.textObjectComponent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import oogasalad.frontend.components.AbstractComponent;

import java.util.Map;
import java.util.ResourceBundle;


public class TextObject extends AbstractComponent implements TextObjectComponent {
    private Text text;
    String content;
    double x;
    double y;
    String colorString;
    double fontSize;
    Color color;

    public TextObject(int id) {
        super(id);
        instantiatePropFile("frontend.properties.Defaults.TextObject");
        this.setDefault();
        this.followMouse();
        this.getNode();
    }

    public TextObject(int ID, Map<String, String> map){
        super(ID);
        setValuesfromMap(map);
        this.followMouse();
        text = new Text(x, y, content);
    }

    @Override
    public void setText(String content) {
        text.setText(content);
    }

    @Override
    public void setTextPosition(double x, double y) {
        text.setX(x);
        text.setY(y);
    }

    @Override
    public void setTextColor(Color color) {
        text.setFill(color);
    }

    @Override
    public void setTextSize(double fontSize) {
        text.setFont(Font.font(fontSize));
    }

    @Override
    public Text getNode() {
        return text;
    }

    @Override
    public void setDefault() {
        content = getDEFAULT_BUNDLE().getString("text.content");
        x = Double.parseDouble(getDEFAULT_BUNDLE().getString("text.x"));
        y = Double.parseDouble(getDEFAULT_BUNDLE().getString("text.y"));
        colorString = getDEFAULT_BUNDLE().getString("text.color");
        fontSize = Double.parseDouble(getDEFAULT_BUNDLE().getString("text.fontSize"));
        color = Color.web(colorString);

        text = new Text(x, y, content);
        text.setFont(Font.font(fontSize));
        text.setFill(Color.BLACK);
    }
}
