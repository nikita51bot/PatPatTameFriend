package ru.nikita51.patpat_tame_friend;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;

public class PatAdvancement {

	private final NamespacedKey key;
	private final String icon;
	private final String title;
	private final String description;
	private final Style style;

	private Advancement advancement;


	public PatAdvancement(NamespacedKey key, String icon, String title, String description, Style style) {
		this.key     = key;
		this.icon  = icon;
		this.title = title;
		this.description = description;
		this.style = style;
	}

	public PatAdvancement register() {
		this.advancement = Bukkit.getUnsafe().loadAdvancement(key, """
				{
				  "display": {
				    "icon": {
				      "id": "%s",
				      "count": 1
				    },
				    "title": "%s",
				    "description": "%s",
				    "background": "minecraft:block/mossy_cobblestone",
				    "frame": "%s",
				    "show_toast": true,
				    "announce_to_chat": false,
				    "hidden": false
				  },
				  "criteria": {
				    "trigger": {
				      "trigger": "minecraft:impossible"
				    }
				  },
				  "requirements": [
				    [
				      "trigger"
				    ]
				  ]
				}
				""".formatted(icon, title, description, style.toString().toLowerCase()));
		return this;
	}

	private void assertAdvancement() {
		if (this.advancement == null) {
			throw new IllegalStateException("Before show toast, you need to register this advancement!");
		}
	}

	public void grantAdvancement(Player player) {
		assertAdvancement();
		player.getAdvancementProgress(advancement).awardCriteria("trigger");
	}

	public void revokeAdvancement(Player player) {
		assertAdvancement();
		player.getAdvancementProgress(advancement).revokeCriteria("trigger");
	}

	public enum Style {
		GOAL,
		TASK,
		CHALLENGE
	}

}
