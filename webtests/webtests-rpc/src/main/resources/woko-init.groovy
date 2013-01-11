import net.sourceforge.jfacets.annotations.AnnotatedFacetDescriptorManager
import net.sourceforge.jfacets.annotations.DuplicatedKeyPolicyType
import woko.Woko
import woko.hbcompass.HibernateCompassStore
import woko.inmemory.InMemoryUserManager
import woko.ioc.SimpleWokoIocContainer
import woko.users.RemoteUserStrategy

HibernateCompassStore objectStore = new HibernateCompassStore(["test"])
InMemoryUserManager userManager = new InMemoryUserManager().addUser("wdevel", "wdevel", ["developer"])
RemoteUserStrategy urs = new RemoteUserStrategy()

def facetPackages = ["test.facet.pkg"]
facetPackages.addAll(Woko.DEFAULT_FACET_PACKAGES)

AnnotatedFacetDescriptorManager fdm = new AnnotatedFacetDescriptorManager(facetPackages)
    .setDuplicatedKeyPolicy(DuplicatedKeyPolicyType.FirstScannedWins)
    .initialize()

SimpleWokoIocContainer ioc = new SimpleWokoIocContainer(objectStore, userManager, urs, fdm)

return new Woko(ioc, [Woko.ROLE_GUEST])
