<#-- @ftlvariable name="post_id" type="java.lang.Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#if post_id?is_string || !c.findIndexBy(posts, "id", post_id)?has_content>
        Post not found
    <#else>
        <@c.post p=c.findBy(posts, "id", post_id) short=false insideSidebar=false/>
    </#if>
</@c.page>