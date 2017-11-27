package sk.tomsik68.mclauncher.impl.forge;

import sk.tomsik68.mclauncher.api.forge.IForgeVersion;
import sk.tomsik68.mclauncher.api.forge.IForgeVersionPackInfo;

import java.util.Dictionary;
import java.util.Hashtable;

public class ForgeVersionPackInfo implements IForgeVersionPackInfo {

    private String minecraftVersion, recommendedForgeVersion, latestForgeVersion;
    Dictionary<String, IForgeVersion> forgeVersions;

    public ForgeVersionPackInfo() {
        forgeVersions = new Hashtable<>();
    }

    @Override
    public String getMinecraftVersion() {
        return this.minecraftVersion;
    }

    @Override
    public String getRecommendedForgeVersion() {
        return this.recommendedForgeVersion;
    }

    @Override
    public String getLatestForgeVersion() {
        return this.latestForgeVersion;
    }

    @Override
    public Dictionary<String, IForgeVersion> getForgeVersions() {
        return this.forgeVersions;
    }

    public void setMinecraftVersion(String minecraftVersion) {
        this.minecraftVersion = minecraftVersion;
    }

    public void setRecommendedForgeVersion(String recommendedForgeVersion) {
        this.recommendedForgeVersion = recommendedForgeVersion;
    }

    public void setLatestForgeVersion(String latestForgeVersion) {
        this.latestForgeVersion = latestForgeVersion;
    }

    public void setForgeVersions(Dictionary<String, IForgeVersion> forgeVersions) {
        this.forgeVersions = forgeVersions;
    }
}
