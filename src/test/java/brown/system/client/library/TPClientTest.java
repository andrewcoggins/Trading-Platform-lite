package brown.system.client.library;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import brown.communication.messages.library.ErrorMessage;
import brown.communication.messages.library.RegistrationMessage;
import brown.communication.messages.library.RegistrationResponseMessage;
import brown.system.kryoserver.library.KryoServer;
import brown.system.setup.ISetup;
import brown.system.setup.library.Setup;

/**
 * Tests of the TP Client object.
 */
public class TPClientTest {

  private class TestClient extends TPClient {
    public String error;

    private TestClient(String host, Integer port, ISetup setup) {

      super(host, port, setup);
      CLIENT.addListener(new Listener() {
        public void received(Connection connection, Object message) {
          if (message instanceof RegistrationResponseMessage) {
            onRegistrationResponse((RegistrationResponseMessage) message);
          } else if (message instanceof ErrorMessage) {
            error = ((ErrorMessage) message).getStatus();
            onStatusMessage((ErrorMessage) message);
          }
        }
      });
      CLIENT.sendTCP(new RegistrationMessage(-1, "testing"));
    }
  }

  private class TestServer extends KryoServer {
    public TestServer(int port, ISetup setup)
        throws ClassNotFoundException, IOException {
      super(port, setup);
      kryoServer.addListener(new Listener() {
        public void received(Connection connection, Object message) {
          if (message instanceof RegistrationMessage) {
            Integer theID = 1;
            connection.sendTCP(15000);
            connection.setTimeout(60000);
            kryoServer.sendToTCP(connection.getID(),
                new RegistrationResponseMessage(theID, theID, "testing"));
            kryoServer.sendToTCP(connection.getID(),
                new ErrorMessage(0, 0, "test error"));
          }
        }
      });
    }
  }

  @Test
  public void testConstructor() throws IOException, ClassNotFoundException {
    TPClient tpClient = new TestClient("localhost", 2121, new Setup());
    assertTrue(tpClient.CLIENT != null);
    assertTrue(tpClient.CLIENT instanceof Client);
    assertEquals(tpClient.ID, null);
  }

  @Test
  public void testRegistration()
      throws InterruptedException, IOException, ClassNotFoundException {
    TestServer testServer = new TestServer(2121, new Setup());
    Thread.sleep(1000);
    TestClient testClient = new TestClient("localhost", 2121, new Setup());
    Thread.sleep(1000);
    System.out.println(testClient.ID);
    assertTrue(testClient.ID == 1);
    assertEquals((testClient).error, "test error");
  }

  public static void main(String[] args)
      throws InterruptedException, IOException, ClassNotFoundException {
    TPClientTest clientTest = new TPClientTest();
    clientTest.testConstructor();
    clientTest.testRegistration();
  }
}
