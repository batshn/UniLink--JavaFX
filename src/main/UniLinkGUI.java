package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import java.util.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UniLinkGUI extends Application {

    public static ObservableList<Post> postList = FXCollections.observableArrayList();
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Post ev1 = new Event("Job Fair", "Open for everyone", "s1", "noimage","RMIT", "05/05/2020", 1);
        Post ev2 = new Event("Event", "Open for everyone", "s2", "noimage","Monash", "05/05/2020", 1);
        Post sal = new Sale("Sale", "zarna", "s2", "noimage",300,  30);
        Post job = new Job("Job", "ajil bainaa ho", "s2", "noimage",500);

        postList.add(ev1);
        postList.add(ev2);
        postList.add(sal);
        postList.add(job);


            Parent root = FXMLLoader.load(getClass().getResource("/view/login_view.fxml"));
            Scene scene = new Scene(root);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
    }


}
