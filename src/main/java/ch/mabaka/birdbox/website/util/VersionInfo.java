package ch.mabaka.birdbox.website.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class VersionInfo {
    private static String cachedVersion = null;
    private static final String EXPECTED_TITLE = "birdbox-website"; // match your artifactId

    public static String getImplementationVersion() {
        if (cachedVersion != null) {
            return cachedVersion;
        }
        try {
            Enumeration<URL> resources = VersionInfo.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                try (InputStream is = url.openStream()) {
                    Manifest manifest = new Manifest(is);
                    Attributes attr = manifest.getMainAttributes();
                    String title = attr.getValue("Implementation-Title");
                    String version = attr.getValue("Implementation-Version");
                    if (EXPECTED_TITLE.equals(title) && version != null && !version.isEmpty()) {
                        cachedVersion = version;
                        return version;
                    }
                }
            }
        } catch (IOException e) {
            // ignore, fall through to unknown
        }
        return "unknown";
    }
}