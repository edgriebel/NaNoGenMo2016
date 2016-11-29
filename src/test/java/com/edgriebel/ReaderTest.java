package com.edgriebel;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

public class ReaderTest {

    @Test
    public void testRead_bible_luke_txt() throws Exception {
        System.out.println(new File(".").getCanonicalPath());
        File f = new File("./bible-luke.txt");
        List<String> s = impl.read(f);
        System.out.println(s);
    }

    @Test
    public void testRead_commas() throws Exception {
        final String str = "hello, world!";
        List<String> s = impl.read(new Scanner(str));
        System.out.println(s);
        assertTrue(s.stream().anyMatch( x -> ",".equals(x))); 
        assertTrue(s.stream().anyMatch( x -> str.substring(str.length()-1).equals(x))); 
    }

    @Test
        public void testRead_shorttxt() throws Exception {
            File f = new File("./short.txt");
            List<String> s = impl.read(f);
            System.out.println(s);
    //        assertThat(s, contains("."));
    }
    
    Reader impl;
    
    @Before
    public void setupImpl() {
        impl = new Reader();
    }


}
