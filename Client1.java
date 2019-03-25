import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client1 extends Application {

    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Group root = new Group();
        primaryStage.setTitle("Simple BBS Client v1.0");
        GridPane gridPane = new GridPane();
        gridPane.setVgap(20);
        gridPane.setHgap(50);
        gridPane.setPadding(new Insets(10, 10, 10, 10));


        Label userNameLbl = new Label("Username: ");
        TextField userNameFld = new TextField();
        Label messageLbl = new Label("Message: ");
        TextField messageFld = new TextField();
        Button sendBtn = new Button("Send");
        sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String myMessage = userNameFld.getText() + ": " + messageFld.getText();
                //System.out.println("Text being sent is " + messageFld.getText());
                System.out.println("Text being sent is " + myMessage);
                //sendMessage(messageFld.getText());
                sendMessage(myMessage);
                messageFld.setText("");
            }
        });
        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction(evt -> System.exit(0));
        

        gridPane.add(userNameLbl, 0, 0);
        gridPane.add(userNameFld, 1, 0);
        gridPane.add(messageLbl, 0, 1);
        gridPane.add(messageFld, 1, 1);
        gridPane.add(sendBtn, 0, 2);
        gridPane.add(exitBtn, 0, 3);


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