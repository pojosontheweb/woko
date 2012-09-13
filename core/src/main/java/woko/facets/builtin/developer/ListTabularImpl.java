package woko.facets.builtin.developer;

import woko.facets.builtin.TabularResultFacet;

import java.util.List;

public class ListTabularImpl extends ListImpl implements TabularResultFacet {

    @Override
    public String getPath() {
        return "/WEB-INF/jsp/developer/list-tabular.jsp";
    }

    @Override
    public List<String> getPropertyNames() {
        return null;
    }
}
