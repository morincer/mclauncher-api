package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import org.junit.Before;
import org.junit.Test;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionShort;
import sk.tomsik68.mclauncher.api.versions.LatestVersionInformation;
import java.nio.file.Files;
import java.nio.file.Path;
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
    public void getVersionInfoShouldReturnNonEmptyBasicInformation() throws Exception {
        IVersion version = downloadCachedVersionList.retrieveVersionInfo("1.7.10");
        assertThat(version, notNullValue());
        assertThat(version.getId(), equalTo("1.7.10"));
    }

    @Test
    public void getVersionsListShouldReturnListOfVersionsWithBasicInfo() throws Exception {
        List<IVersionShort> versionsListShortInfo = downloadCachedVersionList.getShortInfoVersionList();

        assertThat(versionsListShortInfo.size(), is(greaterThan(0)));
        IVersionShort mcDownloadVersion = versionsListShortInfo.get(0);
        assertThat(mcDownloadVersion.getId(), notNullValue());
    }
}