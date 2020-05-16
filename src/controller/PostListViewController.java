package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import main.UniLinkGUI;
import model.*;
import model.exception.InvalidOfferPriceException;
import model.exception.InvalidUserReply;
import model.exception.PostCloseException;

import java.io.IOException;
import java.util.Optional;
import model.asset.CheckInput;



class PostListViewController extends ListCell<Post> {

    GridPane gp =new GridPane();
    Label lbPostID = new Label();
    Label lbTitle = new Label();
    Label lbDescription = new Label();
    Label lbCreatorID = new Label();
    Label lbStatus = new Label();
    Label lbCol1 = new Label();
    Label lbCol2 = new Label();
    Label lbCol3 = new Label();
    Label lbCol4 = new Label();
    Button btnReply = new Button("Reply");
    Button btnMore = new Button("More Details");
    Image imgPost = new Image("/image/noimage.png");
    ImageView img = new ImageView(imgPost);
    Post lastItem;


    public PostListViewController() {
        super();
        gp.getColumnConstraints().add(new ColumnConstraints(100));
        gp.getColumnConstraints().add(new ColumnConstraints(200));
        gp.getColumnConstraints().add(new ColumnConstraints(100));
        gp.getColumnConstraints().add(new ColumnConstraints(100));
        gp.getColumnConstraints().add(new ColumnConstraints(100));
        gp.getColumnConstraints().add(new ColumnConstraints(60));
        gp.add(img,0,0);
        gp.setRowSpan(img,2);
        gp.add(lbPostID, 1, 0);
        gp.add(lbTitle, 1, 1);
        gp.add(lbDescription, 2, 0);
        gp.add(lbCreatorID, 2, 1);
        gp.add(lbStatus, 3, 0);
        gp.add(lbCol1, 4, 0);
        gp.add(lbCol2, 4, 1);
        gp.add(lbCol3, 5, 0);
        gp.add(lbCol4, 5, 1);

        btnReply.setPrefWidth(50.0);
        gp.add(btnReply, 6, 0);
        gp.add(btnMore, 7, 0);


        btnReply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getReplyPost(getItem());
            }
        });

        btnMore.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)  {

                try {
                    openDetail(getItem(), event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void updateItem(Post item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            lastItem = item;

            lbPostID.setText(item.getId());
            lbPostID.setText(item.getId());
            lbTitle.setText(item.getTitle());
            lbDescription.setText(item.getDescription());
            lbCreatorID.setText(item.getCreatorID());
            lbStatus.setText(item.getStatus().toString());
            if (item instanceof Event) {
                btnReply.setText("Join");
                setStyle("-fx-background-color: #BDBDBD");
                lbCol1.setText(((Event) item).getVenue());
                lbCol2.setText(((Event) item).getDate().toString());
                lbCol3.setText(String.valueOf(((Event) item).getCapacity()));
                lbCol4.setText(String.valueOf(((Event) item).getAttendeeCount()));
            } else if (item instanceof Sale) {
                setStyle("-fx-background-color: #00BCD4");
                lbCol1.setText(String.valueOf(((Sale) item).getHighestOffer()));
                lbCol2.setText(String.valueOf(((Sale) item).getMinRaise()));
                if(item.getCreatorID().compareTo(UniLinkGUI.loggedUserID) == 0)
                        lbCol3.setText(String.valueOf(((Sale) item).getAskPrice()));
            } else {
                setStyle("-fx-background-color: #757575");
                lbCol1.setText(String.valueOf(((Job) item).getProposedPrice()));
                lbCol2.setText(String.valueOf(((Job) item).getLowestOffer()));
            }

            if(item.getCreatorID().compareTo(UniLinkGUI.loggedUserID) != 0)
                btnMore.setVisible(false);

            if (item.getImage().compareTo("noimage") != 0) {
                img.setImage(new Image(getClass().getResourceAsStream("/image/" + item.getImage() + ".png")));
            }

            img.setFitHeight(40);
            img.setFitWidth(50);
            setGraphic(gp);
        }
    }

    private void getReplyPost(Post post)  {
        Dialog alert;
        double offer = 0;
        try {
            if(post instanceof Event) {
                Reply rp = new Reply(post.getId(),0, UniLinkGUI.loggedUserID);
                post.handleReply(rp);
                lbCol4.setText(String.valueOf(((Event) post).getAttendeeCount()));
                alert = new Alert(Alert.AlertType.INFORMATION, "You have been successfully joined.");
                alert.setHeaderText("Congratulation");
                alert.showAndWait();
            }
            else {
                alert = new TextInputDialog();
                alert.setHeaderText("Enter your offer");
                Optional<String> result = alert.showAndWait();
                if (result.isPresent() ) {
                    if(result.get().isEmpty() == false && CheckInput.isDouble(result.get())) {
                        offer = Double.parseDouble(result.get());
                        Reply rp = new Reply(post.getId(), offer, UniLinkGUI.loggedUserID);
                        post.handleReply(rp);
                        lbStatus.setText(post.getStatus().toString());
                        if (post instanceof Job)
                            lbCol2.setText(String.valueOf(((Job) post).getLowestOffer()));
                        else
                            lbCol1.setText(String.valueOf(((Sale) post).getHighestOffer()));
                        alert = new Alert(Alert.AlertType.INFORMATION, "Your offer has been successfully sent.");
                        alert.setHeaderText("Congratulation");
                        alert.showAndWait();
                    }
                    else {
                        alert = new Alert(Alert.AlertType.WARNING, "Your input is wrong!.");
                        alert.setHeaderText("The offer must be number.");
                        alert.showAndWait();
                    }
                }

            }

        } catch (PostCloseException e) {
            alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.showAndWait();
        } catch (InvalidUserReply e) {
            alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.showAndWait();
        } catch (InvalidOfferPriceException e) {
            alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.showAndWait();
        }
    }

    private void openDetail(Post post, ActionEvent event) throws IOException  {
        FXMLLoader loader =new FXMLLoader();
        Scene mainVieScene;
        if(post instanceof Event) {
            loader.setLocation(getClass().getResource("/view/event_details_view.fxml"));
            Parent mainViewParent = loader.load();

            mainVieScene = new Scene(mainViewParent);
            EventDetailsViewController controller = loader.getController();
            controller.initData(post);
        }
        else {
            loader.setLocation(getClass().getResource("/view/jobsale_details_view.fxml"));
            Parent mainViewParent = loader.load();

            mainVieScene = new Scene(mainViewParent);
            JobSaleViewDetailsController controller = loader.getController();
            controller.initData(post);
        }

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainVieScene);
        window.centerOnScreen();
        window.show();
    }


}
