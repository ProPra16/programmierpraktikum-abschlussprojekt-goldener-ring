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
    // soll Code des Users checken
    
    public static boolean isCorrect(String testClassName, String code, String phase){
        if(phase.equals("red")){
            CompilationUnit testCompiler = new CompilationUnit(testClassName, code, true);
            JavaStringCompiler currStringCompiler =  CompilerFactory.getCompiler(testCompiler);
            currStringCompiler.compileAndRunTests();
            if(currStringCompiler.getCompilerResult().hasCompileErrors() || 
                currStringCompiler.getTestResult().getNumberOfFailedTests() == 1){
                return true;
            }
        }
        if(phase.equals("green") || phase.equals("refactor")){
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
