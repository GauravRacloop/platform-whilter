package com.minda.iconnect.spark.config;

import com.minda.iconnect.platform.conf.internal.AbstractConfiguration;

/**
 * Created by mayank on 30/08/17.
 */
public class SparkJobsDistro extends AbstractConfiguration {
    private String groupId;
    private String version;
    private String artifactId;
    private String localFsPath;
    private String hdfsFsPath;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getLocalFsPath() {
        return localFsPath;
    }

    public void setLocalFsPath(String localFsPath) {
        this.localFsPath = localFsPath;
    }

    public String getHdfsFsPath() {
        return hdfsFsPath;
    }

    public void setHdfsFsPath(String hdfsFsPath) {
        this.hdfsFsPath = hdfsFsPath;
    }
}
