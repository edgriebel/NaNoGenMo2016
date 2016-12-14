package com.edgriebel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MarkovBigram {

    
    public static final String END_OF_SENTENCE = "[.!?]";
    public static final String PUNCTUATION = "[.,!?]";
    public static int SECTION_WORD_COUNT = 40;
    
    public boolean allLowerCase = false;
    protected Set<String> wordsInCaps = new HashSet<>();
    
    public String capword(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    
    public void store(List<String> l, Map<String, Map<Node, Node>> freqs) {
        if (l.isEmpty() || l.size() == 1)
            return;
        
        String last = null;
        for (String curr : l) {
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
            
            if (!allLowerCase && !last.matches(PUNCTUATION) && Character.isUpperCase(curr.charAt(0)))
                wordsInCaps.add(curr.toLowerCase());
                
            Node ft = new Node(curr.toLowerCase());
            
            Map<Node, Node> matches = freqs.get(last);
            Node res = matches.get(ft);
            if ( res == null) 
                matches.put(ft, ft);
            else
                res.incCount();
                
            last = curr.toLowerCase();
        }
        
        System.out.println("Uppercase words: " + freqs.keySet().stream().filter(w -> Character.isUpperCase(w.charAt(0))).collect(Collectors.toList()));
        assert(allLowerCase ? wordsInCaps.isEmpty() : true);
        
        assert(freqs.keySet().stream().noneMatch(s -> Character.isUpperCase(s.charAt(0))));
        
        System.err.println("Size of capwords: " + wordsInCaps.size() + " First 10: " + wordsInCaps.stream().limit(10).collect(Collectors.toList()));
    }
    
    public static void setProbability(Collection<Node> nodes) {
        double sumCount = nodes.stream().map(n -> n.getCount()).reduce(0, Integer::sum);
        nodes.parallelStream().forEach(n -> n.setFreq(n.getCount() / sumCount));
    }

    public void setProbabilities(Map<String, Map<Node, Node>> nodemap) {
        nodemap.values().stream().map(n -> n.values()).forEach(MarkovBigram::setProbability);
    }

    public List<List<String>> splitIntoSections(List<String> words) {
        List<List<String>> rtn = new ArrayList<>();

        List<String> builder = new ArrayList<>();
        rtn.add(builder);
        for (int i=0; i<words.size(); i++) {
            String w = words.get(i);

            builder.add(w);

            if (builder.size() > SECTION_WORD_COUNT && w.matches(END_OF_SENTENCE)) {
                // generate block
                //                sb.append("\n\n");
                builder = new ArrayList<>();
                rtn.add(builder);
            }
        }
        builder.add(".");
        return rtn;
    }

    public String wrapText(String text) {
        StringBuilder rtn = new StringBuilder();
        
        int length = 0;
        final int linelength = 80;

        for (String word : text.split(" ")) {
            rtn.append(word).append(" ");
            // word-wrap line
            length += word.length() + 1; // one for space
            if (length >= linelength) {
                length = 0;
                rtn.append("\n");
            }
        }
        return rtn.toString();
    }
    
    public String formatText(List<String> words) {
        // assume 5 characters + space per word
        StringBuilder rtn = new StringBuilder(words.size() * 6);
        String lastWord = null;
        
        for (String w : words) {
            if (lastWord == null || lastWord.matches(END_OF_SENTENCE)) {
                w = capword(w);
            }
            if (w.equalsIgnoreCase("i"))
                w = capword(w);
            rtn.append(w.matches(PUNCTUATION) || lastWord == null ? w : " " + w);
            lastWord = w;
        }

        if (!lastWord.matches(PUNCTUATION))
            rtn.append(".");
        
        return rtn.toString();
    }
    
    public List<String> formatTextOld(List<String> words) {
        List<String> rtn = new ArrayList<>();
        
        StringBuilder sb = new StringBuilder();
        
        int wordcount = 0;
        int length = 0;
        final int linelength = 80;
        
        for (int i=0; i<words.size(); i++) {
            String w = words.get(i);
            if (i == 0 || words.get(i-1).matches(END_OF_SENTENCE))
                w = capword(w);

            sb.append(w.matches(PUNCTUATION) || length == 0 ? w : " " + w);
            
            wordcount++;
            
            if (wordcount > 40 && w.matches(END_OF_SENTENCE)) {
                // generate block
//                sb.append("\n\n");
                rtn.add(sb.toString());
                sb = new StringBuilder();
                wordcount = 0;
                length = 0;
            }
            else {
                // word-wrap line
                length += w.length() + (w.matches(PUNCTUATION) ? 0 : 1); // one for space
                if (length >= linelength && words.get(i+1).length() > 1) {
                    length = 0;
                    sb.append("\n");
                }
            }
            
        }
        sb.append(".");
        rtn.add(sb.toString());
        return rtn;
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
            words.add(wordsInCaps.contains(word) ? capword(word) : word);
        }
        return words;
    }

    public Optional<String> getStartWord(Set<String> words) {
        int c = (new Random()).nextInt(words.size());
        return words.stream().filter(word -> !word.matches(MarkovBigram.PUNCTUATION)).skip(c).findFirst();
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
