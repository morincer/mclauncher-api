package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.slf4j.Logger;
import sk.tomsik68.mclauncher.api.common.IObserver;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.versions.*;
import sk.tomsik68.mclauncher.util.HttpUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MCDownloadCachedVersionList implements ICachedVersionList, IShortInfoVersionList {

    public static final String VERSIONS_LIST_FILENAME = "versions.json";
    public static final String VERSION_MANIFEST_URL = "https://launchermeta.mojang.com/mc/game/version_manifest.json";

    private final String manifestUrl;

    private static final Logger log = MCLauncherAPI.log;

    private Path versionsListFile;

    private Dictionary<String, IVersionShort> versionsCache;

    public MCDownloadCachedVersionList(String cacheLocationDir) {
        this(cacheLocationDir, VERSION_MANIFEST_URL);
    }

    /**
     * Creates a new instance allowing to specify both the cache directory and manifest URL
     * @param cacheLocationDir - the directory to store the cached JSON
     * @param versionManifestUrl - the URL of the version_manifest.json
     */
    public MCDownloadCachedVersionList(String cacheLocationDir, String versionManifestUrl) {
        this.versionsListFile = Paths.get(cacheLocationDir, VERSIONS_LIST_FILENAME).toAbsolutePath();
        this.manifestUrl = versionManifestUrl;
    }

    @Override
    public void startDownload() throws Exception {
        if (Files.exists(this.versionsListFile)) {
            log.debug("Skipping download - file already exists at " + versionsListFile);
            return;
        }

        ensureVersionInfoManifest();
    }

    private void ensureVersionInfoManifest() throws Exception {
        if (Files.exists(this.versionsListFile)) {
            return;
        } else {
            Files.createDirectories(this.versionsListFile.getParent());
            // at first, we download the complete version list
            String jsonString = HttpUtils.httpGet(manifestUrl);
            Files.write(this.versionsListFile, jsonString.getBytes());
        }
    }

    @Override
    public void setCacheDirectory(Path cacheDirectory) {
        this.versionsListFile = Paths.get(cacheDirectory.toAbsolutePath().toString(), VERSIONS_LIST_FILENAME);
    }

    /**
     * Invalidates the caches making subsequent calls for data go for the online data sources
     * @throws IOException
     */
    public void invalidateCache() throws IOException {
        Files.deleteIfExists(this.versionsListFile);
        versionsCache = null;
    }

    @Override
    public IVersion retrieveVersionInfo(String id) throws Exception {
        ensureVersionsCache();

        IVersionShort mcDownloadVersion = versionsCache.get(id);
        if (mcDownloadVersion == null || mcDownloadVersion.getUrl() == null) throw new Exception("Failed to find URL for the version " + id);

        String fullVersionJSONString = HttpUtils.httpGet(mcDownloadVersion.getUrl());
        JSONObject fullVersionObject = (JSONObject) JSONValue.parse(fullVersionJSONString);
        // ,create a MCDownloadVersion based on it
        MCDownloadVersion version = new MCDownloadVersion(fullVersionObject);
        return version;
    }

    private void ensureVersionsCache() throws Exception {
        if (versionsCache == null) {
            ensureVersionInfoManifest();

            versionsCache = new Hashtable<>();
            JSONObject manifest = (JSONObject) JSONValue.parse(Files.readAllBytes(versionsListFile));
            JSONArray versions = (JSONArray) manifest.get("versions");
            for (Object object : versions) {
                JSONObject versionInfo = (JSONObject) object;
                MCDownloadVersion mcDownloadVersion = new MCDownloadVersion(versionInfo);
                versionsCache.put(mcDownloadVersion.getId(), mcDownloadVersion);
            }
        }
    }


    @Override
    public LatestVersionInformation getLatestVersionInformation() throws Exception {
        ensureVersionInfoManifest();
        JSONObject manifest = (JSONObject) JSONValue.parse(Files.readAllBytes(this.versionsListFile));
        JSONObject latest = (JSONObject) manifest.get("latest");
        LatestVersionInformation result = new LatestVersionInformation(latest.get("release").toString(), latest.get("snapshot").toString());
        return result;
    }

    @Override
    public void addObserver(IObserver<String> obs) {

    }

    @Override
    public void deleteObserver(IObserver<String> obs) {

    }

    @Override
    public void notifyObservers(String changedObj) {

    }

    public Path getVersionsListFile() {
        return versionsListFile;
    }

    @Override
    public List<IVersionShort> getShortInfoVersionList() throws Exception {
        ensureVersionsCache();
        return Collections.list(this.versionsCache.elements());
    }
}
