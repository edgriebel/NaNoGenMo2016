package com.edgriebel;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.util.List;

import org.junit.*;

public class GenerateTextTest {
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
        assertThat("some text should have been read in", fileText, allOf(notNullValue(), not(empty())));
    }

    @Test
    public void testGenerateAndFormat() throws Exception {
//        impl.allLowerCase = true;
        IMarkov markov = new MarkovBigram();
        Formatter f = new Formatter();
        
        String out = impl.generateAndFormatText(1000, fileText, markov, f);
        
        assertThat(out, not(emptyOrNullString()));
        
        System.out.println(out);
    }

    GenerateText impl;
    
    @Before
    public void setupImpl() 
    {
        impl = new GenerateText();
    }
    
}
