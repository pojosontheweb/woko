package woko.facets.builtin.auth;

/**
 * Interface for post login facet : if such a facet exists, it's invoked right after the authentication procedd has succeeded.
 */
public interface PostLoginFacet {

    static final String FACET_NAME = "postLogin";

    void execute(String user);
}
