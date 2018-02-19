package be.isach.eru;

import be.isach.eru.module.Module;
import be.isach.eru.module.autoreply.AutoReplyModule;
import be.isach.eru.runtime.ShutdownThread;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

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
    
    public  Eru(String token) throws LoginException, InterruptedException {
        jda = new JDABuilder(AccountType.BOT).setToken(token).buildBlocking();
        
        Runtime.getRuntime().addShutdownHook(new ShutdownThread(this));
        
        // Modules
        modules = new ArrayList<>();
        
        AutoReplyModule autoReplyModule = new AutoReplyModule();
        jda.addEventListener(autoReplyModule);
        modules.add(autoReplyModule);
        
        modules.forEach(Module::enable);
    }
    
    public void shutdown() {
        modules.forEach(Module::disable);
        jda.shutdownNow();
    }
}