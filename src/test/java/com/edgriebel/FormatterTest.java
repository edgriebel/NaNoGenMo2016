package com.edgriebel;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

public class FormatterTest {

    @Test
    public void testFormatText() {
        List<String> toTest = Arrays.asList( new String [] {
                "hello", "world", "."
        });
        List<String> l = new ArrayList<>(toTest);
        
        String out = impl.formatText(l);
        System.out.println("Out: " + out);
        assertEquals(out, toTest.stream().reduce("", (r,s) -> r += (r.equals("") ? Formatter.capword(s) : ( s.matches(Formatter.PUNCTUATION) ? s : " " + s) )) );
    }

    @Test
    public void testWrapText() {
        String stub = "0123456789";
        String str = IntStream.rangeClosed(1, 10).mapToObj(i -> stub).reduce("", (x,y) -> x += y + "\n");
        System.out.println("String size: " + str.length());
        
        String out = impl.wrapText(str);
        System.out.println(out);
        for (String s : out.split("\n")) {
            System.out.printf("%3d: %s\n", s.length(), s);
        }
    }

    @Test
    public void testWrapText_TwoLines() {
        String stub = "word ";
        String text = IntStream.rangeClosed(1, Math.round(Formatter.linelength * 1.5F / stub.length())).mapToObj(i -> stub).reduce("", (x,y) -> x += y);
        
        String out = impl.wrapText(text);
        System.out.printf("<START>%s<END>\n", out);
        assertEquals("Should be two lines", 1, out.replaceAll("[^\\n]+","").length());
    }

    @Test
    public void testFormatText_Small() {
        List<String> smallText = Arrays.asList("This is a small list".split(" "));
        
        String out = impl.formatText(smallText);
        System.out.printf("<START>%s<END>\n", out);
        assertTrue("Should be capital at start", Character.isUpperCase(out.charAt(0)));
        assertEquals("Should be period at end", '.', out.charAt(out.length()-1));
    }
    
    @Test
    public void testWrapText_Small() {
        String smallText = "This is a small list";
        
        String out = impl.wrapText(smallText);
        System.out.printf("<START>%s<END>\n", out);
        assertEquals("Should only be one line", 0, out.replaceAll("[^\\n]+","").length());
    }
    

    Formatter impl;
    
    @Before
    public void setupImpl() {
        impl = new Formatter();
    }

}
