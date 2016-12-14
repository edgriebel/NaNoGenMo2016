package com.edgriebel;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

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
        MarkovBigram markov = new MarkovBigram();
        Formatter f = new Formatter();
        
        String out = impl.generateAndFormatText(1000, fileText, markov, f);
        
        System.out.println(out);
    }
    
    @Test
    public void testFormatText_Small() {
        List<String> smallText = Arrays.asList("This is a small list".split(" "));
        
        String out = genericFormatter.formatText(smallText);
        System.out.printf("<START>%s<END>\n", out);
        assertTrue("Should be capital at start", Character.isUpperCase(out.charAt(0)));
        assertEquals("Should be period at end", '.', out.charAt(out.length()-1));
    }
    
    @Test
    public void testWrapText_Small() {
        String smallText = "This is a small list";
        
        String out = genericFormatter.wrapText(smallText);
        System.out.printf("<START>%s<END>\n", out);
        assertEquals("Should only be one line", 0, out.replaceAll("[^\\n]+","").length());
    }
    

    @Test
    public void testWrapText_TwoLines() {
        String stub = "word ";
        String text = IntStream.rangeClosed(1, Math.round(Formatter.linelength * 1.5F / stub.length())).mapToObj(i -> stub).reduce("", (x,y) -> x += y);
        
        String out = genericFormatter.wrapText(text);
        System.out.printf("<START>%s<END>\n", out);
        assertEquals("Should be two lines", 1, out.replaceAll("[^\\n]+","").length());
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
    Formatter genericFormatter;
    
    @Before
    public void setupImpl() 
    {
        impl = new GenerateText();
        genericFormatter = new Formatter();
    }
    
}
