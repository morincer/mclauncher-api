package sk.tomsik68.mclauncher.impl.forge;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import sk.tomsik68.mclauncher.api.forge.IForgeAsset;
import sk.tomsik68.mclauncher.api.forge.IForgeDownloader;
import sk.tomsik68.mclauncher.api.forge.IForgeVersion;
import sk.tomsik68.mclauncher.api.forge.IForgeVersionPackInfo;
import utils.DummyProgressMonitor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Dictionary;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ForgeVersionPackProviderTest {

    private ForgeVersionPackProvider forgeVersionPackProvider;

    private Path targetDir;

    @Before
    public void setUp() throws Exception {
        this.forgeVersionPackProvider = new ForgeVersionPackProvider();
        this.targetDir = Paths.get("testmc/");
        if (Files.exists(targetDir)) {
            FileUtils.deleteDirectory(targetDir.toFile());
        }
    }

    @Test
    public void getForgeVersionPackShouldRetrieveTheValidForgeVersions() throws Exception {
        Dictionary<String, IForgeVersionPackInfo> forgeVersionPack = this.forgeVersionPackProvider.getForgeVersionPacks();
        assertThat(forgeVersionPack, notNullValue());

        IForgeVersionPackInfo versionPackInfo = forgeVersionPack.get("1.7.10");
        assertThat(versionPackInfo, notNullValue());

        assertThat(versionPackInfo.getMinecraftVersion(), equalTo("1.7.10"));
        assertThat(versionPackInfo.getForgeVersions().size(), greaterThan(3));
        assertThat(versionPackInfo.getLatestForgeVersion(), equalTo("1614"));
        assertThat(versionPackInfo.getRecommendedForgeVersion(), equalTo("1558"));

        IForgeVersion recommendedForgeVersion = versionPackInfo.getForgeVersions().get("1558");
        assertThat(recommendedForgeVersion, notNullValue());
        assertThat(recommendedForgeVersion.getForgeBuildNumber(), equalTo("1558"));
        assertThat(recommendedForgeVersion.getForgeVersion(), equalTo("10.13.4.1558"));
        assertThat(recommendedForgeVersion.getMinecraftVersion(), equalTo("1.7.10"));
        assertThat(recommendedForgeVersion.getModified(), equalTo(new Date(1447170240)));

        IForgeAsset jarInstallerAsset = null;

        for (IForgeAsset asset : recommendedForgeVersion.getAssets()) {
            if (asset.getPackageType().equals("jar") && asset.getType().equals("installer")) {
                jarInstallerAsset = asset;
                break;
            }
        }

        assertThat(jarInstallerAsset, notNullValue());
        assertThat(jarInstallerAsset.getHash(), notNullValue());

        IForgeDownloader downloader = jarInstallerAsset.getDownloader();
        assertThat(downloader, notNullValue());

        Path download = downloader.download(jarInstallerAsset, targetDir, new DummyProgressMonitor());
        assertTrue(Files.exists(download));

    }
}