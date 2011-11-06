<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="termCloud">
    <c:forEach items="${termCloudFragment.cloudElems}" var="cloudElem">
        <a class="cloudElem${cloudElem.categ}" href="${pageContext.request.contextPath}/search?facet.query=${cloudElem.term}">${cloudElem.term}</a>
    </c:forEach>
</div>