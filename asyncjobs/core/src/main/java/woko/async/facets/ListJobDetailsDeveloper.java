package woko.async.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.async.JobDetails;
import woko.facets.builtin.developer.ListTabularImpl;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.Arrays;
import java.util.List;

/**
 * Overriden <code>list</code> facet to show {@link JobDetails} in a tabular fashion
 * and with relevant properties only.
 */
@FacetKey(name="list", profileId = "developer", targetObjectType = JobDetails.class)
public class ListJobDetailsDeveloper<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends ListTabularImpl<OsType,UmType,UnsType,FdmType> {

    @Override
    public List<String> getPropertyNames() {
        return Arrays.asList("jobUuid", "class", "startTime", "endTime", "killTime", "exceptionString");
    }
}
