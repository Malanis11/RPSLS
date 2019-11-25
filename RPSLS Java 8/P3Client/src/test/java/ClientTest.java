import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class ClientTest {

	Client clientConnection;
	
	@Test
	void constr_test() {
	
		//clientConnection = new Client(data->{},res->{}/*, pl->{}*/, "127.0.0.1", 5555);
		
		assertEquals("Client", clientConnection.getClass().getName(), "Class Constructor Does Not Work");
	}
}
