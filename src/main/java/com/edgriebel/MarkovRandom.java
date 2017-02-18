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
    protected Map<String, Integer> wordCounts;
    protected List<String> allWords;
    
    public MarkovRandom() {
        wordCounts = new HashMap<>();
        allWords = new ArrayList<>();
    }
    
    @Override
    public void store(List<String> l) {
        if (l.isEmpty() || l.size() == 1)
            return;
        
        l.forEach(word -> wordCounts.merge(word.toLowerCase(), 1, Integer::sum));
        
        l.forEach(word -> allWords.add(word.toLowerCase()));
        
        properNouns = findProperNouns(l);
        
        assert(allWords.stream().noneMatch(s -> Character.isUpperCase(s.charAt(0))));
        
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

    private Random r = new Random();

    @Override
    public String getStartWord() {
        int c = (new Random()).nextInt(wordCounts.keySet().size());
        return wordCounts.keySet().stream().filter(word -> !word.matches(Formatter.PUNCTUATION)).skip(c).findFirst().get();
    }
    
    @Override
    public String getNextWord(String... word) {
        int nextWord = r.nextInt(wordCounts.size());
        return allWords.get(nextWord);
    }
    
}
