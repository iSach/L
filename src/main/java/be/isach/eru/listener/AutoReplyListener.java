package be.isach.eru.listener;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.RestAction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AutoReplyListener extends ListenerAdapter {

    private static final ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(4);

    private List<AutoReplyData> autoReplies;

    public AutoReplyListener() {
        this.autoReplies = new ArrayList<>();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String content = message.getContentDisplay().toLowerCase();

        mainLoop:
        for(AutoReplyData data : autoReplies) {
            String toSend = data.getMessage();
            List<String> required = data.getRequiredWords();
            List<String> softRequired = data.getSoftRequiredWords();

            for(String s : required) {
                if (!content.contains(s)) {
                    continue mainLoop;
                }
            }

            boolean oneSoftFound = false;

            softLoop:
            for(String s : softRequired) {
                if (content.contains(s)) {
                    oneSoftFound = true;
                    break softLoop;
                }
            }

            if(!oneSoftFound) {
                continue;
            }

            RestAction restAction = message.getChannel().sendMessage(toSend.replace("{author}", message.getAuthor().getAsMention()));
            restAction.queue(o -> EXECUTOR.schedule(() -> ((Message)o).delete().queue(), 5, TimeUnit.SECONDS));
            message.delete().queue();

            return;
        }
    }

    public void addData(AutoReplyData... datas) {
        for(AutoReplyData data : datas) {
            autoReplies.add(data);
        }
    }
}
