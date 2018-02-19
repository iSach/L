package be.isach.eru;

import be.isach.eru.listener.AutoReplyData;
import be.isach.eru.listener.AutoReplyListener;
import be.isach.eru.runtime.ShutdownThread;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class Eru {

    /* TODO

        - detect Java errors sent directly in chat
          and automatically paste them on Pastebin.

        - Anti-spam

        - Anti-swear (?)

    */

    private JDA jda;

    public Eru(String token) throws LoginException, InterruptedException {
        jda = new JDABuilder(AccountType.BOT).setToken(token).buildBlocking();

        Runtime.getRuntime().addShutdownHook(new ShutdownThread(this));

        AutoReplyListener autoReplyListener = new AutoReplyListener();

        // Reply data
        // Permissions
        AutoReplyData permissions = new AutoReplyData("Hello {author}! Permission are on: ** https://bit.ly/PermsUC **!");
        permissions.addRequiredWords("perm");
        permissions.addSoftRequiredWords("where", "give");

        // Wiki
        AutoReplyData wiki = new AutoReplyData("Hello {author}! Wiki is here: ** https://github.com/iSach/UltraCosmetics/wiki **!");
        wiki.addRequiredWords("wiki");
        wiki.addSoftRequiredWords("where", "link");

        // Treasure Chests
        AutoReplyData treasureChests = new AutoReplyData("Hello {author}! Treasure Chests info here: ** https://bit.ly/UCTChests **!");
        treasureChests.addRequiredWords("treasure", "chest");
        treasureChests.addSoftRequiredWords("help", "link", "make", "how to use");

        autoReplyListener.addData(permissions, wiki, treasureChests);

        jda.addEventListener(autoReplyListener);
    }

    public void shutdown() {
        jda.shutdownNow();
    }

}
