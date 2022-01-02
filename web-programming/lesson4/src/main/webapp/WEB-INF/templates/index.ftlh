<#import "./common.ftlh" as c>

<@c.page>
    <#list posts?reverse as p>
        <@c.post p true false/>
    </#list>
</@c.page>
