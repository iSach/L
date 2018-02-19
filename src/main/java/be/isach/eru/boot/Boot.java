package be.isach.eru.boot;

import be.isach.eru.Eru;
import be.isach.eru.util.AdvancedJSONObject;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Boot {

    public static void main(String... args) throws LoginException, InterruptedException {

        try {
            AdvancedJSONObject object = new AdvancedJSONObject(new String(Files.readAllBytes(Paths.get("token.json"))));
            object.addDefault("token", "");
            Files.write(Paths.get("token.json"), object.toString(4).getBytes());
            String token = object.getString("token");

            new Eru(token);
        } catch (IOException e) {
            System.out.println("Generating token.json please insert a token");
            JSONObject object = new JSONObject();
            object.put("token", "INSERT BOT TOKEN HERE");
            try {
                Files.write(Paths.get("token.json"), object.toString(4).getBytes());
            } catch (IOException e1) {
                System.out.println("Failed to generate token.json (perms?)");
            }
            System.exit(1);
        }
    }
}
