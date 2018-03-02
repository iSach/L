package be.isach.eru.module.log;

import be.isach.eru.Eru;
import be.isach.eru.module.Module;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class LoggingModule extends ListenerAdapter implements Module {

    private Eru eru;

    public LoggingModule(Eru eru) {
        this.eru = eru;
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public String getName() {
        return "Logging Module";
    }

    public void log(Object o) {
        log(o, false);
    }

    public void log(Object o, boolean codeFormat) {
        try {
            JDA jda = eru.getJda();
            StringBuilder sb = new StringBuilder();
            sb.append("```").append(o.toString()).append("\n```");
            jda.getTextChannelById("415533574150946816").sendMessage(codeFormat ? sb.toString() : o.toString()).queue();
        } catch (Exception ignore) {

        }
    }

}
