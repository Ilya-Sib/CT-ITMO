<#-- @ftlvariable name="handle" type="java.lang.String" -->
<#import "./common.ftlh" as c>

<#macro showArrow index arrow lastIndex nextIndex>
<#-- @ftlvariable name="index" type="java.lang.Long" -->
    <#if index == lastIndex>
        <span style="color: lightgray;"> ${arrow} </span>
    <#else>
        <#assign u = users[nextIndex]/>
        <a href="/user?handle=${u["handle"]}"
           style="color: var(--caption-color);
                text-decoration: none;"> ${arrow} </a>
    </#if>
</#macro>

<#macro showNextArrow index>
    <@showArrow index=index arrow="→" lastIndex=users?size - 1 nextIndex=index + 1/>
</#macro>

<#macro showPrevArrow index>
    <@showArrow index=index arrow="←" lastIndex=0 nextIndex=index - 1/>
</#macro>

<@c.page>
    <#if handle?has_content>
        <#assign index = c.findIndexBy(users, "handle", handle)!/>
    <#else>
        <#assign index = c.findIndexBy(users, "id", user_id)!/>
    </#if>

    <#if index?has_content>
        <#assign u = users[index]/>
        <div class="user-profile"
             style="border: 1px solid var(--border-color);
                border-radius: var(--border-radius);
                overflow: hidden;">
            <div class="info" style="float: left;">
            <span style="font-size: 1.5rem;
                    padding: 0 0.5rem;">
                    <@c.userlink u false/>
            </span>
                <ul>
                    <li>
                        id: ${u.id}
                    </li>
                    <li>
                        handle: ${u.handle}
                    </li>
                    <li>
                        name: ${u.name}
                    </li>
                    <li>
                        <@c.userpostslink u.id/>
                    </li>
                </ul>
            </div>
            <img src="img/no-title.jpg" alt="profile photo"
                 style="float: right;">
        </div>

        <div class="arrows" style="font-size: 1.75rem;">
            <@showPrevArrow index/>
            <@showNextArrow index/>
        </div>

    <#else>
        No such user
    </#if>
</@c.page>
