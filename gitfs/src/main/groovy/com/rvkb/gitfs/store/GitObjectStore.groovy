package com.rvkb.gitfs.store

import woko.persistence.faceted.FacetedObjectStore
import com.rvkb.gitfs.GitFS
import net.sourceforge.stripes.util.ResolverUtil
import java.lang.reflect.Modifier
import woko.util.WLogger
import com.rvkb.gitfs.GitFSBuilder

class GitObjectStore extends FacetedObjectStore {

  private static final WLogger logger = WLogger.getLogger(GitObjectStore.class)

  private List<Class<?>> mappedClasses
  private GitFS gfs

  GitObjectStore(File pathToRepo, List<String> packageNames) {
    // add mapped classes
    logger.info("Adding @GitEntity classes from packages $packageNames...")
    ResolverUtil<Object> resolverUtil = new ResolverUtil<Object>();
    String[] packages = new String[packageNames.size()];
    packages = packageNames.toArray(packages);
    resolverUtil.findAnnotated(GitEntity.class, packages);
    mappedClasses = []
    for (Class<?> clazz : resolverUtil.getClasses()) {
      int modifiers = clazz.modifiers
      if (Modifier.isPublic(modifiers) && !Modifier.isAbstract(modifiers)) {
        mappedClasses.add(clazz)
        logger.info("  - Added $clazz")
      }
    }
    mappedClasses = Collections.unmodifiableList(mappedClasses)
    logger.info("${mappedClasses.size()} classes added")

    // init git repo
    logger.info("Initializing Git repo in ${pathToRepo.absolutePath}...")
    GitFS gfs = new GitFSBuilder(pathToRepo).setCreateIfEmpty(true).build()
    this.gfs = gfs
  }

  List<Class<?>> getMappedClasses() {
    return mappedClasses
  }

  GitFS getGfs() {
    return gfs
  }

}
