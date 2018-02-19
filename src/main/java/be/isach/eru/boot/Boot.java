package be.isach.eru.boot;

import be.isach.eru.Eru;
import be.isach.eru.util.AdvancedJSONObject;
import be.isach.eru.util.FilePaths;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;

public class Boot {
    public static void main(String... args) throws LoginException, InterruptedException {
        try {
            AdvancedJSONObject object = new AdvancedJSONObject(new String(Files.readAllBytes(FilePaths.TOKEN)));
            object.addDefault("token", "");
            Files.write(FilePaths.TOKEN, object.toString(4).getBytes());
            String token = object.getString("token");
            
            new Eru(token);
        } catch (IOException e) {
            System.out.println("Generating token.json please insert a token");
            JSONObject object = new JSONObject();
            object.put("token", "INSERT BOT TOKEN HERE");
            try {
                Files.write(FilePaths.TOKEN, object.toString(4).getBytes());
            } catch (IOException e1) {
                System.out.println("Failed to generate token.json (perms?)");
            }
            System.exit(1);
        }
    }
}
