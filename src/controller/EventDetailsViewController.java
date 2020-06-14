package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.UniLinkGUI;
import model.Event;
import model.Post;
import model.Reply;
import model.Status;
import model.asset.CheckInput;
import model.exception.PostCloseException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

public class EventDetailsViewController {
    @FXML private TextField txtTitle;
    @FXML private TextField txtDesc;
    @FXML private TextField txtVenue;
    @FXML private DatePicker dtDate;
    @FXML private TextField txtCapacity;
    @FXML private Label lbAttCount;
    @FXML private ImageView imageEvent;
    @FXML private Button btnSave;
    @FXML private Button btnClose;
    @FXML private Button btnDelete;
    @FXML private Button btnUpload;
    private Post updatePost;

    @FXML private TableView<Reply> tbvEventReplies;
    @FXML private TableColumn<Reply, String> colResponder;


    public void initData(Post post){
        updatePost = post;
        dtDate.setEditable(false);
        txtTitle.setText(updatePost.getTitle());
        txtDesc.setText(updatePost.getDescription());
        if (updatePost instanceof Event) {
            txtVenue.setText(((Event) updatePost).getVenue());
            txtCapacity.setText(String.valueOf(((Event) updatePost).getCapacity()));
            lbAttCount.setText(String.valueOf(((Event) updatePost).getAttendeeCount()));
            dtDate.setValue(((Event) updatePost).getDate());
        }

        if (updatePost.getImage().compareTo("noimage") != 0)
            imageEvent.setImage(new Image("file:images/" + updatePost.getImage() + ".png"));

        if(post.getStatus() == Status.CLOSED || post.getReplyList().isEmpty() == false) {
            txtTitle.setEditable(false);
            txtDesc.setEditable(false);
            txtVenue.setEditable(false);
            txtCapacity.setEditable(false);
            dtDate.setDisable(false);
            btnSave.setDisable(true);
            btnUpload.setDisable(true);
        }

        colResponder.setCellValueFactory(new PropertyValueFactory<Reply, String>("responderID"));
        ObservableList<Reply> replyList = FXCollections.observableArrayList();
        replyList.addAll(updatePost.getReplyList());
        tbvEventReplies.setItems(replyList);

    }

    public void backMainWindow(ActionEvent event) throws IOException {
        FXMLLoader loader =new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/main_view.fxml"));
        Parent mainViewParent = loader.load();

        Scene mainVieScene = new Scene(mainViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainVieScene);
        window.centerOnScreen();
        window.show();
    }

    public void uploadImage(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif");
        fc.getExtensionFilters().add(extFilter);
        File selectImage = fc.showOpenDialog(null);

        if(selectImage != null) {
            Image imgNew = new Image(selectImage.toURI().toString());
            imageEvent.setImage(imgNew);
        }
    }

    public void closePost(ActionEvent actionEvent) throws PostCloseException,IOException {
        Dialog message;
        try {
            updatePost.setStatus();
            message = new Alert(Alert.AlertType.INFORMATION,"The event has been closed successfully.");
            message.setHeaderText("The post is now no longer possible to re-open.");
            message.showAndWait();
            backMainWindow(actionEvent);

        } catch (PostCloseException e) {
            message = new Alert(Alert.AlertType.WARNING, e.getMessage());
            message.showAndWait();
        }

    }

    public void deleteEvent(ActionEvent actionEvent) throws IOException {
        Dialog message = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete the post!");
        message.setTitle("Confirmation");
        message.setHeaderText("If you delete the post, it is no longer to see again.");
        Optional<ButtonType> result = message.showAndWait();
        if((result.isPresent()) && (result.get() == ButtonType.OK)) {
            UniLinkGUI.postList.remove(updatePost);
           /* if (!updatePost.getImage().equals("noimage")) {
                File delFile = new File("images/" + updatePost.getImage() + ".png");
                delFile.delete();
            } */
            updatePost = null;
            backMainWindow(actionEvent);
        }

    }

    public void savePost(ActionEvent actionEvent) throws IOException {

        String val = "yes";
        if (dtDate.getValue() == null)
            val = "Date";
        if (CheckInput.isInt(txtCapacity.getText()) == false)
            val = "Capacity";
        else if (Integer.parseInt(txtCapacity.getText())<=0)
            val = "Capacity";
        if (txtVenue.getText().isBlank() == true)
            val = "Venue";
        if (txtDesc.getText().isBlank() == true)
            val = "Description";
        if (txtTitle.getText().isBlank() == true)
            val = "Title";

        if (val.compareTo("yes") == 0) {
            updatePost.setTitle(txtTitle.getText());
            updatePost.setDescription(txtDesc.getText());
            ((Event) updatePost).setVenue(txtVenue.getText());
            ((Event) updatePost).setCapacity(Integer.parseInt(txtCapacity.getText()));
            ((Event) updatePost).setDate(dtDate.getValue());
            if (imageEvent.getImage() != null) {
                updatePost.setImage(updatePost.getId());
                BufferedImage postImg ;
                postImg = SwingFXUtils.fromFXImage(imageEvent.getImage(), null);
                ImageIO.write(postImg, "png", new File(Paths.get("").toAbsolutePath().toString() + "\\images\\" + updatePost.getId() + ".png"));
            }
            else
                updatePost.setImage("noimage");


            backMainWindow(actionEvent);
        }
        else {
            Dialog message = new Alert(Alert.AlertType.WARNING);
            message.setTitle("WARNING");
            message.setHeaderText("Please enter proper " + val );
            message.showAndWait();
        }

    }
}
