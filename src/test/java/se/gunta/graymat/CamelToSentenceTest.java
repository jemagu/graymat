package se.gunta.graymat;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Magnus Gunnarsson
 */
public class CamelToSentenceTest {

    /**
     * Test of doit method, of class CamelToSentence.
     */
    @Test
    public void testDoit() {
        assertEquals(CamelToSentence.doit("saveTableOnExit"), "save table on exit");
        assertEquals(CamelToSentence.doit("aMonkeyBall"), "a monkey ball");
        assertEquals(CamelToSentence.doit("callIBMPersonal"), "call ibm personal");
        assertEquals(CamelToSentence.doit("sayILoveYou"), "say i love you");
        assertEquals(CamelToSentence.doit("getX"), "get x");
    }    
}
