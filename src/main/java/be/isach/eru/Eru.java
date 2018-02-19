package be.isach.eru;

import be.isach.eru.module.Module;
import be.isach.eru.module.autoreply.AutoReplyModule;
import be.isach.eru.module.lang.LanguageModule;
import be.isach.eru.runtime.ShutdownThread;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

public class Eru {

    /* TODO

        - detect Java errors sent directly in chat
          and automatically paste them on Pastebin.

        - Anti-spam

        - Anti-swear (?)

    */

    private JDA jda;

    private List<Module> modules;

    private String langToken;

    public Eru(String token, String langToken) throws LoginException, InterruptedException {
        jda = new JDABuilder(AccountType.BOT).setToken(token).buildBlocking();
        this.langToken = langToken;

        Runtime.getRuntime().addShutdownHook(new ShutdownThread(this));

        // Modules
        modules = new ArrayList<>();

        AutoReplyModule autoReplyModule = new AutoReplyModule();
        LanguageModule languageModule = new LanguageModule(this);

        modules.add(autoReplyModule);
        modules.add(languageModule);

        modules.forEach(module -> jda.addEventListener((ListenerAdapter)module));
        modules.forEach(Module::enable);
    }

    public void shutdown() {
        modules.forEach(Module::disable);
        jda.shutdownNow();
    }

    public String getLangToken() {
        return langToken;
    }
}