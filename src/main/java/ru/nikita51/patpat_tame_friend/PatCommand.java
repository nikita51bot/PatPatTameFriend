package ru.nikita51.patpat_tame_friend;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import ru.nik51.patpat.plugin.api.PatPatPluginAPI;

import java.util.*;
import org.jetbrains.annotations.NotNull;

public record PatCommand(PatPatPluginAPI api) implements TabExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		if (strings.length != 1) {
			return false;
		}

		String value = strings[0];
		Player whoPatted = commandSender instanceof Player player ? player : null;
		try {
			UUID uuid = UUID.fromString(value);
			Entity entity = Bukkit.getEntity(uuid);
			if (entity == null) {
				commandSender.sendMessage("Error: entity '{}' is not exist", uuid.toString());
				return true;
			}
			if (!(entity instanceof LivingEntity livingEntity)) {
				commandSender.sendMessage("Error: entity '{}' is not LivingEntity", uuid.toString());
				return true;
			}
			api.patEntity(livingEntity, whoPatted);
		} catch (IllegalArgumentException ignored) {
			Player player = Bukkit.getPlayerExact(value);
			if (player == null) {
				commandSender.sendMessage("Error: player '{}' is not exist", value);
				return true;
			}
			if (!player.isOnline()) {
				commandSender.sendMessage("Error: player '{}' is not online", value);
				return true;
			}
			api.patEntity(player, whoPatted);
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		if (strings.length != 1) {
			return Collections.emptyList();
		}
		String value = strings[0];
		List<String> suggestions = new ArrayList<>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			String name = player.getName();
			if (name.startsWith(value)) {
				suggestions.add(name);
			}
		}

		if (!(commandSender instanceof Player player)) {
			return suggestions;
		}
		for (Entity entity : player.getNearbyEntities(32, 32, 32)) {
			if (!(entity instanceof LivingEntity) || entity instanceof Player) {
				continue;
			}
			String uuid = entity.getUniqueId().toString();
			if (uuid.startsWith(value)) {
				suggestions.add(uuid);
			}
		}
		return suggestions;
	}
}
