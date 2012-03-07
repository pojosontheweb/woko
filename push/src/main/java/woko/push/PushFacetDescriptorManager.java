package woko.push;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.FacetDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PushFacetDescriptorManager implements IFacetDescriptorManager {

    private final IFacetDescriptorManager delegate;

    private List<FacetDescriptor> pushedDescriptors = new ArrayList<FacetDescriptor>();

    public PushFacetDescriptorManager(IFacetDescriptorManager delegate) {
        this.delegate = delegate;
    }

    public FacetDescriptor[] getDescriptors() {
        List<FacetDescriptor> all = new ArrayList<FacetDescriptor>();
        all.addAll(Arrays.asList(delegate.getDescriptors()));
        // add / replace with pushed descriptors
        List<FacetDescriptor> toBeRemoved = new ArrayList<FacetDescriptor>();
        for (FacetDescriptor fd : pushedDescriptors) {
            FacetDescriptor fdDelegate = findDescriptorInList(fd, all);
            if (fdDelegate!=null) {
                toBeRemoved.add(fdDelegate);
            }
            all.add(fd);
        }
        for (FacetDescriptor fd : toBeRemoved) {
            all.remove(fd);
        }
        FacetDescriptor[] result = new FacetDescriptor[all.size()];
        return all.toArray(result);
    }


    private boolean equals(String name, String profileId, Class<?> targetObjectType, FacetDescriptor fd) {
        return fd.getName().equals(name) &&
          fd.getProfileId().equals(profileId) &&
          fd.getTargetObjectType().equals(targetObjectType);
    }

    private boolean equals(FacetDescriptor fd1, FacetDescriptor fd2) {
        return equals(fd1.getName(), fd1.getProfileId(), fd1.getFacetClass(), fd2);
    }

    private FacetDescriptor findDescriptorInList(FacetDescriptor fd, List<FacetDescriptor> descriptors) {
        for (FacetDescriptor it : descriptors) {
            if (equals(fd,it)) {
                return it;
            }
        }
        return null;
    }

    public FacetDescriptor getDescriptor(String name, String profileId, Class targetObjectType) {
        // lookup pushed descriptors first
        for (FacetDescriptor fd : pushedDescriptors) {
            if (equals(name, profileId, targetObjectType, fd)) {
                return fd;
            }
        }
        // fallback to delegate
        return delegate.getDescriptor(name, profileId, targetObjectType);
    }


}
