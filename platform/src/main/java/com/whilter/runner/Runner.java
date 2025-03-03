package com.whilter.runner;

import com.beust.jcommander.JCommander;
import com.whilter.Bootstrap;


public class Runner {

    public static void main(String[] args) throws Exception {
        run(args, true);
    }

    public static void run(String[] argv, boolean join) throws Exception {
        Args args = new Args();
        JCommander.newBuilder().addObject(args).build().parse(argv);
        if (args.getHomeDir() != null) {
            System.setProperty("home.dir", args.getHomeDir());
        }
        if (args.getConfFile() != null) {
            System.setProperty("config.name", args.getConfFile());
        }
        Class<?>[] springConfs = new Class[args.getSpringConfs().size()];

        for (int i = 0; i < args.getSpringConfs().size(); i++) {
            springConfs[i] = Class.forName(args.getSpringConfs().get(i));
        }
        Bootstrap bootstrap = new Bootstrap(true, springConfs);
        bootstrap.load(join);
    }
}
