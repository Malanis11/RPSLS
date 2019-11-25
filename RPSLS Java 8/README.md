# CS-342---Project-4

Description: 
You will implement the game Rock, Paper, Scissors, Lizard, Spock. This is just an augmented version of the traditional Rock, Paper, Scissors game. Your implementation will be a two player game where each player is a separate client and the game is run by a server. Your server and clients will use the same machine; with the server choosing a port on the local host and clients knowing the local host and port number (just as was demonstrated in class). Each round of the game will be worth one point. Games will be played until one of the players has three points. At the end of each game, each user will be able to play again or quit. All networking must be done utilizing Java Sockets (as shown in class). The server must  run on its own thread and handle each client on a separate thread. You may not use libraries or classes not included in the standard Java 8 release. You may not alter the POM ﬁle. You may work in teams of two but do not have to. Implementation Details: You will create separate programs, each with a GUI created in JavaFX,  for the server and the client. 

For the server GUI: 
- A way to chose the port number to listen to 
- Have a button to turn on the server. 
- Display the state of the game(you can display more info, this is the minimum): 
    - how many clients are connected to the server. 
    - what each player played. 
    - how many points each player has. 
    - if someone won the game. - are they playing again. 
- Any other GUI elements you feel are necessary for your implementation.
    
Notes: Your server GUI must have a minimum of two scenes: an intro screen that allows the user to input the port number and start the server and another that will display the state of the game information. To display the game information, you must incorporate a listView (as seen in class) with any other widgets used. Keep in mind, you can dynamically add items to the listView without using an ArrayList.
    
For the server logic: 
- It will only allow a game to start if there are two clients connected. 
- It will notify a client if they are the only one connected. 
- It will keep track of what each player played. 
- It will evaluate who won each hand. 
- It will evaluate if a client has won the game. 
- It will update each client with the above items in time. 
- It will do all things necessary to run the game. 

It is expected that your server code will open, manage and close all resources needed and handle all exceptions in a graceful way. For game play, each client will chose to play either Rock, Paper, Scissors, Lizard or Spock and send that choice to the server. The server will determine who won and then update each client with what the other played and the resulting state of the game. If a client has won the hand and has reached three points, the server will send what the other player played, the resulting state of the game and require each client to make a choice as to play again or quit. If a player quits, the server will end that connection. If one player quits and the other wants to play again, the server will notify the client that they must wait for another person to connect. If both want to play again, the server will start a new game. 

For the client GUI: 
- A way for the user to enter the port number and ip address to connect to 
- A button to connect to the server. 
- A way to display the points each player has. 
- A way to display what the opponent played each round using images. 
- Clickable images to choose what to play. 
- A way to display messages from the server. 
- Buttons to choose to play again or quit. 
- Any other GUI elements you feel are necessary for your implementation. 

Notes: Your client GUI must have a minimum of three scenes: an intro screen that allows the user to input the port number and ip address to connect to the server,  another that will display the state of the game information and game play and a third of your choice. To display the game information, you must incorporate a listView (as seen in class) with any other widgets used. Keep in mind, you can dynamically add items to the listView without using an ArrayList. Buttons should be disabled when not in use or when waiting for a response from the server. 

For the client logic: 

After entering the port number and ip address, the user will click to connect to the server. When there is another client to play, the game will start. The user will select which item to play and send to the server. The server will respond with what the opponent played, who won and what points were distributed. The client GUI will update with this information and allow the user to either keep playing or, if someone has won, chose to either play again or quit. Quit will end the client program. It is expected that your client code will open, manage and close all resources needed and handle all exceptions in a graceful way. 
