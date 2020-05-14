package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.UniLinkGUI;
import model.Event;
import model.Post;
import model.asset.CheckInput;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class EventViewController {

    @FXML private TextField txtTitle;
    @FXML private TextField txtDesc;
    @FXML private TextField txtVenue;
    @FXML private DatePicker dtEventDate;
    @FXML private TextField txtCapacity;

    public void backToMainWindow(ActionEvent event) throws IOException {
        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/main_view.fxml"));
        Parent mainViewParent = loader.load();

        Scene mainVieScene = new Scene(mainViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainVieScene);
        window.centerOnScreen();
        window.show();
    }

    public void addEventDetail(ActionEvent actionEvent) throws IOException{

        if (txtTitle.getText().isBlank() != true && txtVenue.getText().isBlank() != true && CheckInput.isInt(txtCapacity.getText()) && dtEventDate.getValue().toString().isBlank() != true) {
            Post ev = new Event(txtTitle.getText(), txtDesc.getText(), UniLinkGUI.loggedUserID, "noimage", txtVenue.getText(), dtEventDate.getValue(), Integer.parseInt(txtCapacity.getText()));
            UniLinkGUI.postList.add(ev);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/main_view.fxml"));
            Parent mainViewParent = loader.load();

            Scene mainVieScene = new Scene(mainViewParent);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(mainVieScene);
            window.centerOnScreen();
            window.show();
        }
        else {
            Dialog alert = new Alert(Alert.AlertType.WARNING, "YOU MUST INPUT ALL INFORMATION CORRECTLY! ");
            alert.showAndWait();
        }

    }
}
