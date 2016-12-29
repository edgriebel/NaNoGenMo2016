package com.edgriebel;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
    }

    @Test
    public void testGenerateAndFormat() throws Exception {
//        impl.allLowerCase = true;
        IMarkov markov = new MarkovBigram();
        Formatter f = new Formatter();
        
        String out = impl.generateAndFormatText(1000, fileText, markov, f);
        
        System.out.println(out);
    }
    

    /*
    @Test
    public void testGenerate_OldAndNew() throws Exception {
        impl.store(fileText);
        
        int size = 400;
        
        List<String> words = impl.generateText(size);
        System.out.println("Monolithic Generator");
        formatter.formatTextOld(words).stream().forEach(str -> System.out.println(str + "\n"));
    
        System.out.println("New Generator");
        List<List<String>> sections = formatter.splitIntoSections(words);
        String newStr = sections.stream().map(s1 -> formatter.formatText(s1)).map(s2 -> formatter.wrapText(s2)).reduce("", (x, y) -> x += y + "\n\n");
        System.out.println(newStr);
    }

    @Test
    public void testGenerateOld() throws Exception {
        impl.store(fileText);
        
        int size = 1000;
        
        List<String> words = impl.generateText(size);
        
        formatter.formatTextOld(words).stream().forEach(str -> System.out.println(str + "\n"));
    }
    */

    GenerateText impl;
    
    @Before
    public void setupImpl() 
    {
        impl = new GenerateText();
    }
    
}
