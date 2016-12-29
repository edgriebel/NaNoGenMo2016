package com.edgriebel;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        System.out.println(((MarkovBigram)impl).wordFrequencies.keySet());
    }
    
    @Test
    public void testStore_FourWords() throws Exception {
        List<String> s = Arrays.asList(
                "One Two Three Four".split("\\s")
                );
        
        impl.store(s);
        assertThat(((MarkovBigram)impl).wordFrequencies.keySet(), hasSize(3));
        assertThat(((MarkovBigram)impl).wordFrequencies.keySet(), 
                containsInAnyOrder(s.stream()
                        .limit(3) // we know only the first 3 will be in the set
                        .map(String::toLowerCase)
                        .collect(Collectors.toList()).toArray())
                );

        System.out.println(((MarkovBigram)impl).wordFrequencies.keySet());
        
    }
    
    @Test
    public void testFindProperNouns_Small() throws Exception {
        List<String> s = Arrays.asList(
                "Hello World".split("\\s")
                );
        List<String> s_toLowerCase = s.stream().map(String::toLowerCase).collect(Collectors.toList());
        
        assertThat(((MarkovBigram)impl).findProperNouns(s), allOf(hasSize(2), containsInAnyOrder(s_toLowerCase.toArray())));
        
        assertThat("no uppercase words should be found in set of lowercase words", ((MarkovBigram)impl).findProperNouns(s_toLowerCase), hasSize(0));
        
        // now check that method embedded in store() works
        impl.store(s);
        assertThat(((MarkovBigram)impl).properNouns, allOf(hasSize(2), containsInAnyOrder(s_toLowerCase.toArray())));
    }
    
    @Test
    public void testStore_FullFile() throws Exception {
        impl.store(fileText);
        
        assertThat(((MarkovBigram)impl).wordFrequencies.entrySet(), allOf(notNullValue(), not(empty())));
        ((MarkovBigram)impl).wordFrequencies.entrySet().stream().limit(20).forEach(System.out::println);
    }
    
    @Test
    public void testSetProbability() throws Exception {
        List<Node> c = new ArrayList<>();
        Node n = new Node("half");
        n.setCount(1);
        c.add(n);
        impl.setProbability(c);
        assertThat("Should be 1.0 with a single entry", c.get(0).getFreq(), closeTo(1.0, 0.001));
        c.add(n);
        impl.setProbability(c);
        System.out.println(c);
        assertThat("Should be 0.5 with two equal counts", c.get(0).getFreq(), closeTo(0.5d, 0.001d));
    }
    
    @Test
    public void testSetProbabilities() throws Exception {
        Map<String, Map<Node, Node>> nodes = new HashMap<>();
        
//        nodes.put(key, value);
    }

    IMarkov impl;
    Formatter formatter;

    @Before
    public void setupImpl() {
        impl = new MarkovBigram();
        formatter = new Formatter();
    }
}
