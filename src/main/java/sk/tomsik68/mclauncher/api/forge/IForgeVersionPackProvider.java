package sk.tomsik68.mclauncher.api.forge;

import java.util.Dictionary;

public interface IForgeVersionPackProvider {
    public Dictionary<String, IForgeVersionPackInfo> getForgeVersionPacks() throws Exception;
}
