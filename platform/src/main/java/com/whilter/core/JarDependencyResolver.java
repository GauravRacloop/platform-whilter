package com.whilter.core;

import java.util.Collection;

/**
 * Created by deepakchauhan on 23/07/17.
 */
public interface JarDependencyResolver {

    Collection<Jar> resolveJars(String groupId, String artifactId, String version) throws Exception;
}
