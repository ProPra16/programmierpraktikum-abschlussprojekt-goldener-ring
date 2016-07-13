package util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *y
 * @author RussiaFiT
 */
public class CodeCompilerTest {
    
    /*
        The following three tests, test if the code breaks if one of the provided
        statements is null.
    */
    
    @Test
    public void testNameNull() {
        System.out.println("testNameNull");
        String testClassName = null;
        String code = "";
        String phase = "";
        boolean expResult = false;
        boolean result = CodeCompiler.isCorrect(testClassName, code, phase);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testCodeNull() {
        System.out.println("testCodeNull");
        String testClassName = "";
        String code = null;
        String phase = "";
        boolean expResult = false;
        boolean result = CodeCompiler.isCorrect(testClassName, code, phase);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testPhaseNull() {
        System.out.println("testPhaseNull");
        String testClassName = "";
        String code = "";
        String phase = null;
        boolean expResult = false;
        boolean result = CodeCompiler.isCorrect(testClassName, code, phase);
        assertEquals(expResult, result);
    }
    
    /*
        The following two tests, test if the method checks the given Code properly.
        P.S. tried to create a custom String for "code" to check if test is true, but
        can't figure out how to get it working. Method itself does work with proper test
        while running the program.
    */
    
    @Test
    public void testCodeFalse() {
        System.out.println("testCodeFalse");
        String testClassName = "";
        String code = "This is obviously not a proper JUnit-test-code.";
        String phase = "";
        boolean expResult = false;
        boolean result = CodeCompiler.isCorrect(testClassName, code, phase);
        assertEquals(expResult, result);
    }
    /*
    @Test
    public void testCodeTrue() {
        System.out.println();
        String testClassName = "";
        String code = "Write proper test Code here.";
        String phase = "";
        boolean expResult = true;
        boolean result = CodeCompiler.isCorrect(testClassName, code, phase);
        assertEquals(expResult, result);
    }
    */
}
