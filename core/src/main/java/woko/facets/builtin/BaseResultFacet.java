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

package woko.facets.builtin;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import woko.Woko;
import woko.facets.BaseForwardRpcResolutionFacet;
import woko.persistence.ObjectStore;
import woko.persistence.ResultIterator;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.JsonResolution;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Base abstract class for implementing {@link ResultFacet}.
 */
public abstract class BaseResultFacet<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseForwardRpcResolutionFacet<OsType,UmType,UnsType,FdmType> implements ResultFacet {

    private Integer resultsPerPage = 10;
    private Integer page = 1;
    private String className;

    private ResultIterator resultIterator;

    public Integer getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(Integer resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getClassName() {
        return className;
    }

    public ResultIterator getResults() {
        return resultIterator;
    }

    @Override
    public String getPageHeaderTitle() {
        return null;
    }

    /**
     * As this feature has no impact on the Woko default behaviour we return an empty Map.
     * Override this method in order to pass some of your arguments over pagination process.
     *
     * @return an empty Map
     */
    @Override
    public Map<String, Object> getArgs() {
        return null;
    }

    public Resolution getResolution(ActionBeanContext abc) {
        className = abc.getRequest().getParameter("className");
        page = page == null ? 1 : page;
        resultsPerPage = resultsPerPage == null ? 10 : resultsPerPage;
        int start = (page - 1) * resultsPerPage;
        int limit = resultsPerPage;
        resultIterator = createResultIterator(abc, start, limit);
        return super.getResolution(abc);
    }

    public static <
            OsType extends ObjectStore,
            UmType extends UserManager,
            UnsType extends UsernameResolutionStrategy,
            FdmType extends IFacetDescriptorManager
            > JSONObject resultIteratorToJson(Woko<OsType,UmType,UnsType,FdmType> woko, HttpServletRequest request, ResultIterator<?> resultIterator) {
        try {
            JSONObject r = new JSONObject();
            r.put("totalSize", resultIterator.getTotalSize());
            r.put("start", resultIterator.getStart());
            r.put("limit", resultIterator.getLimit());
            JSONArray items = new JSONArray();
            while (resultIterator.hasNext()) {
                Object o = resultIterator.next();
                if (o == null) {
                    items.put((JSONObject) null);
                } else {
                    RenderObjectJson roj = woko.getFacet(WokoFacets.renderObjectJson, request, o);
                    JSONObject jo = roj.objectToJson(request);
                    items.put(jo);
                }
            }
            r.put("items", items);
            return r;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Resolution getRpcResolution(final ActionBeanContext actionBeanContext) {
        ResultIterator resultIterator = getResults();
        JSONObject r = resultIteratorToJson(getFacetContext().getWoko(), actionBeanContext.getRequest(), resultIterator);
        return new JsonResolution(r);
    }

    /**
     * To be implemented by concrete subclasses : create and return the results for passed parameters
     * @param abc the action bean context
     * @param start the start index (>=0)
     * @param limit the limit
     * @return the results to be displayed
     */
    protected abstract ResultIterator createResultIterator(ActionBeanContext abc, int start, int limit);

    public String getListWrapperCssClass() {
        return null;
    }

}
