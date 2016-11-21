package com.edgriebel;

import static org.junit.Assert.*;
//import static org.hamcrest.Matchers.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.IntStream;

import org.junit.*;



public class FirstTest {

    @Test
    public void testGenerateOld() throws Exception {
        File f = new File("./Bible.txt");
        List<String> s = impl.read(f);
        System.out.println("after read: " + s.size());
        
        Map<String, Map<Node, Node>> freqs = new TreeMap<>();
        
        impl.store(s, freqs);
        
        int size = 1000;
        
        List<String> words = impl.generateText(freqs, size);
        
        impl.formatTextOld(words).stream().forEach(str -> System.out.println(str + "\n"));
    }

    @Test
    public void testGenerate() throws Exception {
        File f = new File("./Bible.txt");
        List<String> s = impl.read(f);
        System.out.println("after read: " + s.size());
        
        Map<String, Map<Node, Node>> freqs = new TreeMap<>();
        
        impl.store(s, freqs);
        
        int size = 400;
        
        List<String> words = impl.generateText(freqs, size);
        System.out.println("OLD");
        impl.formatTextOld(words).stream().forEach(str -> System.out.println(str + "\n"));

        System.out.println("New");
        List<List<String>> sections = impl.splitIntoSections(words);
        String newStr = sections.stream().map(s1 -> impl.formatText(s1)).map(s2 -> impl.wrapText(s2)).reduce("", (x, y) -> x += y + "\n\n");
        System.out.println(newStr);
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
    public void testFormatText() {
        List<String> toTest = Arrays.asList( new String [] {
                "hello", "world", "."
        });
        List<String> l = new ArrayList<>(toTest);
        
        String out = impl.formatText(l);
        System.out.println("Out: " + out);
        assertEquals(out, toTest.stream().reduce("", (r,s) -> r += (r.equals("") ? impl.capword(s) : ( s.matches(First.PUNCTUATION) ? s : " " + s) )) );
    }
    
    @Test
    public void testStore_Small() throws Exception {
        List<String> s = Arrays.asList(
                "Hello there".split("\\s")
                );
        
        
        Map<String, Map<Node, Node>> freqs = new TreeMap<>();
        impl.store(s, freqs );
        System.out.println(freqs.keySet());
    }
    
    @Test
    public void testStore_Luke() throws Exception {
        List<String> strings = impl.read(new File("bible-luke.txt"));
        Map<String, Map<Node, Node>> freqs = new TreeMap<>();
        impl.store(strings, freqs );
        System.out.println(freqs);
    }
    
    @Test
    public void testRead_shorttxt() throws Exception {
        File f = new File("./short.txt");
        List<String> s = impl.read(f);
        System.out.println(s);
//        assertThat(s, contains("."));
    }

    @Test
    public void testRead_commas() throws Exception {
        final String str = "hello, world!";
        List<String> s = impl.read(new Scanner(str));
        System.out.println(s);
        assertTrue(s.stream().anyMatch( x -> ",".equals(x))); 
        assertTrue(s.stream().anyMatch( x -> str.substring(str.length()-1).equals(x))); 
    }

    @Test
    public void testRead_bible_luke_txt() throws Exception {
        System.out.println(new File(".").getCanonicalPath());
        File f = new File("./bible-luke.txt");
        List<String> s = impl.read(f);
        System.out.println(s);
    }
    
    @Test
    public void testSetProbability() throws Exception {
        List<Node> c = new ArrayList<>();
        Node n = new Node("half");
        n.setCount(1);
        c.add(n);
        First.setProbability(c);
        assertEquals("Should be 1.0 with a single entry", 1.0d, c.get(0).getFreq(), 0.001);
        c.add(n);
        First.setProbability(c);
        System.out.println(c);
        assertEquals("Should be 0.5 with two equal counts", 0.5d, c.get(0).getFreq(), 0.001d);
    }
    
    @Test
    public void testSetProbabilities() throws Exception {
        Map<String, Map<Node, Node>> nodes = new HashMap<>();
        
//        nodes.put(key, value);
    }

    First impl;
    
    @Before
    public void setupImpl() {
        impl = new First();
    }
}
