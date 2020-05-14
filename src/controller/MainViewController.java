package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.UniLinkGUI;
import model.Post;


import java.io.IOException;


public class MainViewController {


    @FXML private ListView<Post> lvPostList;

    // Choice boxes for filtering
    @FXML private ChoiceBox cbPostType;
    @FXML private ChoiceBox cbStatus;
    @FXML private ChoiceBox cbCreator;
    @FXML private  Label loggedUserID;

    public void initialize() {


        loadPost();
        loggedUserID.setText(UniLinkGUI.loggedUserID);

        //cbPostType.getValue().toString();
        cbPostType.getItems().addAll("All","Event","Sale","Job");
        cbStatus.getItems().addAll("All","Open","Closed");
        cbCreator.getItems().addAll("My Posts","All");


    }


    public void initData(String userID){
        loggedUserID.setText(userID);
    }


    public void loggedOut(MouseEvent event) throws IOException {
        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/login_view.fxml"));
        Parent mainViewParent = loader.load();

        Scene mainVieScene = new Scene(mainViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainVieScene);
        window.centerOnScreen();
        window.show();

    }


 //   public ObservableList<Post> getPost() {
 public void loadPost() {

        lvPostList.setItems(UniLinkGUI.postList);
        lvPostList.setCellFactory(new Callback<ListView<Post>, ListCell<Post>>() {
             @Override
             public ListCell<Post> call(ListView<Post> param) {
                 return new PostListViewController();
             }
        });

    }

    public void addNewEvent(ActionEvent event) throws IOException {
        callEvent(event,"event_view","event");
    }

    public void addNewSale(ActionEvent actionEvent) throws IOException{
       callEvent(actionEvent,"sale_view","sale");
    }

    private void callEvent(ActionEvent event, String viewName, String ptType) throws IOException {
        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/"+viewName+".fxml"));
        Parent mainViewParent = loader.load();

        Scene mainVieScene = new Scene(mainViewParent);
        if (ptType.compareTo("event")>0) {
            SaleViewController controller = loader.getController();
            controller.initData(ptType);
        }
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainVieScene);
        window.centerOnScreen();
        window.show();
    }

    public void addNewJob(ActionEvent actionEvent) throws IOException {
        callEvent(actionEvent,"sale_view","job");
    }
}
