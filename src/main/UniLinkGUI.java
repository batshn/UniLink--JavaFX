package main;

import database.ReadData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Arrays;

public class UniLinkGUI extends Application {

    public static ObservableList<Post> postList = FXCollections.observableArrayList();
    public static final ArrayList<String> usersList= new ArrayList<String>(Arrays.asList("s1", "s2", "s3", "s4", "s5", "s6", "s7","s8","s9","s10"));
    public static String loggedUserID;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ReadData.readPost();

        Parent root = FXMLLoader.load(getClass().getResource("/view/login_view.fxml"));
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }


}
