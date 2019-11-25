import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server{

	// number of clients currently connected to the server
	int count = 1;	
	// list clients currently connected 
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	ArrayList<AllPlayers> allplayers = new ArrayList<AllPlayers>();
	// thread that runs our server
	TheServer server;
	// port number currently monitoring
	int Port;
	// object used to pass information to clients
	InfoPass info;
	// counter monitoring how many clients have made selections for current round
	int count_recieved = 0;
	// this consumers accept function is used to update the listview displaying messages in the 
	// server gui
	private Consumer<Serializable> callback;
	
	/* server
	 * 
	 * constructor for server class initializes class variables and starts the server thread
	 */
	Server(Consumer<Serializable> call, int port)
	{
		Port = port;
		callback = call;
		info = new InfoPass();
		server = new TheServer();
		server.start();
	}
	
	// theServer class - runs our server thread where new clients are accepted put on their own threads
	public class TheServer extends Thread{
		
		ServerSocket mysocket;
		// run function for theServer thread - accepts new clients and puts them on their own thread
		public void run() {
		
			// opens socket connection
			try{
				mysocket = new ServerSocket(Port);
			// continually checks for new connections
		    while(true) {
				ClientThread c = new ClientThread(mysocket.accept(), count);
				//callback.accept("player has connected to server: " + "player #" + count);
				clients.add(c); // adds new clients to clients arraylist
		
				// add info on new player to AllPlayer arraylist
				AllPlayers newplayer = new AllPlayers();
				newplayer.playerID = count;
				newplayer.accepted = false;
				newplayer.responseReceived = false;
				newplayer.vs = 0;
				allplayers.add(newplayer);
				callback.accept(allplayers);
				c.start(); // starts accepted client's thread
				
				count++;
			    }
			}//end of try
				catch(Exception e) {
					//callback.accept("Server socket did not launch" + e);
				}
			}//end of while
		}
	

	// clientthread class - each new client gets put on its on thread 
	class ClientThread extends Thread{
		
		// connection to client
		Socket connection;
		// client number
		int count;
		// input stream from client
		ObjectInputStream in;
		// output stream to client
		ObjectOutputStream out;
		
		/* clientthread
		 * 
		 * constructor initializes every clientthread object with the new clients connection and 
		 * client number
		 */
		ClientThread(Socket s, int count){
			
			this.connection = s;
			this.count = count;	
		}
		
		/* updateAllClientStatus
		 *  
		 *  used to update all clients on current status of the game. Takes in an integer that signifies a
		 *  particular status: 
		 *  0 - Server is waiting on one more player to join 
		 *  1 - Server is waiting on one more player to select a hand
		 *  3 - A sufficient number of players have joined and the game can begin 
		 *  
		 *  status field in gameinfo object is overwritten and the object is then sent to clients
		 */
		public synchronized void updateAllClientStatus(int stat)
		{
			InfoPass temp = info;
			temp.status = stat;
			temp.playerlist = allplayers;
			
			for(int i = 0; i < clients.size(); i++) {
				ClientThread t = clients.get(i);
				try {
						t.out.writeObject(temp);
						t.out.reset();
				}
				catch(Exception e) {}
			}
		}
		
		public synchronized void updateSingleClientStatus(int stat, int clientID)
		{
			info.p1ID = clientID;
			info.playerlist = allplayers;
			info.status = stat;
			
			ClientThread t = clients.get(clientID - 1);
			try {
				t.out.writeObject(info);
				t.out.reset();
			}
			catch(Exception e) {}
		}
		
		/* updateClientScores
		 * 
		 * used to update all clients after a round. Uses the information stored in the info gameinfo object to 
		 * determine who won and how many points are distributed. The gameinfo object is updated and then sent back
		 * to the clients. Note: Every client that joins sees themselves as player 1 so once the details of the current
		 * state of the game are figured out, a gameinfo object with inverse details is sent to one of the clients
		 */
		public synchronized void updateClientScores(InfoPass choice)
		{
			info.p1ID = allplayers.get(choice.p1ID - 1).playerID;
			info.p2ID = allplayers.get(info.p1ID - 1).vs;
			info.p1Play = allplayers.get(info.p1ID - 1).selection;
			info.p2Play = allplayers.get(info.p2ID - 1).selection;
			
			if(info.p1Play.equals("Rock") && info.p2Play.equals("Scissors"))
				info.winner = info.p1ID;
			else if(info.p1Play.equals("Rock") && info.p2Play.equals("Lizard"))
				info.winner = info.p1ID;
			else if(info.p1Play.equals("Scissors") && info.p2Play.equals("Paper"))
				info.winner = info.p1ID;
			else if(info.p1Play.equals("Scissors") && info.p2Play.equals("Lizard"))
				info.winner = info.p1ID;
			else if(info.p1Play.equals("Paper") && info.p2Play.equals("Rock"))
				info.winner = info.p1ID;
			else if(info.p1Play.equals("Paper") && info.p2Play.equals("Spock"))
				info.winner = info.p1ID;
			else if(info.p1Play.equals("Lizard") && info.p2Play.equals("Paper"))
				info.winner = info.p1ID;
			else if(info.p1Play.equals("Lizard") && info.p2Play.equals("Spock"))
				info.winner = info.p1ID;
			else if(info.p1Play.equals("Spock") && info.p2Play.equals("Scissors"))
				info.winner = info.p1ID;
			else if(info.p1Play.equals("Spock") && info.p2Play.equals("Rock"))
				info.winner = info.p1ID;
			else if(info.p1Play.equals(info.p2Play))
				info.winner = -1;
			else 
				info.winner = info.p2ID;
			
			info.status = 2;
			ClientThread t = clients.get(info.p1ID - 1);
			ClientThread u = clients.get(info.p2ID - 1);
			try {
					t.out.writeObject(info);
					t.out.reset();
					u.out.writeObject(info);
					u.out.reset();
			}
			catch(Exception e) {}
			
			allplayers.get(info.p1ID - 1).responseReceived = false;
			allplayers.get(info.p2ID - 1).responseReceived = false;

		}
		
		// run function for clientthread class - each client thread is continually polled for input
		public void run(){
				
			// open streams to and from client
			try {
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);	
			}
			catch(Exception e) {}
				
			updateSingleClientStatus(-4, count);
			updateAllClientStatus(-1);
			 while(true) {
				    try {
				    	InfoPass choice = (InfoPass)in.readObject(); // read in from object

				    	// status == -2 indicates a match request
				    	if(choice.status == -2)
				    	{
				    		boolean pendingRequest = false;
				    		
				    		
							for(int i = 0; i < allplayers.size(); i++){
								if(allplayers.get(i).vs == choice.p2ID){
									pendingRequest = true;
									break;
								}
							}

							if(!pendingRequest)
							{
					    		allplayers.get(choice.p1ID - 1).accepted = choice.accepted;
					    		allplayers.get(choice.p1ID - 1).vs = choice.p2ID;
					    		
					    		// if the person who you are trying to match with is not matched with anyone send a match request
					    		if(allplayers.get(choice.p2ID - 1).vs == 0)
					    		{
					    			ClientThread u = clients.get(choice.p2ID - 1);
					    			try {
					    					u.out.writeObject(choice);
					    					u.out.reset();
					    			}
					    			catch(Exception e) {}
					    		}
					    		// if both players agree to play start match
					    		else if(allplayers.get(choice.p2ID - 1).vs == allplayers.get(choice.p1ID - 1).playerID && allplayers.get(choice.p1ID - 1).vs == allplayers.get(choice.p2ID - 1).playerID && allplayers.get(choice.p2ID - 1).accepted && allplayers.get(choice.p1ID - 1).accepted)
					    		{
					    			callback.accept(allplayers);
					    			choice.status = -3;
					    			updateAllClientStatus(-1);
					    			ClientThread t = clients.get(choice.p1ID - 1);
									ClientThread u = clients.get(choice.p2ID - 1);
									try {
										t.out.writeObject(choice);
										t.out.reset();
										u.out.writeObject(choice);
										u.out.reset();
									}
									catch(Exception e) {}
					    		}
					    		// reset if players dont agree 
					    		else
					    		{
					    			allplayers.get(choice.p1ID - 1).accepted = false;
						    		allplayers.get(choice.p1ID - 1).vs = 0;
						    		allplayers.get(choice.p2ID - 1).accepted = false;
						    		allplayers.get(choice.p2ID - 1).vs = 0;
					    			choice.status = -5;
					    			ClientThread t = clients.get(choice.p1ID-1);
					    			ClientThread u = clients.get(choice.p2ID-1);
										try{
											t.out.writeObject(choice);
											t.out.reset();
											u.out.writeObject(choice);
											u.out.reset();
										}
										catch(Exception e) {}
								}
	
					    	}
							else
							{
								choice.status = -7;
								ClientThread u = clients.get(choice.p1ID-1);
								try{
									u.out.writeObject(choice);
									u.out.reset();
								}
								catch(Exception e) {}
							}
				    	}
				    	else if(choice.status == -3)
				    	{
				    		allplayers.get(choice.p1ID - 1).responseReceived = true;
				    		allplayers.get(choice.p1ID - 1).selection = choice.p1Play;
							callback.accept(allplayers);
				    		int check_opponent_response = allplayers.get(choice.p1ID - 1).vs - 1;
				    		if(allplayers.get(check_opponent_response).responseReceived)
				    		{
				    			updateClientScores(choice);
				    		}
				    		
				    	}
				    	else if(choice.status == -6){
				    		int vs = allplayers.get(choice.p1ID - 1).vs;
				    		allplayers.get(vs - 1).vs = 0;
				    		allplayers.get(vs - 1).accepted = false;
							ClientThread t = clients.get(vs - 1);
							try{
								t.out.writeObject(choice);
								t.out.reset();
							}
							catch(Exception e) {}

				    		allplayers.get(choice.p1ID-1).vs = 0;
							allplayers.get(choice.p1ID-1).accepted = false;
							updateAllClientStatus(-1);
							callback.accept(allplayers);
						}
					}
				    catch(Exception e) { // this exception is entered when a client closes down
				    	clients.remove(this);
				    	allplayers.remove(count - 1);
				    	for(int i = 0; i < allplayers.size(); i++)
				    	{
				    		allplayers.get(i).playerID = i + 1;
				    	}
				    	for(int i = 0; i < allplayers.size(); i++)
				    	{
				    		updateSingleClientStatus(-4, i+1);
				    	}
				    	callback.accept(allplayers);
				    	// let remaining client that we need another client to join to proceed playing
				    	updateAllClientStatus(-1);
				    	// update server gui that no match is in progress
				    	break;
				    }
				}
			}//end of run
	}//end of client thread
}

// gameinfo class - used to send and recieve information between server and clients
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
	 *
	 *  -3 - match accepted - start new match
	 *  -2 - match request
	 *  -1 - new player joined
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

	
	

	
