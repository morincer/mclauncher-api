package sk.tomsik68.mclauncher.impl.forge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.tomsik68.mclauncher.api.forge.IForgeAsset;
import sk.tomsik68.mclauncher.api.forge.IForgeDownloader;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.util.FileUtils;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ForgeDownloader implements IForgeDownloader {

    private String[] urlTemplates = new String[]  {
            "http://files.minecraftforge.net/maven/net/minecraftforge/forge/<MCVERSION>-<FORGE_VERSION>/forge-<MCVERSION>-<FORGE_VERSION>-<TYPE>.<PACKAGE_TYPE>",
            "http://files.minecraftforge.net/maven/net/minecraftforge/forge/<MCVERSION>-<FORGE_VERSION>-<MCVERSION>/forge-<MCVERSION>-<FORGE_VERSION>-<MCVERSION>-<TYPE>.<PACKAGE_TYPE>"
    };

    private static final Logger log = LoggerFactory.getLogger(ForgeDownloader.class);

    @Override
    public Path download(IForgeAsset asset, Path baseDirectory, IProgressMonitor progressMonitor) throws Exception {
        for (String urlTemplate : urlTemplates) {
            URL url = new URL(buildUrl(urlTemplate, asset));
            String fileName = Paths.get(url.getFile()).getFileName().toString();
            Path target = Paths.get(baseDirectory.toAbsolutePath().toString(), fileName);

            log.info("Downloading Forge from " + url + " to " + target);
            try {
                FileUtils.downloadFileWithProgress(url.toString(), target.toFile(), progressMonitor);
                return target;
            } catch (FileNotFoundException e) {
                log.warn("Failed to find asset at " + url);
            }
        }

        throw new FileNotFoundException("The asset " + asset.getMinecraftVersion() + "-" + asset.getForgeVersion() + " not found in either of locations");
    }

    private String buildUrl(String urlTemplate, IForgeAsset asset) {
        String result = urlTemplate
                .replace("<MCVERSION>", asset.getMinecraftVersion())
                .replace("<FORGE_VERSION>", asset.getForgeVersion())
                .replace("<TYPE>", asset.getType())
                .replace("<PACKAGE_TYPE>", asset.getPackageType());

        return result;
    }
}
