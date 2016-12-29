package com.edgriebel;

import static org.junit.Assert.*;
//import static org.hamcrest.Matchers.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.*;

public class MarkovBigramTest {

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
    public void testStore_Small() throws Exception {
        List<String> s = Arrays.asList(
                "Hello there".split("\\s")
                );
        
        impl.store(s);
        System.out.println(impl.wordFrequencies.keySet());
    }
    
    @Test
    public void testStore_Luke() throws Exception {
        Map<String, Map<Node, Node>> freqs = new TreeMap<>();
        impl.store(fileText);
        freqs.entrySet().stream().limit(100).forEach(System.out::println);
    }
    
    @Test
    public void testSetProbability() throws Exception {
        List<Node> c = new ArrayList<>();
        Node n = new Node("half");
        n.setCount(1);
        c.add(n);
        impl.setProbability(c);
        assertEquals("Should be 1.0 with a single entry", 1.0d, c.get(0).getFreq(), 0.001);
        c.add(n);
        impl.setProbability(c);
        System.out.println(c);
        assertEquals("Should be 0.5 with two equal counts", 0.5d, c.get(0).getFreq(), 0.001d);
    }
    
    @Test
    public void testSetProbabilities() throws Exception {
        Map<String, Map<Node, Node>> nodes = new HashMap<>();
        
//        nodes.put(key, value);
    }

    MarkovBigram impl;
    Formatter formatter;

    @Before
    public void setupImpl() {
        impl = new MarkovBigram();
        formatter = new Formatter();
    }
}
