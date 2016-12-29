package com.edgriebel;

import java.util.List;

public class GenerateText {

    public GenerateText() {
        // TODO Auto-generated constructor stub
    }

    public List<String> generateText(int size, List<String> words, IMarkov markov)
    {
        markov.store(words);
        List<String> text = markov.generateText(size);
        return text;
    }

    public String formatText(List<String> words, Formatter f)
    {
        List<List<String>> sections = f.splitIntoSections(words);
        String newStr = sections.stream().map(s1 -> f.formatText(s1)).map(s2 -> f.wrapText(s2, 80)).reduce("", (x, y) -> x += y + "\n\n");
        return newStr;
    }

    /**
     * convenience method that calls {@link #generateText(int, List, IMarkov)} then {@link #formatText(List, Formatter)}
     * 
     * @param size
     * @param words
     * @param markov
     * @param f
     * @return
     */
    public String generateAndFormatText(int size, List<String> words, IMarkov markov, Formatter f)
    {
       List<String> text = generateText(size, words, markov);
       String formattedText = formatText(text, f);
       
       return formattedText;
    }

    public static void main(String [] args) 
    {
        
    }
}
