package woko.facets.builtin.developer;

import woko.facets.builtin.TabularResultFacet;

import java.util.List;

public class ListTabularImpl extends ListImpl implements TabularResultFacet {

    @Override
    public String getPath() {
        return "/WEB-INF/woko/jsp/developer/listTabular.jsp";
    }

    @Override
    public List<String> getPropertyNames() {
        return null;
    }
}
