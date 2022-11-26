package jp.ahoaho;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class EventListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        List<String> userList = Hardcore3.config.getStringList("users");
        int life = Hardcore3.config.getInt("life");

        if(life <= 0 && userList.size() != 0) {
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
        } else {
            if(!event.getPlayer().hasPermission("op")) {
                event.getPlayer().setGameMode(GameMode.SURVIVAL);
            }
        }

        if(!userList.contains(event.getPlayer().getUniqueId().toString())) {
            // configにない 初回接続の時
            userList.add(event.getPlayer().getUniqueId().toString());
            Hardcore3.config.set("users", userList);
            life = life + 8;
            Hardcore3.config.set("life", life);
            Hardcore3.getPlugin(Hardcore3.class).reloadPluginConfig();
        }
        event.setJoinMessage(String.format("%s joined : 残り全体ライフ: %d" , event.getPlayer().getDisplayName(), life));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        int life = Hardcore3.config.getInt("life");
        life = life - 1;
        Hardcore3.config.set("life", life);
        Hardcore3.getPlugin(Hardcore3.class).reloadPluginConfig();

        Hardcore3.whclient.send(String.format("%s が死亡 残りライフ %d", event.getEntity().getDisplayName(), life));

        if(life <= 0) {
            Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                if(!player.hasPermission("op")) {
                    player.setGameMode(GameMode.SPECTATOR);
                }
            });

            Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER,10,1);
                player.playSound(player.getLocation(),Sound.ENTITY_WOLF_HOWL,10,1);
                player.playSound(player.getLocation(),Sound.ENTITY_WITHER_SPAWN,10,1);
                player.playSound(player.getLocation(),Sound.ENTITY_WITCH_DEATH,10,1);
                player.playSound(player.getLocation(),Sound.ENTITY_LIGHTNING_BOLT_IMPACT,10,1);
                player.playSound(player.getLocation(),Sound.ENTITY_SPLASH_POTION_BREAK,10,1);
                player.playEffect(player.getLocation(), Effect.INSTANT_POTION_BREAK, 0xff0000);
                player.sendTitle(
                       "§4GAME OVER",
                        "ライフを使い果たしました。",
                        10, 200, 20);
            });
            return;
        }

        final int finalLife = life;

        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER,10,1);
            player.playSound(player.getLocation(),Sound.ENTITY_WOLF_HOWL,10,1);
            player.playSound(player.getLocation(),Sound.ENTITY_WITHER_SPAWN,10,1);
            player.playSound(player.getLocation(),Sound.ENTITY_WITCH_DEATH,10,1);
            player.playSound(player.getLocation(),Sound.ENTITY_LIGHTNING_BOLT_IMPACT,10,1);
            player.playSound(player.getLocation(),Sound.ENTITY_SPLASH_POTION_BREAK,10,1);
            player.playEffect(player.getLocation(), Effect.INSTANT_POTION_BREAK, 0xff0000);
            player.sendTitle(
                    String.format("§4%s 死亡", event.getEntity().getDisplayName()),
                    String.format("残り全体ライフ §c%d", finalLife),
                    10, 50, 20);
        });

        event.setDeathMessage(String.format("%s Dead : 残り全体ライフ: %d", event.getEntity().getDisplayName(), life));
    }
}
