import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientTest {
    Client client;
    String communicate;
    @Before
    public void setCommunicate() {
        communicate = "axdfa";
        client = new Client();
    }
    @Test
    public void checkCommunicate() throws Exception {
        assertTrue(client.checkCommunicate(communicate));
        assertFalse(client.checkCommunicate("xdfsdq00"));
        assertFalse(client.checkCommunicate("x"));
        assertFalse(client.checkCommunicate(""));
    }

    @Test
    public void checkNewbie() {
        assertEquals(client.ifImNew("t1b0t"), 'b');
        assertEquals(client.ifImNew("t2r0t"), 'r');
        assertNotEquals(client.ifImNew("abcda"), 'c');
    }

}