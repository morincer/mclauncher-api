package sk.tomsik68.mclauncher.api.forge;

import java.util.Dictionary;

/**
 * Holds data for MC-version specific packs of Forge - listing the target Minecraft version, the recommended and the
 * latest versions of the Forge as well as the download locations
 */
public interface IForgeVersionPackInfo {
    String getMinecraftVersion();
    String getRecommendedForgeVersion();
    String getLatestForgeVersion();
    Dictionary<String, IForgeVersion> getForgeVersions();
}
