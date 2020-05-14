package controller;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LoginViewController {

    @FXML private TextField txtUserName;
    @FXML private Label lbMessage;

    public void handleClose(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void loggedIn(ActionEvent event) throws IOException {
        if(txtUserName.getText().isEmpty() != true){
            FXMLLoader loader =new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/main_view.fxml"));
            Parent mainViewParent = loader.load();

            Scene mainVieScene = new Scene(mainViewParent);
            MainViewController controller=loader.getController();
            controller.initData(txtUserName.getText());

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(mainVieScene);
            window.centerOnScreen();
            window.show();
        }
        else
            lbMessage.setText("THE USERNAME IS INCORRECT!");
    }

    public void initialize() {
        lbMessage.setText("");
    }
}
