package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONValue;
import org.junit.Before;
import org.junit.Test;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionShort;
import sk.tomsik68.mclauncher.api.versions.LatestVersionInformation;
import sk.tomsik68.mclauncher.util.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class MCDownloadCachedVersionListTest {

    private MCDownloadCachedVersionList downloadCachedVersionList;
    private Path versionsListFile;

    @Before
    public void setUp() throws Exception {
        this.downloadCachedVersionList = new MCDownloadCachedVersionList("testmc/versions");
        this.versionsListFile = downloadCachedVersionList.getVersionsListFile();
        org.apache.commons.io.FileUtils.deleteDirectory(this.versionsListFile.getParent().toFile());
    }

    @Test
    public void shouldDownloadRemoteListToTheLocalDirectoryIfNotExists() throws Exception {
        downloadCachedVersionList.startDownload();
        assertTrue(Files.exists(versionsListFile));

        // Check that it is not overwritten

        assertThat(versionsListFile.toFile().length(), is(greaterThan(0l)));
        Files.write(versionsListFile, new byte[] {});
        assertThat(versionsListFile.toFile().length(), equalTo(0l));

        downloadCachedVersionList.startDownload();
        assertThat(versionsListFile.toFile().length(), equalTo(0l));
    }

    @Test
    public void shouldReturnValidLatestVersionInformation() throws Exception {
        LatestVersionInformation latestVersionInformation = downloadCachedVersionList.getLatestVersionInformation();
        assertThat(latestVersionInformation, notNullValue());
        assertThat(latestVersionInformation.getLatestRelease(), not(isEmptyOrNullString()));
        assertThat(latestVersionInformation.getLatestSnapshot(), not(isEmptyOrNullString()));
    }

    @Test
    public void retrieveVersionInfoShouldReturnVersionInformation() throws Exception {
        IVersion version = downloadCachedVersionList.retrieveVersionInfo("1.7.10");
        assertThat(version, notNullValue());
        assertThat(version.getId(), equalTo("1.7.10"));
        assertThat(version.getLauncher(), notNullValue());
    }

    @Test
    public void retrieveVersionInfoShouldReturnVersionIfItWasAlreadyDownloaded() throws Exception {
        assertThat(downloadCachedVersionList.getVersionsCache(), nullValue());

        // create a special hand-crafted version file
        Path versionsDir = downloadCachedVersionList.getVersionsDirectory();
        Path gromozekaVersionPath = Paths.get(versionsDir.toString(), "gromozeka", "gromozeka.json");
        Files.deleteIfExists(gromozekaVersionPath);
        Files.createDirectories(gromozekaVersionPath.getParent());
        HashMap<String,String> values = new HashMap<>();
        values.put("id", "gromozeka");
        values.put("time", "0");
        values.put("releaseTime", "0");
        values.put("type", "0");

        String s = JSONValue.toJSONString(values);
        Files.write(gromozekaVersionPath, s.getBytes());

        IVersion gromozekaVersion = downloadCachedVersionList.retrieveVersionInfo("gromozeka");
        assertThat(downloadCachedVersionList.getVersionsCache(), nullValue());
        assertThat(gromozekaVersion, notNullValue());
        assertThat(gromozekaVersion.getId(), equalTo("gromozeka"));
    }

    @Test
    public void getVersionsListShouldReturnListOfVersionsWithBasicInfo() throws Exception {
        List<IVersionShort> versionsListShortInfo = downloadCachedVersionList.getShortInfoVersionList();

        assertThat(versionsListShortInfo.size(), is(greaterThan(0)));
        IVersionShort mcDownloadVersion = versionsListShortInfo.get(0);
        assertThat(mcDownloadVersion.getId(), notNullValue());
    }
}