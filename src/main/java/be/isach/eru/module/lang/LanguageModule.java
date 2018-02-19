package be.isach.eru.module.lang;

import be.isach.eru.Eru;
import be.isach.eru.module.Module;
import com.detectlanguage.DetectLanguage;
import com.detectlanguage.Result;
import com.detectlanguage.errors.APIError;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class LanguageModule extends ListenerAdapter implements Module {

    private Eru eru;

    Map<String, List<String>> allowed;

	public LanguageModule(Eru eru) {
	    this.eru = eru;
	    this.allowed = new HashMap<>();

        // English:
        allowed.put("185055040036143104", Arrays.asList("en"));

        // French
        allowed.put("195993949662347273", Arrays.asList("fr"));

        // Spanish
        allowed.put("195993985364393984", Arrays.asList("es"));

        // Dutch
        allowed.put("197330368536379400", Arrays.asList("nl", "af"));

        // German
        allowed.put("198539414652059667", Arrays.asList("de"));

        // Arabic
        allowed.put("198899798688923648", Arrays.asList("ar"));

        // Albanian
        allowed.put("276032011934629909", Arrays.asList("sq"));

        // Norwegian
        allowed.put("288706428250619904", Arrays.asList("no", "sv", "da"));

        // Hungarian
        allowed.put("331185695010062336", Arrays.asList("hr"));
	}

	@Override
	public void enable() {
        DetectLanguage.apiKey = eru.getLangToken();
	}

	@Override
	public void disable() {
	}

	@Override
	public String getName() {
		return "LangDetection";
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
	    try {
            if (event.getMessage().getCategory().getId().equalsIgnoreCase("356150938848985088")) {
                detectLanguage(event.getMessage());
            }
        } catch (Exception ignore){
        }
	}

    /**
     * Detect the language of input text.
     *
     * @param message source text to be detected for language
     *
     * @return Message is OK.
     */
    public boolean detectLanguage(Message message) {
        if(message == null) {
            return true;
        }

        if(message.getAuthor().isBot()) {
            return true;
        }

        String content = message.getContentDisplay();
        content = filterString(content);

        if(content.length() < 8
                || content.split(" ").length <= 4) {
            return true;
        }

        try {
            List<Result> results = DetectLanguage.detect(content);
            Result r = results.get(0);
            if(!r.isReliable) {
                return true;
            }
            System.out.println("[" + r.language + "] -> " + content);
            if(!allowed.get(message.getChannel().getId()).contains(r.language)) {
                System.out.println("Wrong language detected, deleting...");
                tryPrivateMessage(message, r.language);
                message.delete().queue();
                return false;
            }
        } catch (APIError apiError) {
            apiError.printStackTrace();
        }
        return true;
    }

    private String filterString(String ss) {
        StringBuilder sb = new StringBuilder();

        for(String str : ss.split(" ")) {
            String s = str.toLowerCase();
            if(s.isEmpty()) {
                continue;
            }
            if(s.equals("\uD83D\uDE03")
                    || s.equals("\uD83D\uDE1B")) {
                continue;
            }
            if(s.startsWith("@")
                    || s.startsWith("<")
                    || s.startsWith("`")
                    || s.startsWith("\\")
                    || s.startsWith("'")
                    || s.startsWith("ultra")
                    || s.startsWith("world")
                    || s.startsWith("treasure")
                    || s.startsWith("plugin")
                    || s.startsWith("world")
                    || s.startsWith("mob")
                    || s.startsWith("essential")
                    || s.startsWith("pint")
                    || Character.isDigit(s.charAt(0))
                    || s.startsWith("loot")
                    || s.startsWith("box")
                    || s.startsWith("chest")
                    || s.startsWith("permission")
                    || s.startsWith("server")
                    || s.startsWith("depend")
                    || s.startsWith("pet")
                    || s.startsWith("gadget")
                    || s.startsWith("cosmetic")
                    || s.startsWith("suit")
                    || s.startsWith("hat")
                    || s.startsWith("mount")
                    || s.startsWith("effect")
                    || s.startsWith("particle")
                    || s.startsWith("morph")
                    || s.startsWith("wiki")
                    || s.startsWith("help")
                    || s.startsWith("config")
                    || s.startsWith("message")) {
                continue;
            }
            sb.append(s + " ");
        }

        return sb.toString();
    }

    private void tryPrivateMessage(Message message, String language) {
        User user = message.getAuthor();
        user.openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage("Hello! It seems you were trying to send a message in language: " + language + " in the channel: " + message.getChannel().getName() + "!\nPlease send it in the correct channel. This feature is still in Bêta, please send your message content to @sach#5092 if you think this was an error!\nThanks").queue();
        });
    }
}