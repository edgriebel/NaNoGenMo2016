package com.edgriebel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class MarkovRandom extends AbstractMarkov 
{
    // protected so it can be used in unit tests
    protected Map<String, Map<Node, Node>> wordFrequencies;
    protected Map<String, Integer> wordCounts;
    
    public MarkovRandom() {
        wordFrequencies = new HashMap<>();
        wordCounts = new HashMap<>();
    }
    
    @Override
    public void store(List<String> l) {
        if (l.isEmpty() || l.size() == 1)
            return;
        
        l.forEach(word -> wordFrequencies.put(word.toLowerCase(), Collections.emptyMap()));
        l.forEach(word -> wordCounts.merge(word, 1, Integer::sum));
        
        properNouns = findProperNouns(l);
        
        assert(wordFrequencies.keySet().stream().noneMatch(s -> Character.isUpperCase(s.charAt(0))));
        
        System.err.println("Size of capwords: " + properNouns.size() + " First 10: " + properNouns.stream().limit(10).collect(Collectors.toList()));
    }

    @Override
    public void setProbability(Collection<? extends Node> nodes) {
        // do nothing
    }

    @Override
    public void setProbabilities() {
        // do nothing
    }

    @Override
    public List<String> generateText(int size) 
    {
        setProbabilities();
        Collection<String> wordList = wordFrequencies.keySet();
    
        List<String> words = new ArrayList<>(size);
        Random r = new Random();
        // should be based on most popular word?
        String word = getStartWord(wordList).get();
        words.add(word);
        for (int i=1; i<size; i++) {
            float prob = r.nextFloat();
            word = wordList.stream().skip(Math.round(Math.floor(prob * wordList.size()))).findFirst().get();
            words.add(properNouns.contains(word) ? Formatter.capword(word) : word);
        }
        return words;
    }

    @Override
    public String getNextWord(String word, Collection<? extends Node> nodesForWord,
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
    
}
