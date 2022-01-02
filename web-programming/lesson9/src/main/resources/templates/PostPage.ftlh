<#-- @ftlvariable name="posts" type="ru.itmo.wp.domain.Post[]" -->
<#import "commons.ftlh" as c>

<@c.page>
    <#if post??>
        <@c.post post=post/>

        <div class="form" style="margin-bottom: 2rem">
            <div class="header">Write Comment</div>
            <div class="body">
                <form method="post" action="">
                    <input type="hidden" name="action" value="writeComment"/>
                    <div class="field">
                        <div class="name">
                            <label for="text">Text</label>
                        </div>
                        <div class="value">
                            <textarea id="text" name="text">${comment.text!}</textarea>
                        </div>
                        <@c.error "post.text"/>
                    </div>
                    <div class="button-field">
                        <input type="submit" value="Write">
                    </div>
                </form>
            </div>
        </div>

        <div class="post-comments datatable">
            <div class="caption">Comments</div>
            <table>
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Author</th>
                    <th>Text</th>
                    <th>Created Time</th>
                </tr>
                </thead>
                <tbody>
                <#if post.comments?size==0>
                    <tr class="noData">
                        <td colspan="4">
                            No data
                        </td>
                    </tr>
                <#else>
                    <#list post.comments as comment>
                        <tr>
                            <td class="id">${comment.id}</td>
                            <td class="author">${comment.user.login}</td>
                            <td class="text">${comment.text}</td>
                            <td class="creationTime">${comment.creationTime}</td>
                        </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
        </div>
    <#else>
        <h1>Post not found</h1>
    </#if>
</@c.page>
