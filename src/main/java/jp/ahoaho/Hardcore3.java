package jp.ahoaho;

import club.minnced.discord.webhook.WebhookClient;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Hardcore3 extends JavaPlugin {

    public static FileConfiguration config = null;
    public static WebhookClient whclient = null;

    public void reloadPluginConfig() {
        this.saveConfig();
        // this.reloadConfig();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Configなかったら作る
        saveDefaultConfig();
        this.config = getConfig();
        saveConfig();

        // イベントを登録
        this.getServer().getPluginManager().registerEvents(new EventListener(),this);

        // コマンドを登録
        getCommand("setlife").setExecutor(new CommandHandler());

        String url = config.getString("WebHook_URL");
        if(url.startsWith("https://discord.com/api/webhooks/")) {
            whclient = WebhookClient.withUrl(url);
        } else {
            throw new IllegalArgumentException("WebHook URL Not Provided");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
    }
}
