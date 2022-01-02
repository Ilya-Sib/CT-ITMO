<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#list posts as p>
        <#if p.userId == user_id>
            <@c.post p=p short=true insideSidebar=false/>
        </#if>
    </#list>
</@c.page>