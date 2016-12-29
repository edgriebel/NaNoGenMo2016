package com.edgriebel;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
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

    public Optional<String> getStartWord(Collection<String> words) {
        int c = (new Random()).nextInt(words.size());
        return words.stream().filter(word -> !word.matches(Formatter.PUNCTUATION)).skip(c).findFirst();
    }


}
