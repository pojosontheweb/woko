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

public class WokoProfileRepository implements IProfileRepository {

    private final UserManager userManager;

    private final boolean enableRequestCaching;

    private static final WLogger logger = WLogger.getLogger(WokoProfileRepository.class);

    public WokoProfileRepository(UserManager userManager, boolean enableRequestCaching) {
        this.userManager = userManager;
        this.enableRequestCaching = enableRequestCaching;
        logger.info("Request profile caching is " + (enableRequestCaching ? "enabled" : "disabled"));
    }

    public IProfile getProfileById(String profileId) {
        return new WokoProfile(profileId);
    }

    private IProfile[] handleSuperProfilesNoCache(IProfile profile) {
        List<String> roles = userManager.getRoles(profile.getId());
        List<IProfile> profiles = new ArrayList<IProfile>();
        for (String role : roles) {
            profiles.add(new WokoProfile(role));
        }
        IProfile[] result = new IProfile[profiles.size()];
        return profiles.toArray(result);
    }

    public IProfile[] getSuperProfiles(IProfile profile) {
        if (enableRequestCaching) {
            HttpServletRequest request = WokoRequestInterceptor.getRequest();
            if (request==null) {
                // Request caching enabled but WokoRequestInterceptor has returned no request. Out of container or interceptor not reached ? Using default behavior anyway.
                return handleSuperProfilesNoCache(profile);
            }
            String reqAttrName = "wokoSuperProfilesCache_" + profile.getId();
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