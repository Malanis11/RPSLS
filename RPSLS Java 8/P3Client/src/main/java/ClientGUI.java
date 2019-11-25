import java.io.IOException;
import java.util.HashMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ClientGUI extends Application{

	HashMap<String, Scene> sceneMap;
	Client clientConnection;
	ListView<String> listItems_play, listItems_results;
	Label ip_label;
	TextField ip_field;
	Label port_label;
	TextField port_field;
	Button connect_btn;
	Button b1,b2,b3,b4,b5;
//	Label player_score_play, opponent_score_play;
//	Label player_score_results, opponent_score_results;
	EventHandler<ActionEvent> sel1, sel2, sel3, sel4, sel5, next, quit, again, retur;
	static final int picHeight = 150;
	static final int picWidth = 150;
	Button next_match, quit_btn, play_again;
	ImageView rock, scis, pap, liz, spoc;
	Image r,s,p,sp,l;
	ImageView player_choice, opponent_choice;

	ListView<String> listOfPlayers; //listview for selecting players
	int totalPlayers = 0; //total number of players on server
	Label playerChallenge;
	Button gameAccept, gameDecline;
	Label playerId = new Label(" ");
	Label pl,o;
	Label numPlayersInt;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Rock, Paper, Scissors, Lizard, Spock!");
		
		// labels and textfield corresponding to IP address
		ip_label = new Label("Enter IP Address To Connect To: ");
		ip_field = new TextField();
		
		// labels and textfield corresponding to port number
		port_label = new Label("Enter Port To Listen To: ");
		port_field = new TextField();
		
		// button used to connect to server once relevant information is entered
		connect_btn = new Button("Connect");
		
		// button to enter next match once a player reaches 3 wins and current match is over
		next_match = new Button("Next Match");
		next_match.setDisable(true);
		
		// button to end the game 
		quit_btn = new Button("Quit");
		quit_btn.setDisable(true);
		
		// button to enter next round after current round's results are displayed 
		play_again = new Button("Play Next Round");
		play_again.setDisable(true);
		
		// listview used for displaying game messages. One for each scene that uses them
		listItems_play = new ListView<String>();
		listItems_results = new ListView<String>();

		listOfPlayers = new ListView<String>();
		listOfPlayers.setOnMouseClicked(click-> {
			//Listview is now clickable, get elements of that listview index and send information to other client to challenge them
			String string = listOfPlayers.getSelectionModel().getSelectedItem();

			if(string.charAt(8)-'0' != Integer.parseInt(playerId.getText())){
				if(string.charAt(23) == 'N'){
					listOfPlayers.setDisable(true);

					String p1 = playerId.getText();
					int p2 = string.charAt(8) - '0';

					playerChallenge.setText("Player  " + p2 + " has been challenged.");

					InfoPass temp = new InfoPass();
					temp.p1ID = Integer.parseInt(p1);
					temp.p2ID = p2;
					temp.accepted = true;
					temp.status = -2;

					clientConnection.send(temp);
				}
			};
		});
		
		//  creating images for each hand option setting height and width to predefined valus
		// rock. These will be used in the playfield scene
		r = new Image("Rock.png");
		rock = new ImageView(r);
		rock.setFitHeight(picHeight);
		rock.setFitWidth(picWidth);
		rock.setPreserveRatio(true);
		
		//scissors
		s = new Image("Scissors.png");
		scis = new ImageView(s);
		scis.setFitHeight(picHeight);
		scis.setFitWidth(picWidth);
		scis.setPreserveRatio(true);
		
		//paper
		p = new Image("Paper.png");
		pap = new ImageView(p);
		pap.setFitHeight(picHeight);
		pap.setFitWidth(picWidth);
		pap.setPreserveRatio(true);
		
		//lizard
		l = new Image("Lizard.png");
		liz = new ImageView(l);
		liz.setFitHeight(picHeight);
		liz.setFitWidth(picWidth);
		liz.setPreserveRatio(true);
		
		//spock
		sp = new Image("Spock.png");
		spoc = new ImageView(sp);
		spoc.setFitHeight(picHeight);
		spoc.setFitWidth(picWidth);
		spoc.setPreserveRatio(true);
		
		// These are image views that will be used to display the hands that each player 
		// chose for each round in the results scene
		player_choice = new ImageView();
		player_choice.setFitHeight(picHeight);
		player_choice.setFitWidth(picWidth);
		player_choice.setPreserveRatio(true);
		
		opponent_choice = new ImageView();
		opponent_choice.setFitHeight(picHeight);
		opponent_choice.setFitWidth(picWidth);
		opponent_choice.setPreserveRatio(true);
		
		/*~~~~~~~~~~~~~~~~~~~~~~Event Handlers~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

		/*Changed from Proj 3 to Proj 4*/
		/*
		 * 1: gameAccept - accepting challenge request button (replacing connect button's original intention)
		 * 2: gameDecline - declining challenge request button (just disabling buttons, update InfoPass with a return
		 * 					message (Challenge Declined) and dont change scenes
		 *
		 */

		sel1 = new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				b1.setDisable(true);
				b2.setDisable(true);
				b3.setDisable(true);
				b4.setDisable(true);
				b5.setDisable(true);
				InfoPass info = new InfoPass();
				info.p1Play = "Rock";
				info.p1ID = Integer.parseInt(playerId.getText());
				info.status = -3;
				clientConnection.send(info);
			}
		};

		// event handler for button attached to image of scissors
		sel2 = new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				b1.setDisable(true);
				b2.setDisable(true);
				b3.setDisable(true);
				b4.setDisable(true);
				b5.setDisable(true);
				InfoPass info = new InfoPass();
				info.p1Play = "Scissors";
				info.p1ID = Integer.parseInt(playerId.getText());
				info.status = -3;
				clientConnection.send(info);
			}
		};

		// event handler for button attached to image of paper
		sel3 = new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				b1.setDisable(true);
				b2.setDisable(true);
				b3.setDisable(true);
				b4.setDisable(true);
				b5.setDisable(true);
				InfoPass info = new InfoPass();
				info.p1Play = "Paper";
				info.p1ID = Integer.parseInt(playerId.getText());
				info.status = -3;
				clientConnection.send(info);
			}
		};

		// event handler for button attached to image of lizard
		sel4 = new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				b1.setDisable(true);
				b2.setDisable(true);
				b3.setDisable(true);
				b4.setDisable(true);
				b5.setDisable(true);
				InfoPass info = new InfoPass();
				info.p1Play = "Lizard";
				info.p1ID = Integer.parseInt(playerId.getText());
				info.status = -3;
				clientConnection.send(info);
			}
		};

		// event handler for button attached to image of spock
		sel5 = new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				b1.setDisable(true);
				b2.setDisable(true);
				b3.setDisable(true);
				b4.setDisable(true);
				b5.setDisable(true);
				InfoPass info = new InfoPass();
				info.p1Play = "Spock";
				info.p1ID = Integer.parseInt(playerId.getText());
				info.status = -3;
				clientConnection.send(info);
			}
		};
		again = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				// changes scene and enables all buttons again
				primaryStage.setScene(sceneMap.get("playfield"));
				b1.setDisable(false);
				b2.setDisable(false);
				b3.setDisable(false);
				b4.setDisable(false);
				b5.setDisable(false);
			}
		};
		// button for returning to menu after round
		retur = new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				primaryStage.setScene(sceneMap.get("menu"));
				gameAccept.setDisable(true);
				gameDecline.setDisable(true);
				listOfPlayers.setDisable(false);
				InfoPass temp = new InfoPass();
				temp.status = -6;
				temp.p1ID = Integer.parseInt(playerId.getText());
				playerChallenge.setText(" ");
				b1.setDisable(false);
				b2.setDisable(false);
				b3.setDisable(false);
				b4.setDisable(false);
				b5.setDisable(false);
				clientConnection.send(temp);
			}
		};
		quit = new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				if(clientConnection != null)
				{
					try {
						clientConnection.socketClient.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Platform.exit();
			}
		};

		sceneMap = new HashMap<String, Scene>();
		sceneMap.put("init",  initial());
		sceneMap.put("playfield",  playField());
		sceneMap.put("results",  results());
		sceneMap.put("menu", menu());

		// main event handler that initializes the clientconnection class
		//CHANGED* connect_btn.setOnAction(e-> {primaryStage.setScene(sceneMap.get("playfield"));
		connect_btn.setOnAction(e-> {primaryStage.setScene(sceneMap.get("menu"));
											clientConnection = new Client(data->{
											Platform.runLater(()->{
												// updates game messages
												listOfPlayers.getItems().clear();
												InfoPass temp = (InfoPass)data;
												totalPlayers = temp.playerlist.size();
												numPlayersInt.setText(Integer.toString(totalPlayers));
												char isPlaying;
												for(int i = 0; i< temp.playerlist.size(); i++){
													if(temp.playerlist.get(i).accepted){
														isPlaying = 'Y';
													}
													else{
														isPlaying = 'N';
													}
													//update listview of player status
													listOfPlayers.getItems().add("Player: " + (Integer.toString(temp.playerlist.get(i).playerID)) + " | IsPlaying: " + isPlaying);
												}

											});},

											indiv->{
											Platform.runLater(()->{
												// updates GUI in results scene after receiving new game info from
												// server
												InfoPass temp = (InfoPass)indiv;
												playerId.setText(Integer.toString(temp.p1ID));
											});},

											res->{
											Platform.runLater(()->{
												InfoPass temp = (InfoPass) res;
												if (gameAccept.isDisabled() && gameDecline.isDisabled()) {  //person being challenged
													playerChallenge.setText("Player " + Integer.toString(temp.p1ID) + " has challenged you");
													gameAccept.setDisable(false);
													gameDecline.setDisable(false);
													listOfPlayers.setDisable(true);
												} else {
													InfoPass reply = new InfoPass();
													reply.p1ID = temp.p2ID;
													reply.p2ID = temp.p1ID;
													reply.accepted = false;
													reply.status = -2; //no match request
													clientConnection.send(reply);
												}

											});},

											reset->{  //allow players to challenge
											Platform.runLater(()->{
												listOfPlayers.setDisable(false);
												gameAccept.setDisable(true);
												gameDecline.setDisable(true);
											});},
											move->{ //move to scene of player choices
											Platform.runLater(()->{
												primaryStage.setScene(sceneMap.get("playfield"));
											});},
											move2->{
											Platform.runLater(()->{
												// updates GUI in results scene after receiving new game info from
												// server
												primaryStage.setScene(sceneMap.get("results"));
												InfoPass temp = (InfoPass)move2;
												int user = Integer.parseInt(playerId.getText());
												
												if(user == temp.p1ID)
												{														
													player_choice.setImage(new Image(temp.p1Play+".png"));
													opponent_choice.setImage(new Image(temp.p2Play+".png"));
													o.setText("Player " + Integer.toString(temp.p2ID) + " Chose:");
													String theWinner;
													if(temp.winner == temp.p1ID) {
														theWinner = "You";
													}
													else if(temp.winner == -1) {
														theWinner = "Draw";
													}
													else {
														theWinner = "Opponent";
													}
													listItems_results.getItems().add("You Played: " + temp.p1Play);
													listItems_results.getItems().add(String.valueOf(temp.p2ID) + " Played: " + temp.p2Play);
													listItems_results.getItems().add(theWinner + " Won!");
												}
												else
												{
													player_choice.setImage(new Image(temp.p2Play+".png"));
													opponent_choice.setImage(new Image(temp.p1Play+".png"));	
													o.setText("Player " + Integer.toString(temp.p1ID) + " Chose:");
													String theWinner;
													if(temp.winner == temp.p1ID) {
														theWinner = "Opponent";
													}
													else if(temp.winner == -1) {
														theWinner = "Draw";
													}
													else {
														theWinner = "You";
													}
													listItems_results.getItems().add("You Played: " + temp.p2Play);
													listItems_results.getItems().add(String.valueOf(temp.p1ID) + " Played: " + temp.p1Play);
													if(theWinner.equals("Draw")) {
														listItems_results.getItems().add("It's a draw.");
													}
													else {
														listItems_results.getItems().add(theWinner + " Won!");
													}
												}
												play_again.setDisable(false);
												quit_btn.setDisable(false);
											});},
											move3->{  //update GUI to menu scene
											Platform.runLater(()->{
												primaryStage.setScene(sceneMap.get("menu"));
												gameAccept.setDisable(true);
												gameDecline.setDisable(true);
												listOfPlayers.setDisable(false);
												playerChallenge.setText(" ");
												b1.setDisable(false);
												b2.setDisable(false);
												b3.setDisable(false);
												b4.setDisable(false);
												b5.setDisable(false);
											});},
											rc->{
												Platform.runLater(()->{
													listOfPlayers.setDisable(false);
													playerChallenge.setText(" ");
												});},

											ip_field.getText(), Integer.valueOf(port_field.getText()));
											clientConnection.start(); // starts client thread

		});


		
		//Event handler for proceeding to next match after a player has reached 3 points
		next = new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				// changes scence to selection scene and enables all buttons again
				primaryStage.setScene(sceneMap.get("playfield"));
				b1.setDisable(false);
				b2.setDisable(false);
				b3.setDisable(false);
				b4.setDisable(false);
				b5.setDisable(false);
				
				// clears the message box 
				listItems_results.getItems().clear();
				listItems_play.getItems().clear();
				listItems_results.getItems().add("Starting A New Match!");
				listItems_play.getItems().add("Starting A New Match!");
			}
		};
		

			
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) { 
            	if(clientConnection != null)
				{
					try {
						clientConnection.socketClient.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
            	Platform.exit();
				//System.exit(0);
            }
        });
		gameAccept.setOnAction(e->{
			primaryStage.setScene(sceneMap.get("playfield"));
			//clientConnection.send(); //Send information to corresponding client to change their scene to playfield because challenge was accepted
			InfoPass temp = new InfoPass();
			temp.p1ID = Integer.parseInt(playerId.getText());
			String string = playerChallenge.getText();
			int p2 = string.charAt(7) - '0';
			temp.p2ID = p2;
			temp.status = -2;
			temp.accepted = true;
			clientConnection.send(temp);
		});
		// turns off both game challenge buttons and sends decline message back to challenger client
		gameDecline.setOnAction(e->{
			//clientConnection.send(); //Send information to corresponding client to disable their buttons and change text to "Challenge Declined"
			InfoPass temp = new InfoPass();
			temp.p1ID = Integer.parseInt(playerId.getText());
			String string = playerChallenge.getText();
			int p2 = string.charAt(7) - '0';
			temp.p2ID = p2;
			temp.status = -2;
			temp.accepted = false;
			clientConnection.send(temp);

		});
		/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Set Scene ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
		
		primaryStage.setScene(sceneMap.get("init"));
		primaryStage.show();
		
	}
	
	/* Initial
	 * 
	 * This is the first scene that the user sees. Here the user gets to input the IP address and port that they 
	 * want to connect to 
	 */
	public Scene initial() {
		
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
		VBox port = new VBox(20, welc, ip_label, ip_field, port_label, port_field, connect_btn);
		
		BorderPane pane = new BorderPane();
		pane.setLeft(port); 
		pane.setCenter(logo);
		
		return new Scene(pane, 400, 300);	
	}

	/* playField
	 * 
	 * This is the primary scene where the user decides which hand he will play. Here the user can 
	 * also check what their own and their opponent's scores are for the current match
	 */
	public Scene playField() {
		
		// The way the user chooses a hand is by clicking on an image of the hand
		// that they want to play. Here, we assign buttons to each hand image as 
		// well as an interrupt to the clicking of each image
		// rock
		b1 = new Button();
		b1.setOnAction(sel1);
		b1.setGraphic(rock);
		
		// scissors
		b2 = new Button();
		b2.setOnAction(sel2);
		b2.setGraphic(scis);
		
		// paper
		b3 = new Button();
		b3.setOnAction(sel3);
		b3.setGraphic(pap);
		
		// lizard
		b4 = new Button();
		b4.setOnAction(sel4);
		b4.setGraphic(liz);
		
		// spock
		b5 = new Button();
		b5.setOnAction(sel5);
		b5.setGraphic(spoc);
		
		// all image choices are grouped together 
		HBox choices = new HBox(30, b1, b2, b3, b4, b5);

		BorderPane playfield = new BorderPane();
		playfield.setCenter(choices);
//		playfield.setBottom(gamefo);
		return new Scene(playfield, 628, 300);	
	}
	
	/* results
	 * 
	 * This scene is displayed to the user when the server receives input from both connected clients 
	 * and determines a winner. This scene displays what hand each player chose through the respective image.
	 * Scores and game messages are also displayed.
	 */
	public Scene results() {
		
		// Displaying the user's chosen hand as an image. Attaching a label so the user knows this is 
		// their hand
		pl = new Label("You Picked: ");
		VBox player = new VBox(10, pl, player_choice);
		
		// Displaying the opponent's chosen hand as an image. Attaching a label so the user knows this is 
		// their opponents hand
		o = new Label("Opponent Picked: ");
		VBox opponent = new VBox(10, o, opponent_choice);
		
		// This is an image of a "VS". For aesthetic purposes
		Image vs = new Image("vs.png");
		ImageView VS = new ImageView(vs);
		VS.setFitHeight(picHeight);
		VS.setFitWidth(picWidth);
		VS.setPreserveRatio(true);
		
		// grouping all images about all player choices together and adding padding
		// to try and center everything
		HBox choices = new HBox(50, player, VS, opponent);
		choices.setPadding(new Insets(0,100, 0, 100));
		
		// attaching event handler to the buttons that are displayed
		quit_btn.setOnAction(retur);
		play_again.setOnAction(again);
		
		// grouping the buttons together and adding padding to try to center everything
		HBox control_btns = new HBox(30, play_again, quit_btn);
		control_btns.setPadding(new Insets(0, 170, 0, 170));
		

		VBox gamefo = new VBox(10, control_btns, listItems_results);
		
		BorderPane results = new BorderPane();
		results.setCenter(choices);
		results.setBottom(gamefo);
		return new Scene(results, 638, 330);	
	}

	/* menu
	 *
	 *  This screen is displayed once the client enters a server, it will show all players connected
	 *	who is playing, who is not playing, and allowing you to click on who you would like to challenge
	 *  as well as offering the option to quit from the server rather than returning back to the game
	 */

	public Scene menu(){

		Label numPlayersText = new Label("Current Number of Players: ");
		numPlayersInt = new Label(Integer.toString(totalPlayers));
		HBox numPlayersBox = new HBox(numPlayersText, numPlayersInt);

		VBox leftMenu = new VBox(numPlayersBox, listOfPlayers);

		playerChallenge = new Label(" ");
		gameAccept = new Button("Accept");
		gameAccept.setDisable(true);


		gameDecline = new Button("Decline");
		gameDecline.setDisable(true);


		HBox buttonHolder = new HBox(10, gameAccept, gameDecline);

		VBox rightMenu = new VBox(10, playerChallenge, buttonHolder);

		Menu options = new Menu("Options"); //Button to quit from server
		MenuItem leave = new MenuItem("Quit");
		leave.setOnAction(quit);

		MenuBar menuBar = new MenuBar();
		options.getItems().add(leave);
		menuBar.getMenus().add(options);
		HBox menu = new HBox(10, leftMenu, rightMenu);

		Label player = new Label(" Player ");
		HBox playerInfo = new HBox(player ,playerId);

		VBox fullMenu = new VBox(10, menuBar, playerInfo, menu);

		return new Scene(fullMenu, 628, 300);
	}
}

