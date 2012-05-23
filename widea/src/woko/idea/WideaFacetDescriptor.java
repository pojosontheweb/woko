package woko.idea;

import org.jetbrains.annotations.NotNull;

public class WideaFacetDescriptor {

    private final String name;
    private final String profileId;
    private final String targetObjectTypeName;
    private final String facetClassName;

    public WideaFacetDescriptor(
            @NotNull String name,
            @NotNull String profileId,
            @NotNull String targetObjectTypeName,
            @NotNull String facetClassName) {
        this.name = name;
        this.profileId = profileId;
        this.targetObjectTypeName = targetObjectTypeName;
        this.facetClassName = facetClassName;
    }

    public String getName() {
        return name;
    }

    public String getProfileId() {
        return profileId;
    }

    public String getTargetObjectTypeName() {
        return targetObjectTypeName;
    }

    public String getFacetClassName() {
        return facetClassName;
    }
}
