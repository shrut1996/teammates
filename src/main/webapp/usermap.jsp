<%@ page import="teammates.common.util.FrontEndLibrary" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="jsIncludes">
    <script type="text/javascript" src="<%= FrontEndLibrary.D3 %>"></script>
    <script type="text/javascript" src="<%= FrontEndLibrary.TOPOJSON %>"></script>
    <script type="text/javascript" src="<%= FrontEndLibrary.DATAMAPS %>"></script>
    <script>
        var geoDataUrl = '<%= FrontEndLibrary.WORLDMAP %>';
    </script>
    <script type="text/javascript" src="/js/countryCodes.js"></script>
    <script type="text/javascript" src="/js/userMap.js"></script>
</c:set>
<t:staticPage jsIncludes="${jsIncludes}">
    <main>
        <h1 class="color_orange">Who is using TEAMMATES?</h1>
        <div id="world-map"></div>
        <p class="text-right">Last updated: <span id="lastUpdate" ></span></p>
        <h2 class="text-center color_blue">
            <span id="totalUserCount" class="color_orange"></span>
            institutions from
            <span id="totalCountryCount" class="color_orange"></span>
            countries
        </h2>
    </main>
</t:staticPage>
