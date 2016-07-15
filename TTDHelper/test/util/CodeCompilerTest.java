package util;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CodeCompilerTest {

    /**
     * Test of isCorrect method, of class CodeCompiler.
     * Tests if the compiler works without problems
     */
    @Test
    public void testIsCorrect() {
        System.out.println("isCorrect");
        Exercise ex = new Exercise();
        ex.addClass("Bam", Arrays.asList(new String[]{"public class Bam { \n }"}));
        String phase = "green";
        boolean expResult = true;
        boolean result = CodeCompiler.isCorrect(ex, phase);
        assertEquals(expResult, result);
    }
    
}
