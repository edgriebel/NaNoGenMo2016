package com.edgriebel;

import static org.junit.Assert.assertEquals;

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

    Formatter impl;
    
    @Before
    public void setupImpl() {
        impl = new Formatter();
    }

}
