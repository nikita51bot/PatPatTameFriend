package ru.nikita51.patpat_tame_friend;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.nikita51.patpat_tame_friend.event.PatEventListener;

import ru.nik51.patpat.plugin.api.PatPatPluginAPI;

import java.util.Objects;
import java.util.logging.Logger;

public final class PatPatTameFriend extends JavaPlugin {

	public static final Logger LOGGER = Logger.getLogger("PatPatTameFriend");
	public static PatPatTameFriend instance;

	public static PatPatTameFriend getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Method invoked too early");
		}
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;

		PatPatPluginAPI api = Bukkit.getServicesManager().load(PatPatPluginAPI.class);
		if (api == null) {
			LOGGER.warning("PatPatAPI is not loaded!");
		} else {
			LOGGER.info("PatPatAPI is loaded");
			Objects.requireNonNull(this.getCommand("pat"), "Command 'pat' is not registered").setExecutor(new PatCommand(api));
		}

		this.getServer().getPluginManager().registerEvents(new PatEventListener(), this);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
