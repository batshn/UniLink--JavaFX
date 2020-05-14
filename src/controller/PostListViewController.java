package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.*;
import model.exception.InvalidOfferPriceException;
import model.exception.InvalidUserReply;
import model.exception.PostCloseException;
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
    Post lastItem;


    public PostListViewController() {
        super();
        gp.getColumnConstraints().add(new ColumnConstraints(100));
        gp.getColumnConstraints().add(new ColumnConstraints(200));
        gp.getColumnConstraints().add(new ColumnConstraints(100));
        gp.getColumnConstraints().add(new ColumnConstraints(100));
        gp.getColumnConstraints().add(new ColumnConstraints(100));
        gp.getColumnConstraints().add(new ColumnConstraints(60));
        gp.add(lbPostID, 0, 0);
        gp.add(lbTitle, 0, 1);
        gp.add(lbDescription, 1, 0);
        gp.add(lbCreatorID, 1, 1);
        gp.add(lbStatus, 2, 0);
        gp.add(lbCol1, 3, 0);
        gp.add(lbCol2, 3, 1);
        gp.add(lbCol3, 4, 0);
        gp.add(lbCol4, 4, 1);

        btnReply.setPrefWidth(50.0);

        gp.add(btnReply, 5, 0);
        gp.add(btnMore, 6, 0);


        btnReply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                getReplyPost(getItem());
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
                lbCol2.setText(((Event) item).getDate());
                lbCol3.setText(String.valueOf(((Event) item).getCapacity()));
                lbCol4.setText(String.valueOf(((Event) item).getAttendeeCount()));
            } else if (item instanceof Sale) {
                setStyle("-fx-background-color: #00BCD4");
                lbCol1.setText(String.valueOf(((Sale) item).getHighestOffer()));
                lbCol2.setText(String.valueOf(((Sale) item).getMinRaise()));
                lbCol3.setText(String.valueOf(((Sale) item).getAskPrice()));
            } else {
                setStyle("-fx-background-color: #757575");
                lbCol1.setText(String.valueOf(((Job) item).getProposedPrice()));
                lbCol2.setText(String.valueOf(((Job) item).getLowestOffer()));
            }

            setGraphic(gp);
        }
    }

    private void getReplyPost(Post post) {
        Dialog alert;
        double offer = 0;
        try {
            if(post instanceof Event) {
                Reply rp = new Reply(post.getId(),0, "S2");
                post.handleReply(rp);
                lbCol4.setText(String.valueOf(((Event) post).getAttendeeCount()));
                alert = new Alert(Alert.AlertType.INFORMATION, "Congs! You have been successfully joined.");
            }
            else {
                alert = new TextInputDialog();
                alert.setHeaderText("Enter your offer");
                Optional<String> result = alert.showAndWait();
                if (result.isPresent() && result.get().isEmpty() == false && CheckInput.isDouble(result.get())) {
                    offer =Double.parseDouble(result.get());
                    Reply rp = new Reply(post.getId(), offer, "S2");
                    post.handleReply(rp);
                    if(post instanceof Job)
                        lbCol2.setText(String.valueOf(((Job) post).getLowestOffer()));
                    else
                        lbCol1.setText(String.valueOf(((Sale) post).getHighestOffer()));
                    alert = new Alert(Alert.AlertType.INFORMATION, "Congs! Your offer has been successfully sent.");
                }
                else
                    alert = new Alert(Alert.AlertType.WARNING, "Your input is wrong!.");
            }

        } catch (PostCloseException e) {
            alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
        } catch (InvalidUserReply e) {
            alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
        } catch (InvalidOfferPriceException e) {
            alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
        }
        alert.showAndWait();
    }



}
