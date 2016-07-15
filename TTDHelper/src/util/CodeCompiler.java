package util;

import java.util.ArrayList;
import java.util.List;
import vk.core.api.CompilationUnit;
import vk.core.api.CompilerFactory;
import vk.core.api.JavaStringCompiler;

/**
 * Compiles the user provided Code and checks it for errors.
 */
public class CodeCompiler {
     /**
     * Tests user written test and checks if test matches the requirements of the current phase. 
     * 
     * @param ex The exercise that needs testing
     * @param phase Name of phase, the user is currently in. 
     * @return boolean True if the requierements to change Phase are met.
     */
    
    public static boolean isCorrect(Exercise ex, String phase){
        List<CompilationUnit> units = new ArrayList();
        for(int i=0; i<ex.getAvailableTestsCount();i++)
        {
            units.add(new CompilationUnit(ex.getAvailableTestName(i), 
                    ex.getAvailableTestCode(i), true));
        }
        units.add(new CompilationUnit(ex.getClassName(),
                    ex.getClassCode(), false));
        JavaStringCompiler currStringCompiler =  CompilerFactory.getCompiler(
                units.stream().toArray(CompilationUnit[]::new));
        System.out.println(units.size());
        units.stream().forEach((cu) -> {
            System.out.println(cu.getClassName());
        });
        
        currStringCompiler.compileAndRunTests();
        
        if(phase != null && phase.equals("red")){
            if(currStringCompiler.getCompilerResult().hasCompileErrors() || 
                    currStringCompiler.getTestResult().getNumberOfFailedTests() == 1){
                return true;
            }
        }
        if(phase != null && (phase.equals("green") || phase.equals("refactor"))){
            if(!currStringCompiler.getCompilerResult().hasCompileErrors())
                if(currStringCompiler.getTestResult().getNumberOfFailedTests() == 0)
                    return true;
        }
        return false;
    }
}