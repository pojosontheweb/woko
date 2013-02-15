/*
 * Copyright 2001-2012 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package woko.facets;

import net.sourceforge.jfacets.IProfile;
import net.sourceforge.jfacets.IProfileRepository;
import woko.actions.WokoRequestInterceptor;
import woko.users.UserManager;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Woko implementation of the JFacets <code>IProfileRepository</code>. Bridges Woko's
 * <code>UserManager</code> with the JFacets profiles structure.
 *
 * Also provides a request caching mechanism in order to avoid unnecessary calls to <code>UserManager</code>
 * and improve app performance.
 */
public class WokoProfileRepository implements IProfileRepository {

    public static final String WOKO_SUPER_PROFILES_CACHE_PREFIX = "wokoSuperProfilesCache_";

    /**
     * Ref to the UserManager
     */
    private final UserManager userManager;

    /**
     * Do we use request caching ?
     */
    private final boolean enableRequestCaching;

    private static final WLogger logger = WLogger.getLogger(WokoProfileRepository.class);

    /**
     * Create the profile repository with passed parameters
     * @param userManager the <code>UserManager</code>
     * @param enableRequestCaching request cahing flag
     */
    public WokoProfileRepository(UserManager userManager, boolean enableRequestCaching) {
        this.userManager = userManager;
        this.enableRequestCaching = enableRequestCaching;
        logger.info("Request profile caching is " + (enableRequestCaching ? "enabled" : "disabled"));
    }

    /**
     * Simply wraps passed <code>profileId</code> into a <code>WokoProfile</code>
     * @param profileId the profile id
     * @return a freshly created WokoProfile for passed ID
     */
    public IProfile getProfileById(String profileId) {
        return new WokoProfile(profileId);
    }

    /**
     * Return the super profiles for passed profile without caching.
     * @param profile the profile
     * @return an array of super profiles
     */
    private IProfile[] handleSuperProfilesNoCache(IProfile profile) {
        List<String> roles = userManager.getRoles(profile.getId());
        List<IProfile> profiles = new ArrayList<IProfile>();
        for (String role : roles) {
            profiles.add(new WokoProfile(role));
        }
        IProfile[] result = new IProfile[profiles.size()];
        return profiles.toArray(result);
    }

    /**
     * Return the super profiles for passed <code>profile</code>, possibly using the request cache.
     * As Woko uses a flat roles structure (no inheritance), we assume that
     * <code>profile</code> should return super profiles only if the profile
     * represents a user (not a role). If <code>profile</code> represents a
     * role, then we just return an empty array.
     * @param profile the profile
     * @return an array of super profiles
     */
    public IProfile[] getSuperProfiles(IProfile profile) {
        if (enableRequestCaching) {
            HttpServletRequest request = WokoRequestInterceptor.getRequest();
            if (request==null) {
                // Request caching enabled but WokoRequestInterceptor has returned no request. Out of container or interceptor not reached ? Using default behavior anyway.
                return handleSuperProfilesNoCache(profile);
            }
            String reqAttrName = WOKO_SUPER_PROFILES_CACHE_PREFIX + profile.getId();
            IProfile[] superProfiles = (IProfile[])request.getAttribute(reqAttrName);
            if (superProfiles==null) {
                superProfiles = handleSuperProfilesNoCache(profile);
                request.setAttribute(reqAttrName, superProfiles);
            }
            return superProfiles;
        } else {
            return handleSuperProfilesNoCache(profile);
        }
    }

}