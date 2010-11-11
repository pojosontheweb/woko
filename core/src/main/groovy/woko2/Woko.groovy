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
import woko2.facets.FacetNotFoundException
import net.sourceforge.jfacets.IFacetDescriptorManager

class Woko {

  static final String ROLE_ALL = 'all'
  static final String ROLE_GUEST = 'guest'
  static final String ROLE_DEVEL = 'developer'

  protected final WLogger logger = WLogger.getLogger(getClass())

  public static final String CTX_KEY = 'woko'

  private final UserManager userManager;
  private final ObjectStore objectStore;
  private final List<String> fallbackRoles;
  private final IFacetDescriptorManager facetDescriptorManager;
  private final UsernameResolutionStrategy usernameResolutionStrategy;

  static Woko getWoko(ServletContext ctx) {
    return (Woko)ctx.getAttribute(CTX_KEY)
  }

  protected JFacets jFacets;

  public Woko(ObjectStore objectStore,UserManager userManager,List<String> fallbackRoles,IFacetDescriptorManager facetDescriptorManager,UsernameResolutionStrategy usernameResolutionStrategy) {
    this.objectStore = objectStore;
    this.userManager = userManager;
    this.fallbackRoles = Collections.unmodifiableList(fallbackRoles);
    this.facetDescriptorManager = facetDescriptorManager;
    this.usernameResolutionStrategy = usernameResolutionStrategy;
    init()
  }

  public static IFacetDescriptorManager createDefaultFacetDescriptorManager() {
    AnnotatedFacetDescriptorManager a = new AnnotatedFacetDescriptorManager(['woko2.facets.builtin', 'facets'])
    a.initialize()
    return a
  }

  public static UsernameResolutionStrategy createDefaultUsernameResolutionStrategy() {
    return new RemoteUserStrategy();
  }

  private final Woko init() {
    logger.info("Initializing Woko...")
    initJFacets()
    customInit()
    logger.info """
__       __     _  __
\\ \\  _  / /___ | |/ / ___
 \\ \\/ \\/ // o \\|   K /   \\
  \\__W__/ \\___/|_|\\_\\\\_o_/  2.0
             POJOs on the Web !
"""
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
    def facetContextFactory = new WokoFacetContextFactory(this)
    jFacets = new JFacetsBuilder(profileRepository, facetDescriptorManager).
                setFacetContextFactory(facetContextFactory).
                build()
    def descriptors = jFacets.facetRepository.facetDescriptorManager.descriptors;
    logger.info("${descriptors.length} facets found :")
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

  def getFacet(String name, HttpServletRequest request, Object targetObject) {
    return getFacet(name, request, targetObject, null)
  }

  def getFacet(String name, HttpServletRequest request, Object targetObject, Class targetObjectClass, boolean throwIfNotFound) {
    def f = getFacet(name, request, targetObject, targetObjectClass)
    if (f==null && throwIfNotFound) {
      throw new FacetNotFoundException(name, targetObject, targetObjectClass, getUsername(request))
    }
    return f
  }

  def getFacet(String name, HttpServletRequest request, Object targetObject, Class targetObjectClass) {
    logger.debug("Trying to get facet $name for target object $targetObject, targetObjectClass $targetObjectClass...")
    String username = getUsername(request);
    List<String> roles
    if (username==null) {
      roles = fallbackRoles
      logger.debug("Username not supplied, using fallback roles : $fallbackRoles")
    } else {
      roles = userManager.getRoles(username)
      if (!roles) {
        roles = fallbackRoles
      }
      logger.debug("Using roles $roles found for user $username")
    }
    if (!roles.contains(ROLE_ALL)) {
      roles = new ArrayList(roles)
      roles << ROLE_ALL
    }
    if (targetObject==null && targetObjectClass==null) {
      logger.debug("No object or class provided, defaulting to Object.class")
      targetObjectClass = Object.class
    }
    if (targetObjectClass==null) {
      targetObjectClass = targetObject.getClass()
    }
    for (String role : roles) {
      logger.debug("Trying role : $role")
      def facet = jFacets.getFacet(name, role, targetObject, targetObjectClass)
      if (facet) {
        request.setAttribute(name, facet)
        logger.debug("Facet found and bound to request, returning $facet")
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
    def className = objectStore.getClassMapping(obj.getClass())
    def id = objectStore.getKey(obj)
    return "/$facetName/$className/$id"
  }


}
