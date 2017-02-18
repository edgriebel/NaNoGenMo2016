package com.edgriebel;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MarkovBigram extends AbstractMarkov 
{
    // protected so it can be used in unit tests
    protected Map<String, Map<Node, Node>> wordFrequencies;
    
    public MarkovBigram() {
        wordFrequencies = new HashMap<>();
    }
    
    @Override
    public void store(List<String> l) {
        if (l.isEmpty() || l.size() == 1)
            return;
        
        String last = null;
        for (String curr : l) {
            final String currLC = curr.toLowerCase();
            if (last == null) {
                last = curr.toLowerCase();
                continue;
            }

            // use merge()!!
            if (!wordFrequencies.containsKey(last)) {
                wordFrequencies.put(last, new TreeMap<>());
            }
            
            // System.err.println(curr);
            assert(last.length() > 0);
            assert(curr.length() > 0);
            
            Node ft = new Node(currLC);
            
            Map<Node, Node> matches = wordFrequencies.get(last);
            Node res = matches.get(ft);
            if ( res == null) 
                matches.put(ft, ft);
            else
                res.incCount();
                
            last = currLC;
        }
        
        properNouns = findProperNouns(l);
        
        System.out.println("Uppercase words: " + wordFrequencies.keySet().stream().filter(w -> Character.isUpperCase(w.charAt(0))).collect(Collectors.toList()));
        
        assert(wordFrequencies.keySet().stream().noneMatch(s -> Character.isUpperCase(s.charAt(0))));
        
        System.err.println("Size of capwords: " + properNouns.size() + " First 10: " + properNouns.stream().limit(10).collect(Collectors.toList()));
    }

    @Override
    public void setProbability(Collection<? extends Node> nodes) {
        double sumCount = nodes.stream().map(n -> n.getCount()).reduce(0, Integer::sum);
        nodes.parallelStream().forEach(n -> n.setFreq(n.getCount() / sumCount));
    }

    @Override
    public void setProbabilities() {
        wordFrequencies.values().stream().map(n -> n.values()).forEach(this::setProbability);
    }

    private Random r = new Random();
    
    public Collection<? extends Node> lookupNodes(String... word) {
        Collection<Node> nodesForWord = wordFrequencies.get(word[0]).values();
        return nodesForWord;
    }
    
    @Override
    public String getStartWord() {
        int c = (new Random()).nextInt(wordFrequencies.keySet().size());
        return wordFrequencies.keySet().stream().filter(word -> !word.matches(Formatter.PUNCTUATION)).skip(c).findFirst().get();
    }
    
    @Override
    public String getNextWord(String... word) {
        float prob = r.nextFloat();
        double sum = 0;
        String foundWord = null;
        
        Collection<? extends Node> possibleWords = lookupNodes(word);
        for (Node n : possibleWords) {
            sum += n.getFreq();
            if (prob < sum) {
                foundWord = n.getFrom();
                break;
            }
        }
        return foundWord;
    }

}
