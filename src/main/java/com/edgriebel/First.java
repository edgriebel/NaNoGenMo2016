package com.edgriebel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class First {

    public First() {
        // TODO Auto-generated constructor stub
    }

    public static final String END_OF_SENTENCE = "[.!?]";
    public static final String PUNCTUATION = "[.,!]";
    public static int SECTION_WORD_COUNT = 40;
    
    public String capword(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
    
    public void store(List<String> l, Map<String, Map<Node, Node>> freqs) {
        if (l.isEmpty() || l.size() == 1)
            return;
        
        String last = null;
        for (String curr : l) {
            if (last == null) {
                last = curr;
                continue;
            }

            Node ft = new Node(curr);
            
            // use merge()!!
            if (!freqs.containsKey(last)) {
                freqs.put(last, new TreeMap<>());
            }
                Map<Node, Node> matches = freqs.get(last);
                Node res = matches.get(ft);
                if ( res == null) 
                    matches.put(ft, ft);
                else
                    res.incCount();
                
            last = curr;
        }
    }
    
    public List<String> read(File file) throws IOException {
        List<String> rtn = new ArrayList<>();
        try (Scanner sc = new Scanner(file)) {
            rtn = read(sc);
        }
        return rtn;
    }


    public List<String> read(Scanner sc) {
        List<String> rtn = new ArrayList<>();
        String keepers = "([,.!])";
        String dump = "[:;/\"'()]";
        Pattern p = Pattern.compile(keepers);
        Pattern d = Pattern.compile(dump);
        while (sc.hasNext()) {
            String s = sc.next().toLowerCase();
            s = p.matcher(s).replaceAll(" $1");
            s = d.matcher(s).replaceAll("");
            if (! s.isEmpty() && !s.matches("^[\\d]+$")) {
                rtn.addAll(Arrays.asList(s.split(" ")));
            }
        }
        return rtn;
    }
    
    public static void setProbability(Collection<Node> nodes) {
        double sumCount = nodes.stream().map(n -> n.getCount()).reduce(0, Integer::sum);
        nodes.parallelStream().forEach(n -> n.setFreq(n.getCount() / sumCount));
    }

    public void setProbabilities(Map<String, Map<Node, Node>> nodemap) {
        nodemap.values().stream().map(n -> n.values()).forEach(First::setProbability);
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
            words.add(word);
        }
        return words;
    }

    public Optional<String> getStartWord(Set<String> words) {
        int c = (new Random()).nextInt(words.size());
        return words.stream().filter(word -> !word.matches(First.PUNCTUATION)).skip(c).findFirst();
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
