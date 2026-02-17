package ru.nikita51.patpat_tame_friend.event;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.nik51.patpat.plugin.api.event.*;
import ru.nikita51.patpat_tame_friend.PatAdvancement;
import ru.nikita51.patpat_tame_friend.PatAdvancement.Style;
import ru.nikita51.patpat_tame_friend.PatPatTameFriend;

import java.security.SecureRandom;
import java.util.Random;

public class PatEventListener implements Listener {

	private static final Random RANDOM = new SecureRandom();

	private final PatAdvancement advancement;

	public PatEventListener() {
		this.advancement = new PatAdvancement(
				new NamespacedKey(PatPatTameFriend.getInstance(), "person_friend"),
				"minecraft:wolf_spawn_egg",
				"A person's friend",
				"Tame wolf or cat with pats",
				Style.TASK
		).register();
	}

	@EventHandler
	private void onPat(PatPacketReceiveEvent event) {
		System.out.printf("PatPacketReceiveEvent: %s%n", event.isCancelled());
		Player player = event.getWhoPatted();
		LivingEntity pattedEntity = event.getPattedEntity();
		if (pattedEntity instanceof Wolf wolf && tryTameMob(wolf, wolf.isAngry() ? 1 : 2, player)) {
			this.advancement.grantAdvancement(player);
		}
		if (pattedEntity instanceof Cat cat && tryTameMob(cat, 2, player)) {
			this.advancement.grantAdvancement(player);
		}

	}

	private static boolean tryTameMob(Tameable mob, int chance, Player player) {
		if (mob.isTamed()) {
			return false;
		}
		if (RANDOM.nextDouble(100) > chance) {
			return false;
		}
		mob.setOwner(player);
		mob.setTamed(true);
		spawnHeartParticles(mob);
		return true;
	}

	private static void spawnHeartParticles(LivingEntity livingEntity) {
		Location location = livingEntity.getLocation();
		livingEntity.getWorld().spawnParticle(Particle.HEART,
				location.getX(),
				location.getY() + 1,
				location.getZ(),
				5,
				RANDOM.nextGaussian() * 0.02,
				0.5,
				RANDOM.nextGaussian() * 0.02,
				1.0
		);
	}

}
