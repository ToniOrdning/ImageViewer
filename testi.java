import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
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
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;

public class testi extends Application{
    public static void main(String[] args){

        launch(args);

    }

    //Windowsize
    private final double WINDOW_WIDTH = 1024; //Default window width
    private final double WINDOW_HEIGHT = 768;  //Default window height
    private double tempWindowWidth; //For resizing images
    private double tempWindowHeight;//For resizing images

    //Misc
    private List<File> selectedFiles;   //Selecting files with file manager
    private int fileCount = 0;  //For randomizing slideshows
    private BorderPane layout;    //Layout for program
    private ImageView testImageView;    //Viewing images
    private Image showNewImage; //switching image
    private Runnable slideshowImage = () -> {
        nextImage();
    };   //For slideshows
    private Runnable randomSlideshowImage = () -> {
        randomImage();
    };  //For randomized slideshows
    private int imageCounter = 0;   //Keeping track of shown image
    private ScheduledExecutorService sessionChange =
    Executors.newScheduledThreadPool(1);    //For timing picture changes
    Scene primaryScene;

    //Menu
    private MenuBar defaultMenubar;
    private Menu fileMenu;
    private Menu startMenu;
    private Menu viewMenu;
    private MenuItem openMenuItem;
    private MenuItem nextImageMenuItem;
    private MenuItem previousImageMenuItem;
    private MenuItem startSlideshowMenuItem;
    private MenuItem randomSlideshowMenuItem;
    private MenuItem fullscreenMenuItem;

    @Override
    public void start(Stage primaryStage){

        //System.out.println(MAX_WINDOW_SIZE);    //TROUBLESHOOT
        //Setup
        primaryStage.setTitle("Image Viewer");
        layout = new BorderPane();
        primaryScene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);

        //Image
        testImageView = new ImageView();
        testImageView.setFitWidth(WINDOW_WIDTH);
        testImageView.setFitHeight(WINDOW_HEIGHT);
        testImageView.setPreserveRatio(true);

        //MenuItems
        openMenuItem = new MenuItem("Open");
        openMenuItem.setOnAction(e -> {
            chooseFile(testImageView);
        });
        openMenuItem.setAccelerator(new KeyCodeCombination(
            KeyCode.O, KeyCombination.CONTROL_DOWN
        ));
        nextImageMenuItem = new MenuItem("Next");
        nextImageMenuItem.setOnAction(e -> {
            nextImage();
        });
        nextImageMenuItem.setAccelerator(new KeyCodeCombination(
            KeyCode.RIGHT, KeyCombination.CONTROL_ANY
        ));
        previousImageMenuItem = new MenuItem("Previous");
        previousImageMenuItem.setOnAction(e -> {
            previousImage();
        });
        previousImageMenuItem.setAccelerator(new KeyCodeCombination(
            KeyCode.LEFT, KeyCombination.CONTROL_ANY
        ));

        startSlideshowMenuItem = new MenuItem("Slideshow");
        startSlideshowMenuItem.setOnAction(e -> {
            startSlideshow();
        });
        startSlideshowMenuItem.setAccelerator(new KeyCodeCombination(
            KeyCode.F9, KeyCombination.CONTROL_ANY
        ));

        randomSlideshowMenuItem = new MenuItem("Random Slideshow");
        randomSlideshowMenuItem.setOnAction(e -> {
            startRandomSlideshow();
        });
        randomSlideshowMenuItem.setAccelerator(new KeyCodeCombination(
            KeyCode.F10, KeyCombination.CONTROL_ANY
        ));

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
        startMenu.getItems().addAll(startSlideshowMenuItem,
        randomSlideshowMenuItem);

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
        fileChooser.setInitialDirectory(new File("E:\\Backups\\.nsfw\\pics\\"));

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
    
    protected void randomImage(){

        System.out.println("Random start");
        fileCount = selectedFiles.size();
        imageCounter = (int) Math.floor(Math.random() * fileCount + 1);
        System.out.println(imageCounter);
        System.out.println("Random call");
        showNewImage();
        System.out.println("Random end");

    }

    protected void showNewImage(){

        tempWindowWidth = primaryScene.getWidth();
        System.out.println(tempWindowWidth);
        tempWindowHeight = primaryScene.getHeight();
        System.out.println(tempWindowHeight);

        try{
            showNewImage = new Image("file:" + selectedFiles.get(imageCounter),
            0, 0, true, false); //Get new image
        } catch (java.lang.NullPointerException e) {
            System.out.println(e.getMessage());
        }

        if (showNewImage.getWidth() > tempWindowWidth) {
            testImageView.setFitWidth(tempWindowWidth);
        } else {
            testImageView.setFitWidth(showNewImage.getWidth());
        }
        if (showNewImage.getHeight() > tempWindowHeight) {
            testImageView.setFitHeight(tempWindowHeight);
        } else {
            testImageView.setFitHeight(showNewImage.getHeight());
        }

        testImageView.setSmooth(true);

        testImageView.setImage(showNewImage);   //Show new image

    }

    protected void startSlideshow(){

        sessionChange.scheduleAtFixedRate(slideshowImage, 1, 1, TimeUnit.SECONDS);

    }

    protected void startRandomSlideshow(){

        sessionChange.scheduleAtFixedRate(randomSlideshowImage, 1, 1, TimeUnit.SECONDS);

    }

}