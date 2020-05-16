package controller;

import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
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
import model.*;


import java.io.IOException;


public class MainViewController {


    @FXML private ListView<Post> lvPostList;

    // Choice boxes for filtering
    @FXML private ChoiceBox cbPostType;
    @FXML private ChoiceBox cbStatus;
    @FXML private ChoiceBox cbCreator;
    @FXML private  Label loggedUserID;

    public void initialize() {

        loggedUserID.setText(UniLinkGUI.loggedUserID);
        cbPostType.getItems().addAll("All","Event","Sale","Job");
        cbPostType.setValue("All");
        cbStatus.getItems().addAll("All","Open","Closed");
        cbStatus.setValue("All");
        cbCreator.getItems().addAll("All","My Posts");
        cbCreator.setValue("All");

        loadPost();
        cbPostType.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> loadPost());
        cbStatus.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> loadPost());
        cbCreator.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> loadPost());
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


    public void loadPost() {

      // lvPostList.getItems().clear();
        FilteredList<Post> filteredPost = new FilteredList<>(UniLinkGUI.postList, s -> true);
      //  filteredPost.setPredicate(s -> true);

            switch (cbPostType.getValue().toString()) {
                case "Event": {
                    if (cbStatus.getValue().toString().equals("All") == false) {
                        if (cbCreator.getValue().toString().equals("All") == false)
                            filteredPost.setPredicate(s -> s instanceof Event && s.getStatus().toString().equals(cbStatus.getValue().toString().toUpperCase()) && s.getCreatorID().equals(UniLinkGUI.loggedUserID));
                        else
                            filteredPost.setPredicate(s -> s instanceof Event && s.getStatus().toString().equals(cbStatus.getValue().toString().toUpperCase()));
                    }
                    else {
                        if (cbCreator.getValue().toString().equals("All") == false)
                            filteredPost.setPredicate(s -> s instanceof Event && s.getCreatorID().equals(UniLinkGUI.loggedUserID));
                        else
                            filteredPost.setPredicate(s -> s instanceof Event);
                    }
                } break;
                case "Sale": {
                    if (cbStatus.getValue().toString().equals("All") == false) {
                        if (cbCreator.getValue().toString().equals("All") == false)
                            filteredPost.setPredicate(s -> s instanceof Sale && s.getStatus().toString().equals(cbStatus.getValue().toString().toUpperCase()) && s.getCreatorID().equals(UniLinkGUI.loggedUserID));
                        else
                            filteredPost.setPredicate(s -> s instanceof Sale && s.getStatus().toString().equals(cbStatus.getValue().toString().toUpperCase()));
                    }
                    else {
                        if (cbCreator.getValue().toString().equals("All") == false)
                            filteredPost.setPredicate(s -> s instanceof Sale && s.getCreatorID().equals(UniLinkGUI.loggedUserID));
                        else
                            filteredPost.setPredicate(s -> s instanceof Sale);
                    }
                } break;
                case "Job": {
                    if (cbStatus.getValue().toString().equals("All") == false) {
                        if (cbCreator.getValue().toString().equals("All") == false)
                            filteredPost.setPredicate(s -> s instanceof Job && s.getStatus().toString().equals(cbStatus.getValue().toString().toUpperCase()) && s.getCreatorID().equals(UniLinkGUI.loggedUserID));
                        else
                            filteredPost.setPredicate(s -> s instanceof Job && s.getStatus().toString().equals(cbStatus.getValue().toString().toUpperCase()));
                    }
                    else {
                        if (cbCreator.getValue().toString().equals("All") == false)
                            filteredPost.setPredicate(s -> s instanceof Job && s.getCreatorID().equals(UniLinkGUI.loggedUserID));
                        else
                            filteredPost.setPredicate(s -> s instanceof Job);
                    }
                } break;
                default: {
                    if (cbStatus.getValue().toString().equals("All") == false) {
                        if (cbCreator.getValue().toString().equals("All") == false)
                            filteredPost.setPredicate(s -> s.getStatus().toString().equals(cbStatus.getValue().toString().toUpperCase()) && s.getCreatorID().equals(UniLinkGUI.loggedUserID));
                        else
                            filteredPost.setPredicate(s -> s.getStatus().toString().equals(cbStatus.getValue().toString().toUpperCase()));
                    }
                    else {
                        if (cbCreator.getValue().toString().equals("All") == false)
                            filteredPost.setPredicate(s -> s.getCreatorID().equals(UniLinkGUI.loggedUserID));
                    }
                } break;
            }
     //   }


        //if(cbPostType)
     /*   if (cbPostType.getValue().toString().equals("Event"))
           filteredPost.setPredicate(s -> s instanceof Event);
        else if (cbPostType.getValue().toString().equals("Sale"))
                 filteredPost.setPredicate(s -> s instanceof Sale);
             else if (cbPostType.getValue().toString().equals("Job"))
                 filteredPost.setPredicate(s -> s instanceof Job);


        // filter by Status
        if (cbStatus.getValue().toString().equals("All") == false)
            filteredPost.setPredicate(s -> s.getStatus().toString().equals(cbStatus.getValue().toString().toUpperCase()));

        // filter by Creator
        if (cbCreator.getValue().toString().equals("All") == false)
            filteredPost.setPredicate(s -> s.getCreatorID().equals(UniLinkGUI.loggedUserID));
*/
        lvPostList.setItems(filteredPost);
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

    public void openDeveloperWindow(ActionEvent actionEvent) {
        Dialog message = new Dialog();
        message.setTitle("Developer Information");
        message.setHeaderText("   BATSAIKHAN DASHDORJ   s3799204");
        ButtonType buttonTypeOk = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        message.getDialogPane().getButtonTypes().add(buttonTypeOk);
        message.showAndWait();
    }

    public void onExit(ActionEvent actionEvent) {
        Platform.exit();
    }
}
