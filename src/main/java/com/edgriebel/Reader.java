package com.edgriebel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Reader {

    public List<String> read(File file) throws IOException {
        List<String> rtn = new ArrayList<>();
        try (Scanner sc = new Scanner(file)) {
            rtn = read(sc);
        }
        return rtn;
    }

    public List<String> read(Scanner sc) {
        List<String> rtn = new ArrayList<>();
        String keepers = "([,.!?])";
        String dump = "[:;/\"'()âÂ«»]";
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


}
