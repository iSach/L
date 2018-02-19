package be.isach.eru.module.autoreply;

import be.isach.eru.module.Module;
import be.isach.eru.util.FilePaths;
import com.google.gson.Gson;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.RestAction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AutoReplyModule extends ListenerAdapter implements Module {
	private List<AutoReplyData> autoReplyData;
	
	public AutoReplyModule() {
		autoReplyData = new ArrayList<>();
	}
	
	@Override
	public void enable() {
		Gson gson = new Gson();
		File[] autoreplyFiles = FilePaths.AUTOREPLY.toFile().listFiles();
		if (autoreplyFiles != null) {
			for (File file : autoreplyFiles) {
				try {
					StringBuilder lines = new StringBuilder();
					try (BufferedReader br = new BufferedReader(new FileReader(file))) {
						for (String line; (line = br.readLine()) != null; ) {
							lines.append(line).append("\n");
						}
						br.close();
					}
					AutoReplyData data = gson.fromJson(lines.toString(), AutoReplyData.class);
					autoReplyData.add(data);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void disable() {
		autoReplyData.clear();
	}
	
	@Override
	public String getName() {
		return "AutoReply";
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		String message = event.getMessage().getContentDisplay();
		AutoReplyData responseToGive = null;
		int weight = 0;
		
		for (AutoReplyData data : autoReplyData) {
			for (Word word : data.words) {
				if (message.contains(word.word)) {
					weight += word.weight;
				}
			}
			if (data.inherit != null && !data.inherit.isEmpty()) {
				for (String dataName : data.inherit) {
					try {
						AutoReplyData inheritedData = getAutoReplyDataByName(dataName);
						for (Word word : inheritedData.words) {
							if (message.contains(word.word)) {
								weight += word.weight;
							}
						}
					} catch (Exception ignored) {
					}
				}
			}
			
			if (weight >= data.requiredweight) {
				if (responseToGive != null) {
					if (weight > responseToGive.requiredweight) {
						responseToGive = data;
					}
				} else {
					responseToGive = data;
				}
			}
			
			weight = 0;
		}
		
		if (responseToGive != null) {
			RestAction restAction = event.getMessage().getChannel().sendMessage(responseToGive.response.replace("{author}", event.getAuthor().getAsMention()));
			restAction.queue();
		}
	}
	
	private AutoReplyData getAutoReplyDataByName(String name) {
		for (AutoReplyData data : autoReplyData) {
			if (data.name.equalsIgnoreCase(name)) {
				return data;
			}
		}
		return null;
	}
}