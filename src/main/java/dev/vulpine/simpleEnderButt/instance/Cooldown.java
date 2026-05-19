package dev.vulpine.simpleEnderButt.instance;

import dev.vulpine.simpleEnderButt.manager.CooldownManager;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
public class Cooldown extends BukkitRunnable {

    private final CooldownManager cooldownManager;

    private final int totalTime;
    private int remainingTime = 0;

    public Cooldown(CooldownManager cooldownManager, int totalTime) {

        this.cooldownManager = cooldownManager;

        this.totalTime = totalTime;
    }

    @Override
    public void run() {

        if (remainingTime == 0) {
            cancel();
            cooldownManager.removeCooldown(this);
            return;
        }

        remainingTime--;

    }

    public void start() {
        remainingTime = totalTime;
        runTaskTimer(cooldownManager.getPlugin(), 0, 1);
    }

    public boolean isActive() {
        return remainingTime > 0;
    }
}
