package woko.facets.builtin;

public interface RenderPropertiesEdit extends RenderProperties {

    public static final String FACET_NAME = RenderProperties.FACET_NAME +  "Edit";

    boolean isPartialForm();

    String getFieldPrefix();
}
