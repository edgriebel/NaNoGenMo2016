package com.edgriebel;

import java.util.ArrayList;
import java.util.List;

public class Formatter {

    public static final String END_OF_SENTENCE = "[.!?]";
    public static final String PUNCTUATION = "[.,!?]";
    public static int SECTION_WORD_COUNT = 40;
    public static final int linelength = 80;

    public Formatter() {
        // TODO Auto-generated constructor stub
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
                    if (length >= linelength && words.size() < i && words.get(i+1).length() > 1) {
                        length = 0;
                        sb.append("\n");
                    }
                }
                
            }
            sb.append(".");
            rtn.add(sb.toString());
            return rtn;
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

    public static String capword(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

}
