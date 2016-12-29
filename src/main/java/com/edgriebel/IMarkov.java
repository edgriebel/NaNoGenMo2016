package com.edgriebel;

import java.util.Collection;
import java.util.List;

public interface IMarkov {

    public void store(List<String> l);
    
    public void setProbability(Collection<? extends Node> nodes);

    public List<String> generateText(int numberOfWords);
    
    public String getNextWord(String word, Collection<? extends Node> nodesForWord, float probability);

    public void setProbabilities();

}
