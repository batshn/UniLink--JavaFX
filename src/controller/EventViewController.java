package controller;

import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.UniLinkGUI;
import model.Event;
import model.Post;
import model.asset.CheckInput;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class EventViewController {

    @FXML private TextField txtTitle;
    @FXML private TextField txtDesc;
    @FXML private TextField txtVenue;
    @FXML private DatePicker dtEventDate;
    @FXML private TextField txtCapacity;
    @FXML private ImageView imgVenue;

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
        String val = "yes";
        if (dtEventDate.getValue() == null)
            val = "Date";
        if (CheckInput.isInt(txtCapacity.getText()) == false)
            val = "Capacity";
        if (txtVenue.getText().isBlank() == true)
            val = "Venue";
        if (txtDesc.getText().isBlank() == true)
            val = "Description";
        if (txtTitle.getText().isBlank() == true)
            val = "Title";

        if (val.compareTo("yes") == 0) {
            String img="noimage";
            BufferedImage postImg ;
            if(imgVenue.getImage() != null)
                img = "yes";

            Post ev = new Event(txtTitle.getText(), txtDesc.getText(), UniLinkGUI.loggedUserID, txtVenue.getText(), dtEventDate.getValue(), Integer.parseInt(txtCapacity.getText()), img);
            UniLinkGUI.postList.add(ev);
            img = ev.getImage();

            if(imgVenue.getImage() != null) {
                postImg = SwingFXUtils.fromFXImage(imgVenue.getImage(), null);
                ImageIO.write(postImg, "png", new File("F:\\Lesson\\RMIT\\Semester1-2020\\Advanced Programming\\SourceGUI\\images\\"+img+".png"));
            }

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
            Dialog message = new Alert(Alert.AlertType.WARNING);
            message.setTitle("WARNING");
            message.setHeaderText("Please enter proper " + val );
            message.showAndWait();
        }

    }

    public void uploadImage(ActionEvent actionEvent) throws IOException {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif");
        fc.getExtensionFilters().add(extFilter);
        File selectImage = fc.showOpenDialog(null);

        if(selectImage != null) {
            Image imgNew = new Image(selectImage.toURI().toString());
            imgVenue.setImage(imgNew);
        }
    }
}
