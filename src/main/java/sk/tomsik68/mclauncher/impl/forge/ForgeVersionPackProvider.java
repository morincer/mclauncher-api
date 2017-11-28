package sk.tomsik68.mclauncher.impl.forge;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.tomsik68.mclauncher.api.forge.IForgeVersionPackInfo;
import sk.tomsik68.mclauncher.api.forge.IForgeVersionPackProvider;
import sk.tomsik68.mclauncher.util.HttpUtils;

import java.util.*;

public class ForgeVersionPackProvider implements IForgeVersionPackProvider {

    private static final Logger log = LoggerFactory.getLogger(ForgeVersionPackProvider.class);

    private String versionPackInfoJsonUrl = "http://files.minecraftforge.net/maven/net/minecraftforge/forge/json";
    private ForgeDownloader forgeDownloader;

    public ForgeVersionPackProvider() {
        forgeDownloader = new ForgeDownloader();
    }

    @Override
    public Dictionary<String, IForgeVersionPackInfo> getForgeVersionPacks() throws Exception {

        Dictionary<String, IForgeVersionPackInfo> result = new Hashtable<>();
        log.info("Downloading forge versions from " + versionPackInfoJsonUrl);
        String forgeAssetsJson = HttpUtils.httpGet(versionPackInfoJsonUrl);
        JSONObject assetsJson = (JSONObject) JSONValue.parse(forgeAssetsJson);

        JSONObject number = (JSONObject) assetsJson.get("number");

        log.info("Found " + number.size() + " entries");

        for (Object value : number.values()) {
            JSONObject forgeVersionInfo = (JSONObject) value;

            log.debug("Parsing data for build #" + forgeVersionInfo.get("build"));

            String mcVersion = forgeVersionInfo.get("mcversion").toString();

            if (result.get(mcVersion) == null) {
                ForgeVersionPackInfo forgeVersionPackInfo = new ForgeVersionPackInfo();
                forgeVersionPackInfo.setMinecraftVersion(mcVersion);
                result.put(mcVersion, forgeVersionPackInfo);
            }

            IForgeVersionPackInfo forgeVersionPackInfo = result.get(mcVersion);

            String forgeBuildNumber = forgeVersionInfo.get("build").toString();
            ForgeVersion forgeVersion = new ForgeVersion();
            forgeVersionPackInfo.getForgeVersions().put(forgeBuildNumber, forgeVersion);

            forgeVersion.setForgeBuildNumber(forgeBuildNumber);
            forgeVersion.setForgeVersion(forgeVersionInfo.get("version").toString());
            forgeVersion.setMinecraftVersion(forgeVersionInfo.get("mcversion").toString());

            Double modifiedJson = (Double) forgeVersionInfo.get("modified");

            Date modified = new Date(modifiedJson.longValue());

            forgeVersion.setModified(modified);

            JSONArray files = (JSONArray) forgeVersionInfo.get("files");
            for (Object file : files) {
                JSONArray fileJson = (JSONArray) file;
                ForgeAsset asset = new ForgeAsset();
                forgeVersion.getAssets().add(asset);

                asset.setPackageType(fileJson.get(0).toString());
                asset.setType(fileJson.get(1).toString());
                asset.setHash(fileJson.get(2).toString());
                asset.setDownloader(forgeDownloader);
                asset.setMinecraftVersion(forgeVersion.getMinecraftVersion());
                asset.setForgeVersion(forgeVersion.getForgeVersion());
            }
        }

        JSONObject promosJson = (JSONObject) assetsJson.get("promos");
        for (Object key : promosJson.keySet()) {
            String keyInfo = key.toString();
            if (keyInfo.startsWith("latest") || keyInfo.startsWith("recommended")) {
                continue;
            }

            String[] split = keyInfo.split("-");
            if (split.length == 2) {
                ForgeVersionPackInfo info = (ForgeVersionPackInfo) result.get(split[0]);
                if (info != null) {
                    if (split[1].startsWith("latest")) {
                        info.setLatestForgeVersion(promosJson.get(key).toString());
                    } else if (split[1].startsWith("recommended")) {
                        info.setRecommendedForgeVersion(promosJson.get(key).toString());
                    } else {
                        log.warn("Failed to parse promo version type for " + key);
                    }
                } else {
                    log.warn("Failed to find a suitable description for promoed version " + key);
                }

            } else {
                log.warn("Failed to parse promo version string " + key);
            }
        }

        return result;
    }
}
