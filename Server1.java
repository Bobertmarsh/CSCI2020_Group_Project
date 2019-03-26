

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server1 extends Application {

    TextArea txtLog;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
    	// Sets ip scene
        Group root = new Group();
        primaryStage.setTitle("Group Project - Chat Server");
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(0, 10, 10, 10));
        gridPane.setVgap(20);

        // Creates a menu bar with the option to export -> export to .txt
        MenuBar bar = new MenuBar();
        Menu Export = new Menu("Export");
        MenuItem toTxt = new MenuItem("To .txt");
        bar.getMenus().add(Export);
        Export.getItems().add(toTxt);
        // Creates a text area for the chat log
        txtLog = new TextArea();
        // Button to exit the program properly
        Button btnExit = new Button("Exit");
        btnExit.setOnAction(evt -> System.exit(0));
        //Adds above items to the gridPane
        gridPane.add(bar, 0,0);
        gridPane.add(txtLog, 0, 1);
        gridPane.add(btnExit, 0, 2);

        // Exports chat log to a .txt file
        toTxt.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
		        // Creates a file name for exporting based on date and time
		        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
		        Date date = new Date();
		        String filename = "Log_" + dateFormat.format(date) + ".txt";
		        try
		        {
			        //Creates a print writer, writes to the file, and closes the writer
			        PrintWriter outputStream = new PrintWriter(filename);
			        outputStream.print(txtLog.getText());
			        outputStream.flush();
			        outputStream.close();
			        System.out.println("Export Complete");
		        }
		        catch (FileNotFoundException e)
		        {
			        // Error handling
			        e.printStackTrace();
		        }
	        }
        });
		// Adds everything from the grid to the scene
        root.getChildren().add(gridPane);
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
		// Starts a thread to look for connections
        Thread t = new Thread(new handleConnections());
        t.start();

    }
    //
    public class handleConnections implements Runnable {

        private ServerSocket serverSocket;
		// Looks for connection attempts
        public handleConnections() {
            try {
                this.serverSocket = new ServerSocket(60000);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // If a connection happens get the text the client sent and pass it to the handler
        @Override
        public void run() {
            System.out.println("Thread is running");
            while (true) {
                try {
                    System.out.println("Now accepting socket connections");
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Got a connection");
                    // Launch serverGetTextJob and put it into a thread
                    Thread t = new Thread(new getText(clientSocket));
                    t.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    // Handles the text received from a client
    public class getText implements Runnable {
        private Socket clientSocket;
        private String message;

        public getText (Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
        @Override
        public void run() {
        	// Reads text sent from a client and appends it to the end of the text log
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                while ((message = in.readLine()) != null) {
                    txtLog.appendText(message + "\n");
                    System.out.println("Message being read is " + message);
                }
			// Error handling
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }
}
