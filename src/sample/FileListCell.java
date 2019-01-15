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
    private ImageView iconView;

    public FileListCell(){
        super();
        btn.setText("remove");
        fileName.setFont(Font.font(16));
        iconView = new ImageView();
        iconView.setFitHeight(25);
        iconView.setFitWidth(25);
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
            try {
                Image image = getFileIcon(name);
                iconView.setImage(image);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private Image getFileIcon(String name) throws FileNotFoundException {
        String[] arr = name.split("\\.");
        String ext = "bhosda";
        if(arr.length > 0) {
            ext = arr[arr.length - 1];
            ext = ext.trim();
        }
        else
            System.out.println(arr.length);
        ext = ext.toLowerCase();
        String iconName = "txtFile.png";
        //image files
        if(ext.equals("png") || ext.equals("jpg") || ext.equals("bmp"))
        {
            iconName = "imageFile.png";
        }
        //video files
        else if(ext.equals("mp4") || ext.equals("mpv") || ext.equals("avi") || ext.equals("3gp"))
        {
            iconName = "videoFile.png";
        }
        //zip file
        else if(ext.equals("zip") || ext.equals("rar") || ext.equals("gz") || ext.equals("xz"))
        {
            iconName = "zipFile.png";
        }
        //pdf file
        else if(ext.equals("pdf"))
        {
            iconName = "pdfFile.png";
        }
        //doc file
        else if(ext.equals("doc") || ext.equals("docx") || ext.equals("odf"))
        {
            iconName = "wordFile.png";
        }
        //excel file
        else if(ext.equals("xls") || ext.equals("xlsx") || ext.equals("odt"))
        {
            iconName = "excelFile.png";
        }
        System.out.println("Name : " +  name);
        System.out.println("ext : " +  ext);
        System.out.println("iconName : " +  iconName);
        return new Image(new FileInputStream(getClass().getResource(iconName).getPath()));
    }
}
