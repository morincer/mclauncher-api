package sk.tomsik68.mclauncher.impl.forge;

import ch.qos.logback.core.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import utils.DummyProgressMonitor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class ForgeDownloaderTest {

    private Path targetDir;

    @Before
    public void setUp() throws Exception {
        this.targetDir = Paths.get("testmc/");
        if (Files.exists(targetDir)) {
            FileUtils.deleteDirectory(targetDir.toFile());
        }
    }

    @Test
    public void shouldDownloadTheRequestedForgeAsset() throws Exception {
        ForgeAsset asset = new ForgeAsset();
        asset.setDownloader(new ForgeDownloader());
        asset.setHash("trtrt");
        asset.setType("changelog");
        asset.setPackageType("txt");
        asset.setForgeVersion("14.23.1.2556");
        asset.setMinecraftVersion("1.12.2");

        Path path = asset.getDownloader().download(asset, targetDir, new DummyProgressMonitor());

        assertTrue(Files.exists(path));

    }
}