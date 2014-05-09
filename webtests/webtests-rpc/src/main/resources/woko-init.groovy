import net.sourceforge.jfacets.annotations.AnnotatedFacetDescriptorManager
import woko.Woko
import woko.groovyinit.GroovyInitListener
import woko.hbcompass.HibernateCompassStore
import woko.inmemory.InMemoryUserManager
import woko.ioc.SimpleWokoIocContainer
import woko.users.RemoteUserStrategy

HibernateCompassStore objectStore = new HibernateCompassStore(["test"])
InMemoryUserManager userManager = new InMemoryUserManager().addUser("wdevel", "wdevel", ["developer"])
RemoteUserStrategy urs = new RemoteUserStrategy()

def facetPackages = GroovyInitListener.getFacetPackagesFromWebXml(servletContext)

AnnotatedFacetDescriptorManager fdm = new AnnotatedFacetDescriptorManager(facetPackages)
    .initialize()

SimpleWokoIocContainer ioc = new SimpleWokoIocContainer(objectStore, userManager, urs, fdm)

return new Woko(ioc, [Woko.ROLE_GUEST])
