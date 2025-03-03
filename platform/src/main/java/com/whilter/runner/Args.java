package com.whilter.runner;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepakchauhan on 22/07/17.
 */
class Args {

    @Parameter(names = {"-s", "--spring"}, description = "Spring config classes")
    private List<String> springConfs = new ArrayList<>();

    @Parameter(names = {"-h", "--home"}, description = "Home Directory")
    private String homeDir;

    @Parameter(names = {"-c", "--conf"}, description = "Config File Name")
    private String confFile;

    public List<String> getSpringConfs() {
        return springConfs;
    }

    public void setSpringConfs(List<String> springConfs) {
        this.springConfs = springConfs;
    }

    public String getHomeDir() {
        return homeDir;
    }

    public void setHomeDir(String homeDir) {
        this.homeDir = homeDir;
    }

    public String getConfFile() {
        return confFile;
    }

    public void setConfFile(String confFile) {
        this.confFile = confFile;
    }
}
