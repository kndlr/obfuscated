package com.coinkr.obfuscated.obfuscated;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public final class Obfuscated extends JavaPlugin {
    public int delay = 1;
    public int step = 0;
    public boolean enable = true;

    List<String> allowedItems;

    Material[] potionItems = {Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION, Material.TIPPED_ARROW};

    String[] colorBlockNames = new String[] {
            "WOOL", "STAINED_GLASS", "STAINED_GLASS_PANE", "CARPET", "TERRACOTTA", "GLAZED_TERRACOTTA",
            "BED", "SHULKER_BOX", "CONCRETE", "CONCRETE_POWDER", "BANNER", "DYE"
    };
    String[] colors = new String[] {
            "WHITE", "ORANGE", "MAGENTA", "LIGHT_BLUE", "YELLOW", "LIME", "PINK", "GRAY", "LIGHT_GRAY",
            "CYAN", "PURPLE", "BLUE", "BROWN", "GREEN", "RED", "BLACK"
    };

    List<Material> spawnEggs = Arrays.stream(Material.values())
            .filter(m -> m.name().endsWith("SPAWN_EGG"))
            .collect(Collectors.toList());

    List<Material> musicDiscs = Arrays.stream(Material.values())
            .filter(m -> m.name().startsWith("MUSIC_DISC"))
            .collect(Collectors.toList());

    public void configAutoFill() {
        if (getConfig().contains("enable")) enable = getConfig().getBoolean("enable");
        if (getConfig().contains("delay")) {
            delay = getConfig().getInt("delay");
            if (delay < 1) {
                delay = 1;
            }
        }

        if (getConfig().contains("allowed-items")) {
            if (getConfig().getStringList("allowed-items").contains("all")) {
                 allowedItems = Arrays.asList(
                        "wool", "stained_glass", "stained_glass_pane", "carpet", "terracotta", "glazed_terracotta",
                        "bed", "shulker_box", "concrete", "concrete_powder", "banner", "dye",
                        "spawn_egg", "potion", "splash_potion", "lingering_potion", "tipped_arrow", "enchanted_book", "music_disc"
                 );
            } else {
                allowedItems = getConfig().getStringList("allowed-items");
            }
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        getLogger().info("Plugin Enabled");

        saveDefaultConfig();

        configAutoFill();

        this.getCommand("o").setExecutor(new CommandEvent(this));
        
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                if (getConfig().getBoolean("enable")) {
                    if (step % getConfig().getInt("delay") == 0) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            for (int i = 0; i <= 8; i++) {
                                ItemStack item = p.getInventory().getItem(i);
                                if (item != null) {
                                    if (allowedItems.contains("spawn_egg") && item.getType().name().endsWith("SPAWN_EGG")) {
                                        // SPAWN EGGS
                                        ItemStack random = new ItemStack(spawnEggs.get(
                                                new Random().nextInt(spawnEggs.size())
                                        ));
                                        random.setAmount(item.getAmount());

                                        p.getInventory().setItem(i, random);
                                    } else if (ArrayUtils.contains(potionItems, item.getType())) {
                                        // POTIONS / SPLASH POTIONS / LINGERING POTIONS / TIPPED ARROWS
                                        if (!allowedItems.contains(item.getType().name().toLowerCase())) continue;
                                        ItemStack random = new ItemStack(item.getType());
                                        PotionType randomType = PotionType.values()[new Random().nextInt(PotionType.values().length)];
                                        PotionMeta meta = (PotionMeta) random.getItemMeta();
                                        meta.setBasePotionData(new PotionData(randomType));
                                        random.setItemMeta(meta);
                                        random.setAmount(item.getAmount());
                                        p.getInventory().setItem(i, random);

                                    } else if (allowedItems.contains("enchanted_book") && item.getType().name().equals("ENCHANTED_BOOK")) {
                                        // ENCHANTED BOOKS
                                        ItemStack random = new ItemStack(Material.ENCHANTED_BOOK);
                                        Enchantment randomType = Enchantment.values()[new Random().nextInt(Enchantment.values().length)];
                                        int min = randomType.getStartLevel();
                                        int max = randomType.getMaxLevel();
                                        int randomLevel = new Random().nextInt(max - min + 1) + min;
                                        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) random.getItemMeta();
                                        meta.addStoredEnchant(randomType, randomLevel, true);
                                        random.setItemMeta(meta);
                                        p.getInventory().setItem(i, random);

                                    } else if (allowedItems.contains("music_disc") && item.getType().name().startsWith("MUSIC_DISC")) {
                                        // MUSIC DISCS
                                        ItemStack random = new ItemStack(musicDiscs.get(
                                                new Random().nextInt(musicDiscs.size())
                                        ));
                                        random.setAmount(item.getAmount());

                                        p.getInventory().setItem(i, random);
                                    } else {
                                        // COLORED ITEMS
                                        String name = item.getType().name();
                                        List<String> colored = allowedItems.stream()
                                                .filter(a -> name.toLowerCase().endsWith(a))
                                                .collect(Collectors.toList());
                                        if (colored.size() == 0) continue;
                                        for (String colorBlock : colorBlockNames) {
                                            if (name.endsWith(colorBlock)) {
                                                String color = colors[new Random().nextInt(colors.length)];

                                                ItemStack random = new ItemStack(
                                                        Material.getMaterial(color + "_" + colorBlock)
                                                );

                                                random.setAmount(item.getAmount());

                                                p.getInventory().setItem(i, random);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        step = 0;
                    }
                    step++;
                }
            }
        }, 1, 1);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        getLogger().info("Plugin Disabled");
    }
}


class CommandEvent implements CommandExecutor {
    Obfuscated plugin;

    public CommandEvent(Obfuscated plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("o")) {
            if (sender.hasPermission("obfuscated.admin")) {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.RED + "사용법: /o <delay/enable/disable/reload>");
                    return false;
                }
                if (args[0].equalsIgnoreCase("delay")) {
                    if (args.length < 2) {
                        sender.sendMessage(ChatColor.RED + "사용법: /o delay <틱:자연수>");
                        return false;
                    }
                    int delay;
                    try {
                        delay = Integer.parseInt(args[1]);
                        if (delay <= 0) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ChatColor.RED + "자연수로 입력하세요\n사용법: /o delay <틱:자연수>");
                        return false;
                    }
                    sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[Obfuscated]" + ChatColor.RESET + " 지연 시간이 " + ChatColor.GOLD + Integer.toString(delay) + ChatColor.RESET + "틱으로 변경되었습니다.");
                    plugin.delay = delay;
                    plugin.getConfig().set("delay", delay);
                    plugin.saveConfig();
                } else if (args[0].equalsIgnoreCase("enable")) {
                    sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[Obfuscated]" + ChatColor.RESET + " 플러그인이 " + ChatColor.GOLD + "활성화" + ChatColor.RESET + "되었습니다.");
                    plugin.enable = true;
                    plugin.step = 0;
                    plugin.getConfig().set("enable", true);
                    plugin.saveConfig();
                } else if (args[0].equalsIgnoreCase("disable")) {
                    sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[Obfuscated]" + ChatColor.RESET + " 플러그인이 " + ChatColor.GOLD + "비활성화" + ChatColor.RESET + "되었습니다.");
                    plugin.enable = false;
                    plugin.step = 0;
                    plugin.getConfig().set("enable", false);
                    plugin.saveConfig();
                } else if (args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "[Obfuscated]" + ChatColor.RESET + " 플러그인이 " + ChatColor.GOLD + "리로드" + ChatColor.RESET + "되었습니다.");
                    plugin.reloadConfig();
                    plugin.saveConfig();
                    plugin.saveDefaultConfig();
                    plugin.configAutoFill();
                }
            } else {
                sender.sendMessage(ChatColor.RED + "명령어를 사용할 권한이 없습니다.");
            }
        }
        return false;
    }
}