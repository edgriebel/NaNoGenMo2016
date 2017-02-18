package com.edgriebel;

import java.util.Collection;
import java.util.List;

public interface IMarkov {

    void store(List<String> l);
    
    void setProbability(Collection<? extends Node> nodes);

    List<String> generateText(int numberOfWords);
    
    String getNextWord(String... word);

    void setProbabilities();

    String getStartWord();
}
