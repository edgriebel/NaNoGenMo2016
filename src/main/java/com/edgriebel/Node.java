package com.edgriebel;

public class Node implements Comparable<Node> {
    private String from;
    private int count;
    private Double freq;

    public Node() {
    }
    
    public Node(String from) {
        this.from = from;
        count = 1;
    }

    public int incCount() {
        return count++;
    }
    
    @Override
    public boolean equals(Object obj) {
        return from.equals(((Node)obj).from);
    }
    
    @Override
    public int hashCode() {
        return from.hashCode();
    }

    @Override
    public int compareTo(Node o) {
        return from.compareTo(o.from);
    }
    
    @Override
    public String toString() {
        return String.format("'%s': ct:%d%s", from, count, freq != null ? ("/" + freq.toString()) : "");
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setFreq(Double freq) {
        this.freq = freq;
    }

    public Double getFreq() {
        return freq;
    }
}