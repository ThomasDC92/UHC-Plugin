package dev.thomasdc.uhcplugin;

import dev.thomasdc.uhcplugin.commands.*;
import dev.thomasdc.uhcplugin.events.*;
import dev.thomasdc.uhcplugin.models.CustomRecipes;
import dev.thomasdc.uhcplugin.models.Kit;
import dev.thomasdc.uhcplugin.models.KitItems;
import dev.thomasdc.uhcplugin.tasks.AlwaysDay;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.FileUtil;
import org.codehaus.plexus.util.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.*;

public final class UHCPlugin extends JavaPlugin {
    //TODO ADD EPIC ITEMS
    //TODO SHADE NBT API PLUGIN INSTEAD OF PROVIDING THE JAR
    //TODO ADD CLASSES PLAYERS CAN CHOOSE
    //TODO HAVE FUN

    public static HashMap<Player, Kit> eventPlayers = new HashMap<>();
    public static List<Player> alivePlayers = new ArrayList<>();
    public static List<Player> noFallDamage = new ArrayList<>();
    public static List<Kit> kits = new ArrayList<>();
    public static int minRequiredPlayers = 0;
    public static int maxGameTimeInMinutes = 40;
    public static int grazePeriodInMinutes = 10;
    public static boolean eventActive = false;
    public FileConfiguration leaderboard;
    public FileConfiguration playerClasses;

    public static UHCPlugin getInstance() {
        return getPlugin(UHCPlugin.class);
    }

    @Override
    public void onEnable() {
        //register events
        getServer().getPluginManager().registerEvents(new OnEntityDamage(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new EventKit(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new OnFoodLevelChange(), this);
        getServer().getPluginManager().registerEvents(new OnProjectileLaunch(), this);
        getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);
        getServer().getPluginManager().registerEvents(new OnWeatherChange(), this);
        getServer().getPluginManager().registerEvents(new CustomItems(), this);


        //register commands
        getCommand("startEvent").setExecutor(new StartEvent(this));
        getCommand("stopEvent").setExecutor(new StopEvent(this));
        getCommand("addPlayer").setExecutor(new AddPlayer());
        getCommand("removePlayer").setExecutor(new RemovePlayer());
        getCommand("getPlayers").setExecutor(new GetPlayers());
        getCommand("goto").setExecutor(new GoToPlayer());
        getCommand("eventKit").setExecutor(new EventKit());
        getCommand("getCurrentKit").setExecutor(new GetCurrentKit());
        getCommand("customItems").setExecutor(new CustomItems());

        //misc
        initializeKits();
        createFiles();
        CustomRecipes customRecipes = new CustomRecipes(this);
        customRecipes.registerRecipes();
        BukkitTask task = new AlwaysDay().runTaskTimer(this,0L,0L);


        //loop
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!eventActive) {
                    updateScoreboard();
                }
            }
        }.runTaskTimer(this, 0L, 20L);
    }


    public void startEvent() {
        UHCPlugin.eventActive = true;

        World world = generateWorld();

        UHCPlugin.noFallDamage = new ArrayList<>(UHCPlugin.eventPlayers.keySet());
        UHCPlugin.alivePlayers = new ArrayList<>(UHCPlugin.eventPlayers.keySet());

        for (Player p : UHCPlugin.eventPlayers.keySet()) {
            p.teleport(getSpawnLocation(world));
            p.addPotionEffect(PotionEffectType.SPEED.createEffect(grazePeriodInMinutes * 60 * 20, 1));
            p.addPotionEffect(PotionEffectType.FAST_DIGGING.createEffect(grazePeriodInMinutes * 60 * 20, 1));
            p.setAbsorptionAmount(20.0);
            p.setExp(0);
            p.setLevel(0);
//            CustomRecipes.keys.forEach(key -> {
//                p.discoverRecipe(new NamespacedKey(this, key));
//            });
            kits.clear();
            initializeKits();
            Iterator<Recipe> recipes = Bukkit.recipeIterator();
            while (recipes.hasNext()) {
                Recipe recipe = recipes.next();
                if(recipe instanceof Keyed){
                    p.undiscoverRecipe(((Keyed) recipe).getKey());
                }
            }
            for (String key: CustomRecipes.keys) {
                p.discoverRecipe(new NamespacedKey(this, key));

            }
            if (eventPlayers.get(p) != null) {
                for (ItemStack item : eventPlayers.get(p).getItems()) {
                    p.getInventory().addItem(item);
                }
            }
        }

        new BukkitRunnable() {
            int timeLeft = 60 * maxGameTimeInMinutes;
            int grazeTime = 60 * grazePeriodInMinutes;

            @Override
            public void run() {
                //game logic
                timeLeft--;
                if (grazeTime > 0) {
                    grazeTime--;
                }
                if (grazeTime == 30) {
                    for (Player p : alivePlayers) {
                        p.sendMessage("Graze period ends in 30 seconds!");
                    }
                } else if (grazeTime == 10) {
                    for (Player p : alivePlayers) {
                        p.sendMessage("Graze period ends in 10 seconds!");
                    }
                } else if (grazeTime < 10 && grazeTime > 0) {
                    for (Player p : alivePlayers) {
                        p.sendMessage("Graze period ends in " + grazeTime + " seconds!");
                    }
                } else if (grazeTime == 0) {
                    grazeTime = -1;
                    world.setPVP(true);
                    world.getWorldBorder().setSize(100, (maxGameTimeInMinutes - grazePeriodInMinutes) * 60L * 20);
                    for (Player p : alivePlayers) {
                        p.sendMessage("Graze period has ended, pvp is now enabled and the borders have started moving toward 0,0!");
                        p.setAbsorptionAmount(0);
                    }
                }
                System.out.println("graze: " + grazeTime);

                if (!eventActive) {
                    Bukkit.broadcastMessage("Event has been forcibly ended by an admin!");
                    System.out.println("game ended by command");
                    endEvent();
                    this.cancel();
                }

//                if (alivePlayers.size() == 1) {
//                    Bukkit.broadcastMessage("Game over! " + alivePlayers.get(0).getName() + " won!");
//                    System.out.println("game ended by player win");
//                    leaderboard.set(alivePlayers.get(0).getUniqueId().toString(), leaderboard.getInt(alivePlayers.get(0).getUniqueId().toString()) + 1);
//                    endEvent();
//                    this.cancel();
//                }

                if (timeLeft <= 0) {
                    Bukkit.broadcastMessage("Time ran out!");
                    System.out.println("game ended by time");
                    endEvent();
                    this.cancel();
                }


                //update scoreboard
                updateScoreboard(timeLeft);

            }
        }.runTaskTimer(this, 0L, 20L);

    }


    public World generateWorld() {
        World world = WorldCreator.name("uhc_world")
                .biomeProvider(
                        new BiomeProvider(){
                            @Override
                            public @NotNull Biome getBiome(@NotNull WorldInfo worldInfo, int i, int i1, int i2) {
                                List<Biome> biomes = List.of(
                                        Biome.PLAINS,
                                        Biome.DARK_FOREST,
                                        Biome.CHERRY_GROVE,
                                        Biome.FOREST,
                                        Biome.SWAMP,
                                        Biome.SAVANNA,
                                        Biome.FLOWER_FOREST
                                );
                                Random random = new Random();
                                return biomes.get(random.nextInt(biomes.size()));
                            }

                            @Override
                            public @NotNull List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
                                List<Biome> biomes = List.of(
                                        Biome.PLAINS,
                                        Biome.DARK_FOREST,
                                        Biome.CHERRY_GROVE,
                                        Biome.FOREST,
                                        Biome.SWAMP,
                                        Biome.SAVANNA,
                                        Biome.FLOWER_FOREST
                                );
                                return List.of(biomes.get(new Random().nextInt(biomes.size())));
                            }
                        }
                )
                .createWorld();
        world.getWorldBorder().setSize(500);
        world.getWorldBorder().setCenter(0, 0);
        world.setPVP(false);
        world.setTime(0);
        return world;
    }

    public Location getSpawnLocation(World world) {
        int x = (int) (Math.random() * 250) - 125;
        int z = (int) (Math.random() * 250) - 125;
        return new Location(world, x, 100, z);
    }


    public void endEvent() {
        UHCPlugin.eventActive = false;
        World world = Bukkit.getServer().getWorld("uhc_world");

        for (Player p : eventPlayers.keySet()) {
            for (PotionEffect effect : p.getActivePotionEffects()) {
                p.removePotionEffect(effect.getType());
            }
            Location loc = p.getServer().getWorlds().get(0).getSpawnLocation();
            p.teleport(loc);
            p.getInventory().clear();
            p.setGameMode(GameMode.SURVIVAL);
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            p.spigot().respawn();
            p.setAbsorptionAmount(0);
        }
        Bukkit.getServer().unloadWorld(world, false);
        File file = new File("uhc_world");
        if (file.exists()) {
            try {
                FileUtils.deleteDirectory(file);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        UHCPlugin.eventPlayers.clear();
        UHCPlugin.alivePlayers.clear();
        UHCPlugin.noFallDamage.clear();
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            leaderboard.save(new File(getDataFolder(), "leaderboard.yml"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public void updateScoreboard() {
        for (Player p : getServer().getOnlinePlayers()) {
            Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective obj = board.registerNewObjective("UHC", "dummy", ChatColor.RED + "UHC");
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            List<String> topPlayers = new ArrayList<>();
            for (String key : leaderboard.getKeys(false)) {
                topPlayers.add(key);
            }
            topPlayers.sort((o1, o2) -> leaderboard.getInt(o2) - leaderboard.getInt(o1));
            for (int i = 0; i < 5 && i < topPlayers.size(); i++) {
                obj.getScore(ChatColor.GRAY.toString() + (i + 1) + ". " + ChatColor.WHITE.toString() + Bukkit.getOfflinePlayer(UUID.fromString(topPlayers.get(i))).getName() + ": " + ChatColor.GOLD.toString() + leaderboard.getInt(topPlayers.get(i))).setScore(6 - i);
            }
            p.setScoreboard(board);
        }
    }


    public void updateScoreboard(int timeLeft) {
        for (Player p : eventPlayers.keySet()) {
            Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective obj = board.registerNewObjective("UHC", "dummy", ChatColor.RED + "UHC");
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            obj.getScore(ChatColor.GRAY + "Time left: " + ChatColor.WHITE + timeLeft / 60 + ":" + (timeLeft % 60 < 10 ? "0" + timeLeft % 60 : timeLeft % 60)).setScore(2);
            obj.getScore(ChatColor.GRAY + "Players: " + ChatColor.WHITE + alivePlayers.size() + "/" + eventPlayers.size()).setScore(1);
            p.setScoreboard(board);

        }
    }

    public void initializeKits() {
        KitItems kitItems = new KitItems();
        kits.add(Kit.builder()
                .name("Gatherer")
                .icon(generateIcon("Gatherer", Material.COBBLESTONE, ChatColor.GRAY))
                .items(List.of(
                        new ItemStack(Material.STONE_SWORD),
                        new ItemStack(Material.STONE_PICKAXE),
                        new ItemStack(Material.STONE_AXE),
                        new ItemStack(Material.STONE_SHOVEL)
                ))
                .build());

        kits.add(Kit.builder().name("Enderman")
                .icon(generateIcon("Enderman", Material.ENDER_PEARL, ChatColor.DARK_PURPLE))
                .items(List.of(
                        new ItemStack(Material.ENDER_PEARL, (int)(Math.random()*4)+4)
                ))
                .build());




        kits.add(Kit.builder()
                .name("Fishboy")
                .icon(generateIcon("Fishboy", Material.FISHING_ROD, ChatColor.BLUE))
                .items(List.of(kitItems.FISHBOYS_ROD)).build());



        kits.add(Kit.builder()
                .name("Wizard's basement dweller")
                .icon(generateIcon("Wizard's basement dweller", Material.BREWING_STAND, ChatColor.RED))
                .items(List.of(
                        kitItems.KETAMINE
                )).build());

    }


    public ItemStack generateIcon(String name, Material item, ChatColor color) {
        if (color == null) {
            color = ChatColor.WHITE;
        }
        ItemStack icon = new ItemStack(item);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName(color + name);
        icon.setItemMeta(meta);

        return icon;
    }

    public void createFiles() {
        File leaderBoardFile = new File(this.getDataFolder(), "leaderboard.yml");
        File classesFile = new File(this.getDataFolder(), "playerclasses.yml");
        leaderboard = YamlConfiguration.loadConfiguration(leaderBoardFile);
        playerClasses = YamlConfiguration.loadConfiguration(classesFile);
    }
}
