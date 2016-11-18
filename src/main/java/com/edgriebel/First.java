package com.edgriebel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class First {

    public First() {
        // TODO Auto-generated constructor stub
    }

    public static final String END_OF_SENTENCE = "[.!?]";
    public static final String PUNCTUATION = "[.,!]";
    
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


    public List<String> formatText(List<String> words) {
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
}
