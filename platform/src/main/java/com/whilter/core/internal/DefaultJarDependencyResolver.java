package com.whilter.core.internal;

import com.whilter.core.Jar;
import com.whilter.core.JarDependencyResolver;

import java.io.File;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Created by deepakc on 13/12/16.
 */
public class DefaultJarDependencyResolver implements JarDependencyResolver {

    private String basePath;

    public DefaultJarDependencyResolver(String basePath) {
        this.basePath = basePath;
    }

    private Jar toJar(String line) {
        String[] split = line.trim().split(":");
        Jar jar = null;
        if (split != null && split.length == 5) {
            if (split[2].equals("jar")) {
                jar = new Jar();
                jar.setGroupId(split[0]);
                jar.setArtifactId(split[1]);
                jar.setVersion(split[3]);
                jar.setScope(split[4]);
                jar.setDirectoryPath(basePath);
            }
        }
        return jar;
    }

    @Override
    public Collection<Jar> resolveJars(String groupId, String artifactId, String version) throws Exception {
        JarFile jarFile;
        if (basePath.startsWith("http")) {
            URL url = new URL("jar:" + basePath + "/" + artifactId + '-' + version + ".jar" + "!/");
            JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
            jarFile = jarConnection.getJarFile();
        } else {
            jarFile = new JarFile(basePath + File.separator + artifactId + '-' + version + ".jar");
        }
        ZipEntry jarEntry = jarFile.getEntry(artifactId + "-dependency.list");
        InputStream stream = jarFile.getInputStream(jarEntry);

        List<Jar> jars = new ArrayList<>();
        Scanner scanner = new Scanner(stream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Jar jar = toJar(line);
            if (jar != null && !jar.getScope().equalsIgnoreCase("provided") && !jar.getScope().equalsIgnoreCase("test")) {
                jars.add(jar);
            }
        }

        return jars;
    }

}
