package com.edgriebel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractMarkov implements IMarkov
{

    public boolean allLowerCase = false;
    protected Collection<String> properNouns = new HashSet<>();

    public AbstractMarkov() {
        // TODO Auto-generated constructor stub
    }

    public Collection<String> findProperNouns(Collection<String> wordList) {
        // do we need to worry about ignoring words at the start of a sentence
        // providing a false proper noun signal?
        
        Map<Boolean, List<String>> splitWords = wordList
            .parallelStream()
            .distinct()
            .collect(Collectors.partitioningBy(w -> Character.isUpperCase(w.charAt(0))))
            ;
        
        List<String> properNouns = splitWords.get(true).stream().map(String::toLowerCase).collect(Collectors.toList());
        properNouns.removeAll(splitWords.get(false));
        
        return properNouns;
    }

    @Override
    public List<String> generateText(int size) 
    {
        setProbabilities();
    
        List<String> words = new ArrayList<>(size);
        String word = getStartWord();
        words.add(word);
        for (int i=1; i<size; i++) {
            word = getNextWord(word);
            words.add(properNouns.contains(word) ? Formatter.capword(word) : word);
        }
        return words;
    }
    
}
