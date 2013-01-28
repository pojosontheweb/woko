package test;

import org.hibernate.Session;
import woko.WokoIocInitListener;
import woko.async.JobManager;
import woko.auth.builtin.SessionUsernameResolutionStrategy;
import woko.ext.usermanagement.core.AccountStatus;
import woko.ext.usermanagement.hibernate.HbUser;
import woko.ext.usermanagement.hibernate.HibernateUserManager;
import woko.hbcompass.HibernateCompassStore;
import woko.hibernate.HibernateStore;
import woko.hibernate.TxCallback;
import woko.ioc.SimpleWokoIocContainer;
import woko.ioc.WokoIocContainer;
import woko.push.PushFacetDescriptorManager;

import java.util.Arrays;
import java.util.concurrent.Executors;

public class AsyncWebtestsInitListener
        extends WokoIocInitListener<HibernateCompassStore, HibernateUserManager<HbUser>, SessionUsernameResolutionStrategy, PushFacetDescriptorManager> {

    @Override
    protected WokoIocContainer<HibernateCompassStore, HibernateUserManager<HbUser>, SessionUsernameResolutionStrategy, PushFacetDescriptorManager> createIocContainer() {
        HibernateCompassStore store = new HibernateCompassStore(getPackageNamesFromConfig(HibernateCompassStore.CTX_PARAM_PACKAGE_NAMES, true));
        HibernateUserManager<HbUser> userManager = new HibernateUserManager<HbUser>(store, HbUser.class)
                .setRegisteredRoles(Arrays.asList("developer"));

        final HbUser wdevel = new HbUser();
        wdevel.setUsername("wdevel");
        wdevel.setPassword(userManager.encodePassword("wdevel"));
        wdevel.setAccountStatus(AccountStatus.Active);
        wdevel.setEmail("wdevel@woko.com");
        wdevel.setRoles(Arrays.asList("usermanager", "developer")); // user manager has to be first to avoid developer's list facet to take precedence
        wdevel.setAccountStatus(AccountStatus.Active);
        store.doInTx(new TxCallback() {
            @Override
            public void execute(HibernateStore store, Session session) throws Exception {
                store.save(wdevel);
            }
        });

        return new SimpleWokoIocContainer<HibernateCompassStore, HibernateUserManager<HbUser>, SessionUsernameResolutionStrategy, PushFacetDescriptorManager>(
                store,
                userManager,
                new SessionUsernameResolutionStrategy(),
                new PushFacetDescriptorManager(createAnnotatedFdm())
        ).addComponent(JobManager.KEY, new JobManager(Executors.newFixedThreadPool(10)));
    }

}
