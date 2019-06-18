import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.geometry.Pos;
import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;

public class testi extends Application{
    public static void main(String[] args){

        launch(args);

    }

    //Windowsize
    private final int WINDOW_WIDTH = 1200;  //Default window width
    private final int WINDOW_HEIGHT = 700;  //Default window height
    private final Rectangle2D MAX_WINDOW_SIZE =
    Screen.getPrimary().getBounds();    //For fullscreen images size limit

    //Misc
    private List<File> selectedFiles;   //Selecting files with file manager
    private BorderPane layout;    //Layout for program
    private ImageView testImageView;    //Viewing images
    private Image showNewImage; //switching image
    private Runnable slideshowImage = () -> {
    nextImage();
    };   //For slideshows images
    private int imageCounter = 0;   //Keeping track of shown image
    private double newImageHeight;  //For images height
    private double newImageWidth;   //For images width
    private ScheduledExecutorService sessionChange =
    Executors.newScheduledThreadPool(1);    //For timing picture changes

    //Menu
    private MenuBar defaultMenubar;
    private Menu fileMenu;
    private Menu startMenu;
    private Menu viewMenu;
    private MenuItem openMenuItem;
    private MenuItem nextImageMenuItem;
    private MenuItem previousImageMenuItem;
    private MenuItem startSlideshowMenuItem;
    private MenuItem fullscreenMenuItem;

    @Override
    public void start(Stage primaryStage){

        //System.out.println(MAX_WINDOW_SIZE);    //TROUBLESHOOT
        //Setup
        primaryStage.setTitle("Image Viewer");
        layout = new BorderPane();
        Scene primaryScene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);

        //Image
        testImageView = new ImageView();
        testImageView.setFitWidth(WINDOW_WIDTH);
        testImageView.setFitHeight(WINDOW_HEIGHT);

        //MenuItems
        openMenuItem = new MenuItem("Open");
        openMenuItem.setOnAction(e -> chooseFile(testImageView));
        openMenuItem.setAccelerator(new KeyCodeCombination(
            KeyCode.O, KeyCombination.CONTROL_DOWN
        ));
        nextImageMenuItem = new MenuItem("Next");
        nextImageMenuItem.setOnAction(e -> nextImage());
        nextImageMenuItem.setAccelerator(new KeyCodeCombination(
            KeyCode.RIGHT, KeyCombination.CONTROL_ANY
        ));
        previousImageMenuItem = new MenuItem("Previous");
        previousImageMenuItem.setOnAction(e -> previousImage());
        previousImageMenuItem.setAccelerator(new KeyCodeCombination(
            KeyCode.LEFT, KeyCombination.CONTROL_ANY
        ));

        startSlideshowMenuItem = new MenuItem("Slideshow");
        startSlideshowMenuItem.setOnAction(e -> startSlideshow());

        fullscreenMenuItem = new MenuItem("Fullscreen");
        fullscreenMenuItem.setOnAction(e -> {
            if (primaryStage.isFullScreen() == false){
                primaryStage.setFullScreen(true);
                layout.getChildren().remove(defaultMenubar);
            } else if (primaryStage.isFullScreen() == true){
                primaryStage.setFullScreen(false);
                layout.getChildren().remove(testImageView);
                layout.setTop(defaultMenubar);
                layout.setCenter(testImageView);
            }
        });
        fullscreenMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F11));

        //Menus
        fileMenu = new Menu("File");
        fileMenu.getItems().addAll(openMenuItem, nextImageMenuItem,
        previousImageMenuItem);

        startMenu = new Menu("Start");
        startMenu.getItems().addAll(startSlideshowMenuItem);

        viewMenu = new Menu("View");
        viewMenu.getItems().addAll(fullscreenMenuItem);

        //MenuBar
        defaultMenubar = new MenuBar();
        defaultMenubar.getMenus().addAll(fileMenu, startMenu, viewMenu);

        //Add elements
        layout.setTop(defaultMenubar);
        layout.setCenter(testImageView);

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

        showNewImage();

    }

    protected void nextImage(){

        if (imageCounter >= selectedFiles.size() - 1) {
            imageCounter = 0;
        } else {
            imageCounter += 1;
        }
        showNewImage();

    }

    protected void previousImage(){

        if (imageCounter <= 0) {
            imageCounter = selectedFiles.size() - 1;
        } else {
            imageCounter -= 1;
        }
        showNewImage();

    }

    protected void showNewImage(){

        showNewImage = new Image(("file:" + selectedFiles.get(imageCounter)),
        MAX_WINDOW_SIZE.getWidth(), MAX_WINDOW_SIZE.getHeight(), true, true);
        newImageWidth = showNewImage.getWidth();
        newImageHeight = showNewImage.getHeight();
        
        testImageView.setFitWidth(newImageWidth);
        testImageView.setFitHeight(newImageHeight);

        testImageView.setImage(showNewImage);

    }

    protected void startSlideshow(){

        sessionChange.scheduleAtFixedRate(slideshowImage, 5, 5, TimeUnit.SECONDS);

    }

}