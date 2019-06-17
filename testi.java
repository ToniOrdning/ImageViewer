import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import java.io.File;
import javafx.stage.FileChooser.ExtensionFilter;
import java.util.List;

public class testi extends Application{
    public static void main(String[] args){

        launch(args);

    }

    //Windowsize
    private final int WINDOW_WIDTH = 1200;
    private final int WINDOW_HEIGHT = 700;

    private List<File> selectedFiles;   //Selecting files with file manager
    private VBox layout;    //Layout for program
    private ImageView testImageView;    //Viewing images
    private Image showNewImage; //switching image
    private int imageCounter = 0;   //Keeping track of shown image

    //Buttons
    private Button fileButton;  //Select files
    private Button nextButton; //Button for next image
    private Button previousButton; //Button for previous image

    @Override
    public void start(Stage primaryStage){

        //Setup
        primaryStage.setTitle("Image Viewer");
        layout = new VBox();
        Scene primaryScene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);

        //Image
        testImageView = new ImageView();
        testImageView.setFitWidth(WINDOW_WIDTH);
        testImageView.setFitHeight(WINDOW_HEIGHT);

        //Buttons
        fileButton = new Button("Images");
        fileButton.setOnAction(e -> chooseFile(testImageView));
        nextButton = new Button("Next");
        nextButton.setOnAction(e -> nextImage());
        previousButton = new Button("Previous");
        previousButton.setOnAction(e -> previousImage());

        //Add elements
        layout.getChildren().addAll(fileButton, nextButton, previousButton,
        testImageView);

        //Final setup
        primaryStage.setScene(primaryScene);
        primaryStage.show();

    }

    protected void chooseFile(ImageView viewer){   //Select files for viewing

        imageCounter = 0;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose your pictures");

        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg"),
                new ExtensionFilter("PNG", "*.png"),
                new ExtensionFilter("JPG", "*.jpg"));

        selectedFiles = fileChooser.showOpenMultipleDialog(null);
        
        //System.out.println(selectedFiles);    //TROUBLESHOOT

        showNewImage = new Image("file:" + selectedFiles.get(imageCounter));
        testImageView.setImage(showNewImage);

    }

    protected void nextImage(){

        imageCounter += 1;
        showNewImage = new Image("file:" + selectedFiles.get(imageCounter));
        testImageView.setImage(showNewImage);

    }

    protected void previousImage(){

        imageCounter -= 1;
        showNewImage = new Image("file:" + selectedFiles.get(imageCounter));
        testImageView.setImage(showNewImage);

    }

}