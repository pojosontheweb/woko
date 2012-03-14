/*
 * Copyright 2001-2010 Remi Vankeisbelck
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

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import woko.Woko;
import woko.facets.BaseForwardRpcResolutionFacet;
import woko.facets.WokoFacetContext;
import woko.persistence.ResultIterator;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseResultFacet extends BaseForwardRpcResolutionFacet implements ResultFacet {

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

    public Resolution getResolution(ActionBeanContext abc) {
        className = abc.getRequest().getParameter("className");
        page = page == null ? 1 : page;
        resultsPerPage = resultsPerPage == null ? 10 : resultsPerPage;
        int start = (page - 1) * resultsPerPage;
        int limit = resultsPerPage;
        resultIterator = createResultIterator(abc, start, limit);
        return super.getResolution(abc);
    }

    public static JSONObject resultIteratorToJson(Woko woko, HttpServletRequest request, ResultIterator resultIterator) {
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
                    RenderObjectJson roj =
                            (RenderObjectJson) woko.getFacet(WokoFacets.renderObjectJson, request, o);
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
        return new StreamingResolution("text/json", r.toString());
    }

    protected abstract ResultIterator createResultIterator(ActionBeanContext abc, int start, int limit);

}
