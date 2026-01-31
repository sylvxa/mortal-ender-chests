package lol.sylvie.mortalec.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import java.io.*;

import static lol.sylvie.mortalec.MortalEnderChests.LOGGER;

public class ModConfig {
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    @SerializedName("drop_slots")
    public int dropSlots = 27;

    @SerializedName("only_players")
    public boolean onlyPlayers = true;

    @SerializedName("respect_keep_inventory")
    public boolean respectKeepInventory = true;

    public static ModConfig fromFile(File file) {
        try (FileReader reader = new FileReader(file)) {
            return GSON.fromJson(reader, ModConfig.class);
        } catch (IOException | JsonSyntaxException readException) {
            if (!(readException instanceof FileNotFoundException))
                LOGGER.warn("Couldn't load config!", readException);
            ModConfig instance = new ModConfig();
            instance.writeToFile(file);
            return instance;
        }
    }

    public void writeToFile(File file) {
        LOGGER.info("Writing config to file");
        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(this, writer);
        } catch (IOException writeException) {
            LOGGER.error("Couldn't write config!", writeException);
        }
    }
}
