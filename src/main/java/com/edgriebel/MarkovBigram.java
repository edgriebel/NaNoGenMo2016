package com.edgriebel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MarkovBigram extends AbstractMarkov 
{
    public void store(List<String> l, Map<String, Map<Node, Node>> freqs) {
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
            if (!freqs.containsKey(last)) {
                freqs.put(last, new TreeMap<>());
            }
            
            // System.err.println(curr);
            assert(last.length() > 0);
            assert(curr.length() > 0);
            
            Node ft = new Node(currLC);
            
            Map<Node, Node> matches = freqs.get(last);
            Node res = matches.get(ft);
            if ( res == null) 
                matches.put(ft, ft);
            else
                res.incCount();
                
            last = currLC;
        }
        
        properNouns = findProperNouns(l);
        
        System.out.println("Uppercase words: " + freqs.keySet().stream().filter(w -> Character.isUpperCase(w.charAt(0))).collect(Collectors.toList()));
        
        assert(freqs.keySet().stream().noneMatch(s -> Character.isUpperCase(s.charAt(0))));
        
        System.err.println("Size of capwords: " + properNouns.size() + " First 10: " + properNouns.stream().limit(10).collect(Collectors.toList()));
    }
    
    public static void setProbability(Collection<Node> nodes) {
        double sumCount = nodes.stream().map(n -> n.getCount()).reduce(0, Integer::sum);
        nodes.parallelStream().forEach(n -> n.setFreq(n.getCount() / sumCount));
    }

    public void setProbabilities(Map<String, Map<Node, Node>> nodemap) {
        nodemap.values().stream().map(n -> n.values()).forEach(MarkovBigram::setProbability);
    }

    public List<String> generateText(Map<String, Map<Node, Node>> wordList, int size) 
    {
        setProbabilities(wordList);
    
        List<String> words = new ArrayList<>(size);
        Random r = new Random();
        String word = getStartWord(wordList.keySet()).get();
        words.add(word);
        for (int i=1; i<size; i++) {
            float prob = r.nextFloat();
            Collection<Node> nodesForWord = wordList.get(word).values();
            word = getNextWord(word, nodesForWord, prob);
            words.add(properNouns.contains(word) ? Formatter.capword(word) : word);
        }
        return words;
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
}
