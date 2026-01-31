package lol.sylvie.mortalec;

import lol.sylvie.mortalec.config.ModConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class MortalEnderChests implements ModInitializer {
	public static final String MOD_ID = "mortal-echests";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ModConfig config;

	@Override
	public void onInitialize() {
		LOGGER.info("making ender chests less op since 2026");
		File configFile = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json").toFile();
		config = ModConfig.fromFile(configFile);
	}
}