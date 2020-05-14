package controller;

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
import javax.swing.*;
import java.io.*;

public class SaleViewController {
    @FXML private TextField txtTitle;
    @FXML private TextField txtDesc;
    @FXML private TextField txtAskingPrice;
    @FXML private TextField txtMinimumRaise;
    @FXML private Label lbAskingPrice;
    @FXML private Label lbMinimumRaise;
    @FXML private ImageView imgPost;
    private String postType;

    public void initData(String pt){
        postType = pt;
        if(postType.compareTo("job") == 0) {
            lbAskingPrice.setText("Proposed price: ");
            lbMinimumRaise.setVisible(false);
            txtMinimumRaise.setVisible(false);
        }
    }

    public void addSale(ActionEvent actionEvent) throws IOException {
        if (txtTitle.getText().isBlank() != true && CheckInput.isDouble(txtAskingPrice.getText())) {
            if ((postType.compareTo("sale") == 0 && CheckInput.isInt(txtMinimumRaise.getText())) || postType.compareTo("job") == 0) {
                if (postType.compareTo("sale") == 0) {
                    Post sal = new Sale(txtTitle.getText(), txtDesc.getText(), UniLinkGUI.loggedUserID, "noimage", Double.parseDouble(txtAskingPrice.getText()), Integer.parseInt(txtMinimumRaise.getText()));
                    UniLinkGUI.postList.add(sal);
                }
                else {
                    Post job = new Job(txtTitle.getText(), txtDesc.getText(), UniLinkGUI.loggedUserID, "noimage", Double.parseDouble(txtAskingPrice.getText()));
                    UniLinkGUI.postList.add(job);
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
                Dialog alert = new Alert(Alert.AlertType.WARNING, "YOU MUST INPUT ALL INFORMATION CORRECTLY! ");
                alert.showAndWait();
            }
        }
        else {
            Dialog alert = new Alert(Alert.AlertType.WARNING, "YOU MUST INPUT ALL INFORMATION CORRECTLY! ");
            alert.showAndWait();
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

            FileInputStream in = new FileInputStream(selectImage);
            FileOutputStream ou = new FileOutputStream("F:\\Lesson\\RMIT\\Semester1-2020\\Advanced Programming\\SourceGUI\\src\\image\\CUS002.png");
            BufferedInputStream bin = new BufferedInputStream(in);
            BufferedOutputStream bou = new BufferedOutputStream(ou);
            int b = 0;
            while (b != -1) {
                b = bin.read();
                bou.write(b);
            }
            bin.close();
            bou.close();

            Image img = new Image(selectImage.toURI().toString());
            imgPost.setImage(img);
        }
    }
}
