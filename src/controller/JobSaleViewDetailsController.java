package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import model.*;
import model.asset.CheckInput;
import model.exception.PostCloseException;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class JobSaleViewDetailsController {
    @FXML private Label lbTITLE;
    @FXML private TextField txtTitle;
    @FXML private TextField txtDesc;
    @FXML private TextField txtAskPrice;
    @FXML private TextField txtMinRaise;
    @FXML private Label lbAskPrice;
    @FXML private Label lbMinRaise;
    @FXML private ImageView imagePost;
    @FXML private Button btnSave;
    @FXML private Button btnClose;
    @FXML private Button btnDelete;
    @FXML private Button btnUpload;
    private Post updatePost;

    @FXML private TableView<Reply> tbvPostReplies;
    @FXML private TableColumn<Reply, String> colResponder;
    @FXML private TableColumn<Reply, String> colOffer;

    public void initData(Post post){
        updatePost = post;
        txtTitle.setText(updatePost.getTitle());
        txtDesc.setText(updatePost.getDescription());
        if (updatePost instanceof Sale) {
            txtAskPrice.setText(String.valueOf(((Sale) updatePost).getAskPrice()));
            txtMinRaise.setText(String.valueOf(((Sale) updatePost).getMinRaise()));
        }
        else {
            lbTITLE.setText("THE JOB DETAILS");
            lbAskPrice.setText("Proposed Price : ");
            lbMinRaise.setVisible(false);
            txtAskPrice.setText(String.valueOf(((Job) updatePost).getProposedPrice()));
            txtMinRaise.setVisible(false);
        }

        if (updatePost.getImage().compareTo("noimage") != 0)
            imagePost.setImage(new Image("file:images/" + updatePost.getImage() + ".png"));

        if(post.getStatus() == Status.CLOSED || post.getReplyList().isEmpty() == false) {
            txtTitle.setEditable(false);
            txtDesc.setEditable(false);
            txtAskPrice.setEditable(false);
            txtMinRaise.setEditable(false);
            btnSave.setDisable(true);
            btnClose.setDisable(true);
//            btnDelete.setDisable(true);
            btnUpload.setDisable(true);
        }

        colResponder.setCellValueFactory(new PropertyValueFactory<Reply, String>("responderID"));
        colOffer.setCellValueFactory(new PropertyValueFactory<Reply, String>("value"));
        ObservableList<Reply> replyList = FXCollections.observableArrayList();
        replyList.addAll(updatePost.getReplyList());
        tbvPostReplies.setItems(replyList);

        if(post instanceof Sale)
            colOffer.setSortType(TableColumn.SortType.DESCENDING);
        else
            colOffer.setSortType(TableColumn.SortType.ASCENDING);
        tbvPostReplies.getSortOrder().add(colOffer);

    }

    public void closePost(ActionEvent actionEvent) throws IOException {
        Dialog message;
        try {
            updatePost.setStatus();
            message = new Alert(Alert.AlertType.INFORMATION,"The post has been closed successfully.");
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
            updatePost = null;
            backMainWindow(actionEvent);
        }
    }

    public void savePost(ActionEvent actionEvent) throws IOException {
        String val = "yes";
        if ((updatePost instanceof Sale) && (CheckInput.isInt(txtMinRaise.getText()) != true))
            val = "Minimum Raise";
        if (CheckInput.isDouble(txtAskPrice.getText()) == false) {
            val = "Asking Price";
            if (updatePost instanceof Job)
                val = "Proposed Price";
        }
        if (txtDesc.getText().isBlank() == true)
            val = "Description";
        if (txtTitle.getText().isBlank() == true)
            val = "Title";

        if (val.compareTo("yes") == 0) {
            updatePost.setTitle(txtTitle.getText());
            updatePost.setDescription(txtDesc.getText());
            if (updatePost instanceof Sale) {
                ((Sale) updatePost).setAskPrice(Double.parseDouble(txtAskPrice.getText()));
                ((Sale) updatePost).setMinRaise(Integer.parseInt(txtMinRaise.getText()));
            } else
                ((Job) updatePost).setProposedPrice(Double.parseDouble(txtAskPrice.getText()));

            if (imagePost.getImage() != null)
                updatePost.setImage(updatePost.getId());
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
            imagePost.setImage(imgNew);
        }
    }
}
