package be.isach.eru.listener;

import java.util.ArrayList;
import java.util.List;

public class AutoReplyData {

    private List<String> requiredWords;
    private List<String> softRequiredWords;
    private String message;

    public AutoReplyData(String message) {
        this.requiredWords = new ArrayList<>();
        this.softRequiredWords = new ArrayList<>();
        this.message = message;
    }

    public List<String> getRequiredWords() {
        return requiredWords;
    }

    public List<String> getSoftRequiredWords() {
        return softRequiredWords;
    }

    public void addRequiredWords(String... words) {
        for(String word : words) {
            requiredWords.add(word.toLowerCase());
        }
    }

    public void addSoftRequiredWords(String... words) {
        for(String word : words) {
            softRequiredWords.add(word.toLowerCase());
        }
    }

    public String getMessage() {
        return message;
    }
}
