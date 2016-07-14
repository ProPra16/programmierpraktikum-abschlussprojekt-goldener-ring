/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import vk.core.api.CompilationUnit;
import vk.core.api.CompilerFactory;
import vk.core.api.JavaStringCompiler;

/**
 *
 * @author Lars
 */
public class CodeCompiler {
     /**
     * Tests user written test and checks if test matches the requirements of the current phase. 
     * 
     * @param testClassName Name of current test.
     * @param code Code of currently used test.
     * @param phase Name of phase, the user is currently in. 
     * @return boolean True if the requierements to change Phase are met.
     */
    
    public static boolean isCorrect(String testClassName, String code, String phase){
        if(phase!= null && phase.equals("red")){
            CompilationUnit testCompiler = new CompilationUnit(testClassName, code, true);
            JavaStringCompiler currStringCompiler =  CompilerFactory.getCompiler(testCompiler);
            currStringCompiler.compileAndRunTests();
            if(currStringCompiler.getCompilerResult().hasCompileErrors() || 
                currStringCompiler.getTestResult().getNumberOfFailedTests() == 1){
                return true;
            }
        }
        if(phase!= null && (phase.equals("green") || phase.equals("refactor"))){
            CompilationUnit testCompiler = new CompilationUnit(testClassName, code, true);
            JavaStringCompiler currStringCompiler = CompilerFactory.getCompiler(testCompiler);
            currStringCompiler.compileAndRunTests();
            if(!currStringCompiler.getCompilerResult().hasCompileErrors()){
                if (currStringCompiler.getTestResult().getNumberOfFailedTests() == 0){
                    return true;
                }
            }
        }
        
        
        
        return false;
    }
}
