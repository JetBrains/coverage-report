<#setting number_format="#">

<#macro currentScope>Current scope: </#macro>

<#macro coverageStatCell statEntry>
<#if statEntry.percent &gt;= 0>
<td class="coverageStat">
  <span class="percent">
    ${(statEntry.percent)?string("0.#")}%<#if statEntry.diff?has_content> <@showDiff diffValue=statEntry.diff.percentDiff>${statEntry.diff.percentDiff?string("0.##")}%</@showDiff></#if>
  </span>
  <span class="absValue">
    (${statEntry.covered}<#if statEntry.diff?has_content
           > <@showDiff diffValue=statEntry.diff.coveredDiff>${statEntry.diff.coveredDiff}</@showDiff></#if
           >/${statEntry.total}<#if statEntry.diff?has_content
           ><@showDiff diffValue=statEntry.diff.totalDiff>${statEntry.diff.totalDiff}</@showDiff></#if
           >)
  </span>
</td>
</#if>
</#macro>

<#macro showDiff diffValue showBrackets=false>
<#if diffValue &gt; 0>
<span class="green"><#if showBrackets>(+<#nested/>)<#else>+<#nested/></#if></span>
</#if>
<#if diffValue < 0>
<span class="red"><#if showBrackets>(<#nested/>)<#else><#nested/></#if></span>
</#if>
</#macro>

<#macro coverageStatHeaderCell label statEntry sorted sortOption=sort_option_none>
<#if statEntry.percent &gt;= 0>
<th class="coverageStat <@sortableCellClass sorted=sorted sortedDesc=sortOption.inverse().descendingOrder/>">
  <#if !sorted && sortOption.name() == "NONE">${label}<#else><@sortableCellLabel label=label sortOption=sortOption/></#if>
</th>
</#if>
</#macro>

<#macro sortableCellClass sorted sortedDesc>
<#if sorted && sortedDesc>sortedDesc</#if><#if sorted && !sortedDesc>sortedAsc</#if>
</#macro>

<#macro sortableCellLabel label sortOption><a href="${paths.getOrder(sortOption)}">${label}</a></#macro>

<#macro coverageStatRow coverageStatistics showForClass=true>
<#if showForClass>
<@coverageStatCell statEntry=coverageStatistics.classStats/>
</#if>
<@coverageStatCell statEntry=coverageStatistics.methodStats/>
<@coverageStatCell statEntry=coverageStatistics.blockStats/>
<@coverageStatCell statEntry=coverageStatistics.lineStats/>
<@coverageStatCell statEntry=coverageStatistics.statementStats/>
</#macro>

<#macro coverageStatHeaderRow coverageStatistics sortOption=sort_option_none showForClass=true>
<#if showForClass>
<@coverageStatHeaderCell statEntry=coverageStatistics.classStats label="Class, %" sorted=sortOption.orderByClass() sortOption=sortOption.nextOrderByClass()/>
</#if>
<@coverageStatHeaderCell statEntry=coverageStatistics.methodStats label="Method, %" sorted=sortOption.orderByMethod()  sortOption=sortOption.nextOrderByMethod()/>
<@coverageStatHeaderCell statEntry=coverageStatistics.blockStats label="Block, %" sorted=sortOption.orderByBlock()  sortOption=sortOption.nextOrderByBlock()/>
<@coverageStatHeaderCell statEntry=coverageStatistics.lineStats label="Line, %" sorted=sortOption.orderByLine()  sortOption=sortOption.nextOrderByLine()/>
<@coverageStatHeaderCell statEntry=coverageStatistics.statementStats label="Statements, %" sorted=sortOption.orderByStatement()  sortOption=sortOption.nextOrderByStatement()/>
</#macro>

<#function ternaryOp condition positiveExpr negativeExpr>
  <#if condition><#return positiveExpr/></#if>
  <#return negativeExpr/>
</#function>

<#macro overallStatTable labelName labelValue coverageStatistics>
<table class="coverageStats">
  <tr>
    <th class="name">${labelName}</th>
    <@coverageStatHeaderRow coverageStatistics=coverageStatistics/>
  </tr>
  <tr>
    <td class="name">${labelValue}</td>
    <@coverageStatRow coverageStatistics=coverageStatistics/>
  </tr>
</table>
</#macro>

<#macro moduleName module><#if module.empty>&lt;unknown ${resources['coverage.module']}&gt;<#else>${module.name?html}</#if></#macro>
<#macro namespaceName namespace><#if namespace?length = 0>&lt;empty ${resources['coverage.namespace']} name&gt;<#else>${namespace?html}</#if></#macro>
<#macro className clazz><#if clazz.name?length = 0>&lt;empty ${resources['coverage.class']} name&gt;<#else>${clazz.name?html}</#if></#macro>


<#macro page title="">
<!DOCTYPE html>
<html id="htmlId">
<head>
  <title>Coverage Report > ${title}</title>
  <style type="text/css">
    @import "${paths.resourcesPath}/.css/coverage.css";
    @import "${paths.resourcesPath}/.css/highlight-idea.css";
  </style>
  <script type="text/javascript" src="${paths.resourcesPath}/.js/highlight.pack.js"></script>
</head>

<body>
<div class="content">
<#nested/>
</div>

<script type="text/javascript">
(function() {
    var msie = false, msie9 = false;
    /*@cc_on
      msie = true;
      @if (@_jscript_version >= 9)
        msie9 = true;
      @end
    @*/

    if (!msie || msie && msie9) {
        var codeBlock = document.getElementById('sourceCode');

        if (codeBlock) {
            hljs.highlightBlock(codeBlock);
        }
    }
})();
</script>

<div class="footer">
    <#if footerTextHTML?has_content>${footerTextHTML}</#if>
    <div style="float:right;">generated on ${generateDate?string("yyyy-MM-dd HH:mm")}</div>
</div>
</body>
</html>
</#macro>