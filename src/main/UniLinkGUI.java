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

public class UniLinkGUI extends Application {

    public static ObservableList<Post> postList = FXCollections.observableArrayList();
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
