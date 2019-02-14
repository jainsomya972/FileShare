package App.java;

import javafx.geometry.Insets;
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
    private ImageView iconView, doneIconView;

    public FileListCell(){
        super();
        btn.setText("remove");
        fileName.setFont(Font.font(12));
        fileName.setPadding(new Insets(5,0,0,10));
        iconView = new ImageView();
        iconView.setFitHeight(27);
        iconView.setFitWidth(27);
        Image doneImage;
        try {
            doneImage = new Image(new FileInputStream(getClass().getResource("/App/images/done.png").getPath()));
            doneIconView = new ImageView(doneImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        doneIconView.setFitHeight(25);
        doneIconView.setFitWidth(25);

        hbox.getChildren().addAll(iconView,fileName,pane,doneIconView);
        hbox.setHgrow(pane, Priority.ALWAYS);
    }

    public void updateItem(String name, boolean empty){

        super.updateItem(name,empty);
        setText(null);
        setGraphic(null);
        doneIconView.setVisible(false);
        try {
            System.out.println("name : "  + name);
            if(name.contains("\n")){ //for checking 1 at the end of file name with space
                doneIconView.setVisible(true);
                name = name.split("\n")[0];
            }
        }
        catch(Exception e){}

        if(name!=null && !empty)
        {
            fileName.setText(name.replace(".dir",""));
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
        else if(ext.equals("mp4") || ext.equals("mkv") || ext.equals("avi") || ext.equals("3gp") || ext.equals("wmv"))
        {
            iconName = "videoFile.png";
        }
        else if(ext.equals("mp3") || ext.equals("aac") || ext.equals("flac") || ext.equals("m4a") || ext.equals("wav"))
        {
            iconName = "audioFile.png";
        }
        //zip file
        else if(ext.equals("zip") || ext.equals("rar") || ext.equals("gz") || ext.equals("xz") || ext.equals("jar"))
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
        //bin file
        else if(ext.equals("exe") || ext.equals("bin") || ext.equals("apk") || ext.equals("dll"))
        {
            iconName = "binFile.png";
        }
        //iso file
        else if(ext.equals("iso"))
        {
            iconName = "isoFile.png";
        }
        else if(ext.equals("dir"))
        {
            iconName = "folder.png";
        }
        System.out.println("Name : " +  name);
        System.out.println("ext : " +  ext);
        System.out.println("iconName : " +  iconName);
        return new Image(new FileInputStream(getClass().getResource("/App/images/" + iconName).getPath()));
    }
}
