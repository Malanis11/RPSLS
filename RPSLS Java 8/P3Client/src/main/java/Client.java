import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Client extends Thread{
	
	// connection our client gets connected to
	Socket socketClient;
	// IP address client wants to connect to
	String IP;
	// Port number client wants to connect to
	int Port;
	// stream out of client
	ObjectOutputStream out;
	// stream into client
	ObjectInputStream in;
	// object used to pass information to clients
	InfoPass info;
	// this consumers accept function is used to update the listview displaying messages in the 
	// server gui
	private Consumer<Serializable> callback;
	// this consumers accept function is used to update the gui of the playfield scene
	private Consumer<Serializable> update_play;
	// this consumers accept function is used to update the gui of the results scene
	private Consumer<Serializable> activate_buttons;
	private Consumer<Serializable> indiv;
	private Consumer<Serializable> reset;
	private Consumer<Serializable> move;
	private Consumer<Serializable> move2;
	private Consumer<Serializable> move3;
	private Consumer<Serializable> resetChoices;
	
	
	/* client
	 * 
	 * constructor for client class initializes class variables
	 */
	Client(Consumer<Serializable> call, Consumer<Serializable> individual, Consumer<Serializable> res, Consumer<Serializable> resetMenu, Consumer<Serializable> moveScene, Consumer<Serializable> moveScene2, Consumer<Serializable> moveScene3, Consumer<Serializable> rc, String ip, int port){
		info = new InfoPass();
		IP = ip;
		Port = port;
		callback = call;
		//update_play = play;
		activate_buttons = res;
		indiv = individual;
		reset = resetMenu;
		move = moveScene; //Changes Scenes to playfield
		move2 = moveScene2; //Changes Scenes to results
		move3 = moveScene3; //Changes Scenes to menu
		resetChoices = rc;
	}
	
	// run function for client class - continually checks for input from server and updates gui scenes as necessary
	public void run() {
		
		// open connections to the server
		try {
		socketClient= new Socket(IP, Port);
	    out = new ObjectOutputStream(socketClient.getOutputStream());
	    in = new ObjectInputStream(socketClient.getInputStream());
	    socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {
			callback.accept("Not Connected To The Server. Close The App And Try Again.");
		}
		
		// continually check for input from server
		while(true) {
			 
			try {
			// read in gameinfo object
			info = (InfoPass)in.readObject();
			// check the status field to determine action to perform
			switch(info.status) {
				case -7: 
						resetChoices.accept(info); //reset back to menu after quitting from a game
						break;
				case -6:
						move3.accept(info); //Moves to Menu Scene
						break;
				case -5:
						reset.accept(info); //Challenges Player
						break;
				case -4: //Self Identification
						indiv.accept(info);
						break;
				case -3:
						move.accept(info); //Moves to Playfield Scene
						break;
				case -2:
						activate_buttons.accept(info); //Match Request
						break;

				case -1:
						callback.accept(info); //Update all player infos
						break;
				case 2:
						move2.accept(info); //Move to Results Scene
						break;
			}
			}
			catch(Exception e) {
				//callback.accept("Not Connected To The Server. Close The App And Try Again.");
				break;
			}
		}
	
    }
	
	/* send
	 * 
	 * function is used to send information regarding player's hand choice to the server
	 * choice1 field is overwritten in gameinfo object and sent then the object is sent to the 
	 * server
	 */
	public void send(InfoPass data) {
		try {
			out.writeObject(data);
			out.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

//gameinfo class - used to send and recieve information between server and clients
class InfoPass implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int winner; // stores current rounds winner
	String p1Play, p2Play; // stores each players hand selection in string format
	ArrayList<AllPlayers> playerlist;
	boolean accepted; // stores whether a match is in progress. 1 - no; 0 - yes
	int status; // used to convey game status
	int p1ID, p2ID;
	/* status values:
	 *  0 - Server is waiting on one more player to join 
	 *  1 - Server is waiting on one more player to select a hand
	 *  2 - Server sent round results
	 *  3 - A sufficient number of players have joined and the game can begin 
	 */
}

class AllPlayers implements Serializable{
	private static final long serialVersionUID = 1L;
	boolean accepted;
	int playerID;
	int vs;
	boolean responseReceived;
	String selection;
}