package be.isach.eru.module.autoreply;

import java.util.List;

class AutoReplyData {
	public String name;
	public String description;
	public String response;
	public List<Word> words;
	public int requiredweight;
	public List<String> inherit;
}

class Word {
	public String word;
	public int weight;
}