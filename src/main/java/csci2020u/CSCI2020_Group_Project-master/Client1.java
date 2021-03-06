import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client1 extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Group root = new Group();
        primaryStage.setTitle("Group Project - Chat Client");
        GridPane gridPane = new GridPane();
        gridPane.setVgap(20);
        gridPane.setHgap(50);
        gridPane.setPadding(new Insets(10, 10, 10, 10));


        Label userNameLbl = new Label("Username: ");
        TextField txtUserName = new TextField();
        Label messageLbl = new Label("Message: ");
        TextField txtMessage = new TextField();
        Button btnSend = new Button("Send");
        // If enter is pressed while txtMessage is selected it will attempt to send the message
        txtMessage.setOnKeyPressed(event -> {
        	if (event.getCode() == KeyCode.ENTER) {
		        String message = txtUserName.getText() + ": " + txtMessage.getText();
		        System.out.println("Text being sent is " + message);
		        sendMessage(message);
		        txtMessage.setText("");
	        }
        });
        // If the send button is pressed it will attempt to send the message in txtMEssage
        btnSend.setOnAction(event -> {
            String message = txtUserName.getText() + ": " + txtMessage.getText();
            System.out.println("Text being sent is " + message);
            sendMessage(message);
            txtMessage.setText("");
        });
        Button btnExit = new Button("Exit");
        btnExit.setOnAction(evt -> System.exit(0));
        

        gridPane.add(userNameLbl, 0, 0);
        gridPane.add(txtUserName, 1, 0);
        gridPane.add(messageLbl, 0, 1);
        gridPane.add(txtMessage, 1, 1);
        gridPane.add(btnSend, 0, 2);
        gridPane.add(btnExit, 0, 3);


        root.getChildren().addAll(gridPane);
        Scene scene = new Scene(root, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void sendMessage(String message) {
        try {
            Socket socket = new Socket("localhost", 60000);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println(message);
            out.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}