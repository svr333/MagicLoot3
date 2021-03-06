package me.mrCookieSlime.MagicLoot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;

import org.bukkit.Location;

public class RuinBuilder {
	
	public static List<Schematic> schematics = new ArrayList<Schematic>();
	public static Map<String, Config> configs = new HashMap<String, Config>();
	public static List<Schematic> buildings = new ArrayList<Schematic>();
	
	public static void loadRuins() throws IOException {
		for (File file: new File("plugins/MagicLoot/schematics").listFiles()) {
			if (file.getName().endsWith(".schematic")) {
				schematics.add(Schematic.loadSchematic(file));
				Config cfg = new Config("plugins/MagicLoot/ruin_settings/" + file.getName().replace(".schematic", ".yml"));
				cfg.setDefaultValue("y-offset", 0);
				cfg.setDefaultValue("underwater", false);
				cfg.save();
				configs.put(file.getName().replace(".schematic", ""), cfg);
			}
		}
		for (File file: new File("plugins/MagicLoot/buildings").listFiles()) {
			if (file.getName().endsWith(".schematic")) buildings.add(Schematic.loadSchematic(file));
		}
	}
	
	public static void buildRuin(Location l) {
		Schematic s = schematics.get(CSCoreLib.randomizer().nextInt(schematics.size()));
		if (CSCoreLib.randomizer().nextInt(100) < 4) {
			s = buildings.get(CSCoreLib.randomizer().nextInt(buildings.size()));
			if (main.cfg.getBoolean("options.log")) System.out.println("Generated \"" + s.getName() + "\"");
			if (l.getBlock().isLiquid()) return;
			Schematic.pasteSchematic(l, s, false);
		}
		else {
			if (main.cfg.getBoolean("options.log")) System.out.println("Generated \"" + s.getName() + "\"");
			Config cfg = configs.get(s.getName());
			if (l.getBlock().isLiquid() && !cfg.getBoolean("underwater")) return;
			l.setY(l.getY() + cfg.getInt("y-offset"));
			Schematic.pasteSchematic(l, s, true);
		}
	}

}
