<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="termCloud">
    <c:forEach items="${termCloudFragment.cloudElems}" var="cloudElem">
        <span class="cloudElem${cloudElem.categ}">${cloudElem.term}</span>
    </c:forEach>
</div>