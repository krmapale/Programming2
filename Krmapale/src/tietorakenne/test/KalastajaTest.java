package tietorakenne.test;
// Generated by ComTest BEGIN
import static org.junit.Assert.*;
import org.junit.*;
import tietorakenne.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2016.04.08 11:11:30 // Generated by ComTest
 *
 */
@SuppressWarnings("all")
public class KalastajaTest {



  // Generated by ComTest BEGIN
  /** testAsetaID26 */
  @Test
  public void testAsetaID26() {    // Kalastaja: 26
    Kalastaja kalastaja1 = new Kalastaja(); 
    kalastaja1.luoArvot(); 
    assertEquals("From: Kalastaja line: 29", 1, kalastaja1.getID()); 
    Kalastaja kalastaja2 = new Kalastaja(); 
    kalastaja2.luoArvot(); 
    assertEquals("From: Kalastaja line: 32", 2, kalastaja2.getID()); 
    Kalastaja kalastaja3 = new Kalastaja(); 
    kalastaja3.luoArvot(); 
    assertEquals("From: Kalastaja line: 35", 3, kalastaja3.getID()); 
  } // Generated by ComTest END
}