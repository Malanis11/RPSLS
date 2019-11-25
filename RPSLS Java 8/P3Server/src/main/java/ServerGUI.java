import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ServerGUI extends Application{

	HashMap<String, Scene> sceneMap;
	Server serverConnection;
	ListView<String> listItems;
	static final int picHeight = 150;
	static final int picWidth = 150;
	Label port_label;
	TextField port_field;
	Button port_btn;
	Label p1_choice;
	Label p1_wins;
	Label p2_choice;
	Label p2_wins;
	Label num_clients;
	Label last_winner, last_match_win;
	Label play_again;
	int totalP = 0;
	Label totalPLabel = new Label(" ");

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Rock, Paper, Scissors, Lizard, Spock! - Server");
		
		// labels and textfield corresponding to port number
		port_label = new Label("Enter Port To Listen To: ");
		port_field = new TextField();
		
		// button used to start server
		port_btn = new Button("Begin Listening");
		
		// labels showing player 1s hand choice and current match points
		p1_choice = new Label("Player 1 Choice: N/A");
		p1_wins = new Label("Player 1's Wins: 0");
		
		// labels showing player 2s hand choice and current match points
		p2_choice = new Label("Player 2 Choice: N/A");
		p2_wins = new Label("Player 2's Wins: 0");
		
		// label to show current number of clients connected 
		num_clients = new Label("# Clients: 0");
		
		// label to show last rounds winner
		last_winner = new Label("Last Round Winner: N/A");
		last_match_win = new Label("Last Match's Winner: N/A");
		
		// label to show whether there is a game in progress
		play_again = new Label("Another Match Has Started: No");
		
		// listview used for displaying game messages. One for each scene that uses them
		listItems = new ListView<String>();
		
		/*~~~~~~~~~~~~~~~~~~~~~~Event Handlers~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
		
		// main event handler that initializes the serverconnection class
		port_btn.setOnAction(e->{ primaryStage.setScene(sceneMap.get("server"));
										serverConnection = new Server(data->{
										Platform.runLater(()->{
											// updates game messages
											listItems.getItems().clear();
											ArrayList<AllPlayers> temp = (ArrayList<AllPlayers>) data;
											totalPLabel.setText(String.valueOf(temp.size()));
											char isPlaying;
											for(int i = 0; i < temp.size(); i++){  //update menu listview
												if(temp.get(i).accepted){
													isPlaying = 'Y';
												}
												else{
													isPlaying = 'N';
												}
												listItems.getItems().add("Player " + temp.get(i).playerID + " | IsPlaying: " + isPlaying + " | Opponent: Client " + temp.get(i).vs + " | Last Selected: " + temp.get(i).selection);
											}

										});}, 
										Integer.valueOf(port_field.getText()));						
		});
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
            	if(serverConnection != null)
            	{
	            	try {
						serverConnection.server.mysocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
				Platform.exit();
            }
        });

		/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Set Scene ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
		
		sceneMap = new HashMap<String, Scene>();
		sceneMap.put("server",  createServerGui());
		sceneMap.put("init",  initialGui());
		
		primaryStage.setScene(sceneMap.get("init"));
		primaryStage.show();
	}
	
	/* initialGui
	 * 
	 * This is the first scene that the user sees. Here the user gets to input the port that they 
	 * want to listen to
	 */
	public Scene initialGui() {
		
		// A welcome image is displayed for users
		Image i = new Image("welcome.png");
		ImageView welc = new ImageView(i);
		welc.setFitHeight(picHeight);
		welc.setFitWidth(picWidth);
		welc.setPreserveRatio(true);
		
		// An image depicting how the game works is also provided
		i = new Image("logo.png");
		ImageView logo = new ImageView(i);
		logo.setFitHeight(picHeight);
		logo.setFitWidth(picWidth);
		logo.setPreserveRatio(true);
		
		// Grouping together all input fields and text related to entering connection details into a VBox
		VBox port = new VBox(20, welc, port_label, port_field, port_btn);
		
		BorderPane pane = new BorderPane();
		pane.setLeft(port);
		pane.setCenter(logo);
		
		return new Scene(pane, 400, 300);	
	}

	public Scene createServerGui(){
		Label totalPlayers = new Label("Total Number of Players on Server: ");

		HBox totalPlayersInfo = new HBox(totalPlayers, totalPLabel);

		VBox body = new VBox(totalPlayersInfo, listItems);
		return new Scene( body, 520, 400);
	}
}
