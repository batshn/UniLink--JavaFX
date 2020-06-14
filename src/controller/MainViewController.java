package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.UniLinkGUI;
import model.*;
import database.*;
import org.hsqldb.util.CSVWriter;


import java.io.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MainViewController {

    private List<Post> posts = new ArrayList<Post>();
    private ObservableList<Post> filterPostList  = FXCollections.observableArrayList();

    private static String FILE_NAME = "";

    @FXML private ListView<Post> lvPostList;

    // Choice boxes for filtering
    @FXML private ChoiceBox cbPostType;
    @FXML private ChoiceBox cbStatus;
    @FXML private ChoiceBox cbCreator;
    @FXML private  Label loggedUserID;
    @FXML private AnchorPane mainStage;

    public void initialize() {

        loggedUserID.setText(UniLinkGUI.loggedUserID);
        cbPostType.getItems().addAll("All","Event","Sale","Job");
        cbPostType.setValue("All");
        cbStatus.getItems().addAll("All","Open","Closed");
        cbStatus.setValue("All");
        cbCreator.getItems().addAll("All","My Posts");
        cbCreator.setValue("All");

        lvPostList.setOrientation(Orientation.VERTICAL);


        loadPost();
        cbPostType.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> loadPost());
        cbStatus.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> loadPost());
        cbCreator.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> loadPost());
    }

    // initialize data
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
        posts.clear();
        filterPostList.clear();
        posts.addAll(UniLinkGUI.postList);
        filterPostList.addAll(getPosts(cbPostType.getValue().toString(), cbStatus.getValue().toString(), cbCreator.getValue().toString()));
        lvPostList.setItems(filterPostList);
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
        } else {
            EventViewController controller = loader.getController();
            controller.initData();
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
        showInfoDialog("Developer Information","   BATSAIKHAN DASHDORJ   s3799204");
    }


    public void onExit(ActionEvent actionEvent) {
        WriteData.insertData();
        Platform.exit();
    }

    // write to CSV file
    public void exportToFile(ActionEvent actionEvent) {
        try {
            DirectoryChooser dChooser = new DirectoryChooser();
            File selDirectory = dChooser.showDialog(null);
            FILE_NAME = selDirectory.getAbsolutePath()+"\\export_data.csv";

            FileWriter csvWriter = new FileWriter(FILE_NAME);
            for(Post pt: UniLinkGUI.postList){
                csvWriter.append(pt.getId().substring(0,3));
                csvWriter.append(",");
                csvWriter.append(pt.getTitle());
                csvWriter.append(",");
                csvWriter.append(pt.getDescription());
                csvWriter.append(",");
                csvWriter.append(pt.getCreatorID());
                csvWriter.append(",");
                csvWriter.append(pt.getStatus().toString());

                if (pt instanceof Event) {
                    csvWriter.append(",");
                    csvWriter.append(((Event)pt).getVenue());
                    csvWriter.append(",");
                    csvWriter.append(((Event)pt).getDate().toString());
                    csvWriter.append(",");
                    csvWriter.append(String.valueOf(((Event) pt).getCapacity()));
                    csvWriter.append(",");
                    csvWriter.append(String.valueOf(((Event) pt).getAttendeeCount()));
                }

                if (pt instanceof Sale) {
                    csvWriter.append(",");
                    csvWriter.append(String.valueOf(((Sale) pt).getAskPrice()));
                    csvWriter.append(",");
                    csvWriter.append(String.valueOf(((Sale) pt).getMinRaise()));
                    csvWriter.append(",");
                    csvWriter.append(String.valueOf(((Sale) pt).getHighestOffer()));
                }

                if (pt instanceof Job) {
                    csvWriter.append(",");
                    csvWriter.append(String.valueOf(((Job) pt).getProposedPrice()));
                    csvWriter.append(",");
                    csvWriter.append(String.valueOf(((Job) pt).getLowestOffer()));
                }
                for(Reply rp: pt.getReplyList()){
                    csvWriter.append(",");
                    csvWriter.append(String.valueOf(rp.getValue()));
                    csvWriter.append(",");
                    csvWriter.append(rp.getResponderID());
                }

                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();
            showInfoDialog("File export","  All posts have been written successfully");

        } catch (FileNotFoundException e) {
            System.out.println("\nPost ERROR: File not found");
        } catch (IOException e) {
            System.out.println("\nPost ERROR: initializing stream");
        } catch (Exception e) {
            System.out.println("\nPost ERROR: File is not created.");
        }
    }

    // read from CSV file
    public void readFromFile(ActionEvent actionEvent) {
        try
        {
            FileChooser fc = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV Files", "*.csv");
            fc.getExtensionFilters().add(extFilter);
            File selectFile = fc.showOpenDialog(null);

            String postRows;
            if(selectFile != null) {
                BufferedReader csvReader = new BufferedReader(new FileReader(selectFile.getAbsolutePath()));
                while ((postRows = csvReader.readLine()) != null) {
                    String[] row = postRows.split(",");
                    Status st = Status.CLOSED;
                    if (row[4].stripTrailing().equals("OPEN"))
                        st =Status.OPEN;

                    if (row[0].equals("EVE")) {
                        Post ev = new Event(row[0] + String.format("%03d" , ++Event.idGen),
                                row[1].stripTrailing(),
                                row[2].stripTrailing(),
                                row[3].stripTrailing(),
                                st,
                                row[5].stripTrailing(),
                                LocalDate.parse(row[6]),
                                Integer.parseInt(row[7]),
                                Integer.parseInt(row[8]),
                                "noimage");
                        for(int i=9; i<row.length; i+=2){
                            Reply rp = new Reply(ev.getId(),Double.parseDouble(row[i]), row[i+1]);
                            ev.addReplyToPostFromDbOrFile(rp);
                        }
                        --Event.idGen;
                        UniLinkGUI.postList.add(ev);
                    }

                    if (row[0].equals("SAL")) {
                        Post sal = new Sale(row[0] + String.format("%03d" , ++Sale.idGen),
                                row[1].stripTrailing(),
                                row[2].stripTrailing(),
                                row[3].stripTrailing(),
                                st,
                                Double.parseDouble(row[5].stripTrailing()),
                                Integer.parseInt(row[6]),
                                Double.parseDouble(row[7]),
                                "noimage");
                        for(int i=8; i<row.length; i+=2){
                            Reply rp = new Reply(sal.getId(),Double.parseDouble(row[i]), row[i+1]);
                            sal.addReplyToPostFromDbOrFile(rp);
                        }
                        --Sale.idGen;
                        UniLinkGUI.postList.add(sal);
                    }

                    if (row[0].equals("JOB")) {
                        Post job = new Job(row[0] + String.format("%03d" , ++Job.idGen),
                                row[1].stripTrailing(),
                                row[2].stripTrailing(),
                                row[3].stripTrailing(),
                                st,
                                Double.parseDouble(row[5].stripTrailing()),
                                Double.parseDouble(row[6]),
                                "noimage");
                        for(int i=7; i<row.length; i+=2){
                            Reply rp = new Reply(job.getId(),Double.parseDouble(row[i]), row[i+1]);
                            job.addReplyToPostFromDbOrFile(rp);
                        }
                        --Job.idGen;
                        UniLinkGUI.postList.add(job);
                    }

                }
                csvReader.close();
                loadPost();
                lvPostList.refresh();

            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error initializing stream");
        }
    }


    private void showInfoDialog(String title, String header) {
        Dialog message = new Dialog();
        message.setTitle(title);
        message.setHeaderText(header);
        ButtonType buttonTypeOk = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        message.getDialogPane().getButtonTypes().add(buttonTypeOk);
        message.showAndWait();
    }


    // filter posts  by status, type, and creator
    private List<Post> getPosts(String postType, String status, String uID) {
        List<Post> res  = new ArrayList<Post>();

        if( uID.equalsIgnoreCase("All"))
            res = posts;
        else
            res = posts.stream().filter(c -> c.getCreatorID ().equals(UniLinkGUI.loggedUserID)).collect(Collectors.toList());

        if(postType.equals("Event")) {
            res = res.stream().filter(c -> c instanceof Event).collect(Collectors.toList());
        } else if(postType.equals("Sale")) {
            res = res.stream().filter(c -> c instanceof Sale).collect(Collectors.toList());
        } else if(postType.equals("Job")) {
            res = res.stream().filter(c -> c instanceof Job).collect(Collectors.toList());

        }

        if(!status.equalsIgnoreCase("All")) {
            res = res.stream().filter(c -> c.getStatus().toString().equals(status.toUpperCase())).collect(Collectors.toList());
        }

        return res;
    }
}
