package sk.tomsik68.mclauncher.api.forge;

import java.nio.file.Path;

/**
 * Downloads the Forge asset
 */
public interface IForgeDownloader {

    /**
     * Downloads the specified Forge asset to the given directory
     * @param asset Forge Asset to download
     * @param baseDirectory The directory to download to
     * @return Path to the downloaded file
     */
    Path download(IForgeAsset asset, Path baseDirectory);
}
