package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.tomsik68.mclauncher.api.common.IObserver;
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

    private static final Logger log = LoggerFactory.getLogger(MCDownloadCachedVersionList.class);
    private Path versionsDirectory;

    private Dictionary<String, IVersionShort> versionsCache;

    public MCDownloadCachedVersionList(String versionsDir) {
        this(versionsDir, VERSION_MANIFEST_URL);
    }

    /**
     * Creates a new instance allowing to specify both the cache directory and manifest URL
     * @param versionDir - the directory to store the cached JSON
     * @param versionManifestUrl - the URL of the version_manifest.json
     */
    public MCDownloadCachedVersionList(String versionDir, String versionManifestUrl) {
        this.versionsDirectory = Paths.get(versionDir).toAbsolutePath();
        this.manifestUrl = versionManifestUrl;
    }

    @Override
    public void startDownload() throws Exception {
        if (Files.exists(getVersionsListFile())) {
            log.debug("Skipping download - file already exists at " + getVersionsListFile());
            return;
        }

        ensureVersionInfoManifest();
    }

    public Dictionary<String, IVersionShort> getVersionsCache() {
        return versionsCache;
    }

    private void ensureVersionInfoManifest() throws Exception {
        if (Files.exists(getVersionsListFile())) {
            return;
        } else {
            Files.createDirectories(this.getVersionsListFile().getParent());
            // at first, we download the complete version list
            log.info("Fetching versions metadata from " + manifestUrl);
            String jsonString = HttpUtils.httpGet(manifestUrl);
            Files.write(this.getVersionsListFile(), jsonString.getBytes());
        }
    }

    @Override
    public void setVersionsDirectory(Path versionDirectory) {
        this.versionsDirectory = versionDirectory;
    }

    @Override
    public Path getVersionsDirectory() {
        return versionsDirectory;
    }

    /**
     * Invalidates the caches making subsequent calls for data go for the online data sources
     * @throws IOException
     */
    public void invalidateCache() throws IOException {
        Files.deleteIfExists(this.getVersionsListFile());
        versionsCache = null;
    }

    @Override
    public IVersion retrieveVersionInfo(String id) throws Exception {
        // check if the version json already exists
        Path versionJson = Paths.get(this.versionsDirectory.toString(), id, id + ".json");
        if (Files.exists(versionJson)) {
            JSONObject jsonObject = (JSONObject) JSONValue.parse(Files.readAllBytes(versionJson));
            MCDownloadVersion version = new MCDownloadVersion(jsonObject);
            return version;
        }

        ensureVersionsCache();

        IVersionShort mcDownloadVersion = versionsCache.get(id);
        if (mcDownloadVersion == null || mcDownloadVersion.getUrl() == null) throw new Exception("Failed to find URL for the version " + id);

        log.info("Fetching version information from " + mcDownloadVersion.getUrl());

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
            JSONObject manifest = (JSONObject) JSONValue.parse(Files.readAllBytes(getVersionsListFile()));
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
        JSONObject manifest = (JSONObject) JSONValue.parse(Files.readAllBytes(getVersionsListFile()));
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
        return Paths.get(this.versionsDirectory.toString(), "versions.json");
    }

    @Override
    public List<IVersionShort> getShortInfoVersionList() throws Exception {
        ensureVersionsCache();
        return Collections.list(this.versionsCache.elements());
    }
}
