package sk.tomsik68.mclauncher.api.forge;

public interface IForgeAsset {
    /**
     * @return Type of the package (zip, txt, jar, exe)
     */
    String getPackageType();

    /**
     * @return Type of the contents (mdk, changelog, universal, userdev, installer-win, installer)
     */
    String getType();

    /**
     * @return Hash code of the asset
     */
    String getHash();

    String getMinecraftVersion();

    String getForgeVersion();

    IForgeDownloader getDownloader();

}
