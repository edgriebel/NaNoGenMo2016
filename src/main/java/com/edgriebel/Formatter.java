package com.edgriebel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

public class Formatter {

    public static final String END_OF_SENTENCE = "[.!?]";
    public static final String PUNCTUATION = "[.,!?]";
    public static int SECTION_WORD_COUNT = 40;
    public static final int linelength = 80;

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

    public String wrapText(String text, int lineLength) {
        return WordUtils.wrap(text, lineLength);
    }

    public static String capword(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

}
