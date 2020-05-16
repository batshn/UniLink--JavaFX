package controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.UniLinkGUI;
import model.Event;
import model.Job;
import model.Post;
import model.Sale;
import model.asset.CheckInput;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class SaleViewController {
    @FXML private TextField txtTitle;
    @FXML private TextField txtDesc;
    @FXML private TextField txtAskingPrice;
    @FXML private TextField txtMinimumRaise;
    @FXML private Label lbAskingPrice;
    @FXML private Label lbMinimumRaise;
    @FXML private ImageView imgPost;
    @FXML private Label lbTITLE;
    private String postType;

    public void initData(String pt){
        postType = pt;
        if(postType.compareTo("job") == 0) {
            lbAskingPrice.setText("Proposed price: ");
            lbMinimumRaise.setVisible(false);
            txtMinimumRaise.setVisible(false);
            lbTITLE.setText("THE JOB DETAILS");
        }
    }

    public void addSale(ActionEvent actionEvent) throws IOException {
        String val = "yes";
        if (((postType.compareTo("sale") == 0) && (CheckInput.isInt(txtMinimumRaise.getText()) != true)))
            val = "Minimum Raise";
        if (CheckInput.isDouble(txtAskingPrice.getText()) == false) {
            val = "Asking Price";
            if (postType.compareTo("job") == 0)
                val = "Proposed Price";
        }
        if (txtDesc.getText().isBlank() == true)
            val = "Description";
        if (txtTitle.getText().isBlank() == true)
            val = "Title";

        if (val.compareTo("yes") == 0) {
                String img="0";
                BufferedImage postImg ;
                if(imgPost.getImage() != null)
                    img = "1";

                if (postType.compareTo("sale") == 0) {
                    Post sal = new Sale(txtTitle.getText(), txtDesc.getText(), UniLinkGUI.loggedUserID, Double.parseDouble(txtAskingPrice.getText()), Integer.parseInt(txtMinimumRaise.getText()), img);
                    UniLinkGUI.postList.add(sal);
                    img = sal.getImage();
                }
                else {
                    Post job = new Job(txtTitle.getText(), txtDesc.getText(), UniLinkGUI.loggedUserID, Double.parseDouble(txtAskingPrice.getText()), img);
                    UniLinkGUI.postList.add(job);
                    img = job.getImage();
                }

                if(imgPost.getImage() != null) {
                    postImg = SwingFXUtils.fromFXImage(imgPost.getImage(), null);
                    ImageIO.write(postImg, "png", new File("F:\\Lesson\\RMIT\\Semester1-2020\\Advanced Programming\\SourceGUI\\src\\image\\"+img+".png"));
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

    public void backToMainWindow(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/main_view.fxml"));
        Parent mainViewParent = loader.load();

        Scene mainVieScene = new Scene(mainViewParent);
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(mainVieScene);
        window.centerOnScreen();
        window.show();
    }

    public void copyImage(ActionEvent actionEvent) throws IOException {

        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif");
        fc.getExtensionFilters().add(extFilter);
        File selectImage = fc.showOpenDialog(null);

        if(selectImage != null) {
            Image img = new Image(selectImage.toURI().toString());
            imgPost.setImage(img);
        }
    }
}
