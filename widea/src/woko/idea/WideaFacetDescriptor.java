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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WideaFacetDescriptor that = (WideaFacetDescriptor) o;

        if (!facetClassName.equals(that.facetClassName)) return false;
        if (!name.equals(that.name)) return false;
        if (!profileId.equals(that.profileId)) return false;
        if (!targetObjectTypeName.equals(that.targetObjectTypeName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + profileId.hashCode();
        result = 31 * result + targetObjectTypeName.hashCode();
        result = 31 * result + facetClassName.hashCode();
        return result;
    }
}
