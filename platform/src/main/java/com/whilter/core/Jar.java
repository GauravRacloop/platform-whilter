package com.whilter.core;

import java.io.File;

/**
 * Created by mayank on 30/08/17.
 */
public class Jar {
    private String groupId;
    private String artifactId;
    private String version;
    private String scope;
    private String directoryPath;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public String getFileName() {
        return artifactId + '-' + version + ".jar";
    }

    public String getFilePath() {
        String directoryPath = getDirectoryPath();
        if (!directoryPath.endsWith(File.separator)) {
            directoryPath = directoryPath + File.separator;
        }
        return directoryPath + artifactId + '-' + version + ".jar";
    }
}
