package woko2

import woko2.users.UserManager
import woko2.persistence.ObjectStore
import woko2.util.WLogger
import javax.servlet.ServletContext
import javax.servlet.http.HttpServletRequest
import net.sourceforge.jfacets.JFacets
import net.sourceforge.jfacets.JFacetsBuilder
import woko2.facets.WokoProfileRepository
import net.sourceforge.jfacets.annotations.AnnotatedFacetDescriptorManager
import woko2.facets.WokoFacetContextFactory
import woko2.users.UsernameResolutionStrategy
import woko2.users.RemoteUserStrategy

abstract class Woko {

  static final String ROLE_ALL = 'all'
  static final String ROLE_GUEST = 'guest'
  static final String ROLE_DEVEL = 'developer'

  protected final WLogger logger = WLogger.getLogger(getClass())

  private static final String CTX_KEY = 'woko'

  private final UserManager userManager;
  private final ObjectStore objectStore;
  private final List<String> fallbackRoles;
  private UsernameResolutionStrategy usernameResolutionStrategy = new RemoteUserStrategy()

  static Woko getWoko(ServletContext ctx) {
    return (Woko)ctx.getAttribute(CTX_KEY)
  }

  protected JFacets jFacets;

  Woko(ObjectStore objectStore, UserManager userManager, List<String> fallbackRoles) {
    this.objectStore = objectStore;
    this.userManager = userManager;
    this.fallbackRoles = Collections.unmodifiableList(fallbackRoles);
    init()
  }

  private final Woko init() {
    logger.info """
__       __     _  __
\\ \\  _  / /___ | |/ / ___
 \\ \\/ \\/ // o \\|   K /   \\
  \\__W__/ \\___/|_|\\_\\\\_o_/

Initializing...
"""
    initJFacets()
    customInit()
    logger.info "Woko is ready :"
    logger.info " * userManager : $userManager"
    logger.info " * objectStore : $objectStore"
    logger.info " * jFacets : $jFacets"
    logger.info " * fallbackRole : $fallbackRoles"
    logger.info " * usernameResolutionStrategy : $usernameResolutionStrategy"
    return this
  }

  protected void initJFacets() {
    logger.info("Initializing JFacets...")
    def profileRepository = new WokoProfileRepository(userManager)
    AnnotatedFacetDescriptorManager facetDescriptorManager = new AnnotatedFacetDescriptorManager(['woko2.facets.builtin'])
    facetDescriptorManager.initialize()
    def facetContextFactory = new WokoFacetContextFactory(this)
    jFacets = new JFacetsBuilder(profileRepository, facetDescriptorManager).
                setFacetContextFactory(facetContextFactory).
                build()
    def descriptors = jFacets.facetRepository.facetDescriptorManager.descriptors;
    logger.info("${descriptors.length} descriptors loaded :")
    descriptors.each { d ->
      logger.info("  * $d.name, $d.profileId, $d.targetObjectType -> $d.facetClass")
    }
    logger.info("JFacets init OK.")
  }

  protected void customInit() {}

  List<String> getFallbackRoles() { return fallbackRoles }

  ObjectStore getObjectStore() { return objectStore }

  UserManager getUserManager() { return userManager }

  JFacets getJFacets() { return jFacets }

  Woko setUsernameResolutionStrategy(UsernameResolutionStrategy urs) {
    this.usernameResolutionStrategy = urs
    return this
  }

  final void close() {
    logger.info("Closing...")
    doClose()
    logger.info("Woko has been closed.")
  }

  protected void doClose() {}

  def getFacet(String name, HttpServletRequest request, Object targetObject, Class targetObjectClass) {
    logger.debug("Trying to get facet $name for target object $targetObject...")
    String username = getUsername(request);
    List<String> roles
    if (username==null) {
      roles = fallbackRoles
      logger.debug("Username not supplied, using fallback roles : $fallbackRoles")
    } else {
      roles = userManager.getRoles(username)
      logger.debug("Using roles $roles found for user $username")
    }
    if (!roles) {
      throw new IllegalStateException("No roles obtained for user $username ! Please make sure that the user manager returns appropriate roles for the user, or that the fallback roles are ok.")
    }
    if (!roles.contains(ROLE_ALL)) {
      roles = new ArrayList(roles)
      roles << ROLE_ALL
    }
    if (targetObject==null && targetObjectClass==null) {
      logger.debug("No object or class provided, defaulting to Object.class")
      targetObjectClass = Object.class
    }
    for (String role : roles) {
      logger.debug("Trying role : $role")
      def facet = jFacets.getFacet(name, role, targetObject, targetObjectClass)
      if (facet) {
        logger.debug("Facet found, returning $facet")
        return facet
      }
    }
    logger.debug("Facet not found for name: $name, roles: $roles, targetObject: $targetObject, targetObjectClass: $targetObjectClass, returning null")
    return null
  }

  String getUsername(HttpServletRequest request) {
    return usernameResolutionStrategy.getUsername(request)
  }

  String facetUrl(String facetName, Object obj) {
    def className = objectStore.getClassName(obj)
    def id = objectStore.getKey(obj)
    return "/$facetName/$className/$id"
  }


}
