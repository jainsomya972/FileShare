package sample;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

class FileListCell extends ListCell<String>
{
    private HBox hbox = new HBox();

    private Label fileName = new Label("File name");
    private Pane pane = new Pane();
    private Button btn = new Button();

    public FileListCell() throws FileNotFoundException {
        super();
        btn.setText("remove");
        fileName.setFont(Font.font(16));
        Image icon = new Image(new FileInputStream(getClass().getResource("files.png").getPath()));
        ImageView iconView = new ImageView(icon);
        iconView.setFitHeight(30);
        iconView.setFitWidth(30);
        hbox.getChildren().addAll(iconView,fileName,pane);
        hbox.setHgrow(pane, Priority.ALWAYS);
    }

    public void updateItem(String name, boolean empty){

        super.updateItem(name,empty);
        setText(null);
        setGraphic(null);
        if(name!=null && !empty)
        {
            fileName.setText(name);
            setGraphic(hbox);
        }
    }
}
