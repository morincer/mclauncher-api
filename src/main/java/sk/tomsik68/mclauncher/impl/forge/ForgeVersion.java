package sk.tomsik68.mclauncher.impl.forge;

import sk.tomsik68.mclauncher.api.forge.IForgeAsset;
import sk.tomsik68.mclauncher.api.forge.IForgeVersion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ForgeVersion implements IForgeVersion {

    private String minecraftVersion, forgeVersion, forgeBuildNumber;
    Date modified;
    private List<IForgeAsset> assets;

    public ForgeVersion() {
        assets = new ArrayList<>();
    }

    @Override
    public String getMinecraftVersion() {
        return this.minecraftVersion;
    }

    @Override
    public String getForgeVersion() {
        return this.forgeVersion;
    }

    @Override
    public Date getModified() {
        return this.modified;
    }

    @Override
    public List<IForgeAsset> getAssets() {
        return this.assets;
    }

    public void setMinecraftVersion(String minecraftVersion) {
        this.minecraftVersion = minecraftVersion;
    }

    public void setForgeVersion(String forgeVersion) {
        this.forgeVersion = forgeVersion;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public void setAssets(List<IForgeAsset> assets) {
        this.assets = assets;
    }

    @Override
    public String getForgeBuildNumber() {
        return forgeBuildNumber;
    }

    public void setForgeBuildNumber(String forgeBuildNumber) {
        this.forgeBuildNumber = forgeBuildNumber;
    }
}
