package sk.tomsik68.mclauncher.impl.forge;

import sk.tomsik68.mclauncher.api.forge.IForgeAsset;
import sk.tomsik68.mclauncher.api.forge.IForgeDownloader;

public class ForgeAsset implements IForgeAsset {

    private String packageType, type, hash, minecraftVersion, forgeVersion;
    private IForgeDownloader downloader;

    @Override
    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public IForgeDownloader getDownloader() {
        return downloader;
    }

    public void setDownloader(IForgeDownloader downloader) {
        this.downloader = downloader;
    }

    @Override
    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    public void setMinecraftVersion(String minecraftVersion) {
        this.minecraftVersion = minecraftVersion;
    }

    @Override
    public String getForgeVersion() {
        return forgeVersion;
    }

    public void setForgeVersion(String forgeVersion) {
        this.forgeVersion = forgeVersion;
    }
}
