package sk.tomsik68.mclauncher.api.forge;

import java.util.Date;
import java.util.List;

public interface IForgeVersion {
    String getMinecraftVersion();
    String getForgeVersion();
    String getForgeBuildNumber();
    Date getModified();
    List<IForgeAsset> getAssets();
}
