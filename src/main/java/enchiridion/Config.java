package enchiridion;

import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
import enchiridion.api.GuideHandler;


public class Config {
	public static void init(Configuration config) {
		try {
			config.load();
            Enchiridion.id = config.getItem("Enchiridion Items", 28999).getInt();
			TooltipHandler.PRINT = config.get("Settings", "Print Item Codes to the Console", false).getBoolean(false);
			GuideHandler.DEBUG_ENABLED = config.get("Settings", "Debug Mode Enabled", false).getBoolean(false);
		} catch (Exception e) {
            BookLogHandler.log(Level.WARNING, "Failed to load Enchiridion config correctly");
            e.printStackTrace();
		} finally {
			config.save();
		}
	}
}
