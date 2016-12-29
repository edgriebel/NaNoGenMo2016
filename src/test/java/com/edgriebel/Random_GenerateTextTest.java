package com.edgriebel;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Random_GenerateTextTest {
    static Reader reader = null;
    static List<String> fileText = null;
    static final String readFileName = 
            "docs/Bible.txt";
//        "docs/KerouacJack-OnTheRoad_djvu.txt";

    @BeforeClass
    public static void getText() throws Exception {
        reader = new Reader();
        File readFile = new File(readFileName);
        fileText = reader.read(readFile);
        System.out.println("after read of file " + readFileName + ": " + fileText.size());
    }

    @Test
    public void testGenerateAndFormat() throws Exception {
//        impl.allLowerCase = true;
        IMarkov markov = new MarkovRandom();
        Formatter f = new Formatter();
        
        String out = impl.generateAndFormatText(1000, fileText, markov, f);
        
        System.out.println(out);
    }
    
    GenerateText impl;
    
    @Before
    public void setupImpl() 
    {
        impl = new GenerateText();
    }
    
}
