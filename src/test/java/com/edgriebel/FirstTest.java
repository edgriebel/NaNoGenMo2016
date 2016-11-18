package com.edgriebel;

import static org.junit.Assert.*;
//import static org.hamcrest.Matchers.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import org.junit.*;



public class FirstTest {

    @Test
    public void testGenerate() throws Exception {
        Random r = new Random();
        File f = new File("./us-constitution.txt");
        List<String> s = impl.read(f);
        System.out.println("after read: " + s.size());
        
        Map<String, Map<Node, Node>> freqs = new TreeMap<>();
        impl.store(s, freqs);
        
        impl.setProbabilities(freqs);
        int size = 1000;
        
        List<String> words = new ArrayList<>(size);
        String word = getStartWord(freqs.keySet()).get();
        words.add(word);
        for (int i=1; i<size; i++) {
            float prob = r.nextFloat();
            Collection<Node> nodesForWord = freqs.get(word).values();
            word = getNextWord(word, nodesForWord, prob);
            words.add(word);
        }
        
        impl.formatText(words).stream().forEach(str -> System.out.println(str + "\n"));
        
        // System.out.println(formatText(words));
    }

    public String getNextWord(String word, Collection<Node> nodesForWord,
            float probability) {
        double sum = 0;
        for (Node n : nodesForWord) {
            sum += n.getFreq();
            if (probability < sum) {
                word = n.getFrom();
                break;
            }
        }
        return word;
    }
    
    
    private Optional<String> getStartWord(Set<String> words) {
        int c = (new Random()).nextInt(words.size());
        return words.stream().filter(word -> !word.matches(First.PUNCTUATION)).skip(c).findFirst();
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
