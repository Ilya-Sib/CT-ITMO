<#-- @ftlvariable name="user" type="ru.itmo.wp.model.domain.User" -->
<#-- @ftlvariable name="users" type="ru.itmo.wp.model.domain.User[]" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="users datatable">
        <div class="caption">User</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <#if user?? && user.admin>
                    <th>Admin</th>
                </#if>
            </tr>
            </thead>
            <tbody>
            <template>
                <tr>
                    <td class="user-id"></td>
                    <td class="user_login"></td>
                    <#if user?? && user.admin>
                        <td class="user-admin">
                            <button type="button"></button>
                        </td>
                    </#if>
                </tr>
            </template>
            <tr class="noData">
                <td colspan="3">
                    No data
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <script>
        $(function () {
            const $noData = $(".users .noData");
            $noData.find("td").text("Loading...");

            window.ajax({
                    action: "findAll"
                },
                function (response) {
                    response["users"].forEach(function (user) {
                        $noData.hide();
                        const $tr = $($(".users template").prop("content")).clone();
                        $tr.find(".user-id").text(user["id"]);
                        $tr.find(".user_login").text(user["login"]);
                        if (user["admin"]) {
                            $tr.find(".user-admin button").text("disable");
                        } else {
                            $tr.find(".user-admin button").text("enable");
                        }
                        $tr.find("tr").attr("data-userId", user["id"]);
                        $tr.find("button").attr("data-userId", user["id"]);
                        $(".users tbody").append($tr);
                    });

                    $(".users tr").click(function () {
                        window.ajax({
                                action: "findUser",
                                userId: $(this).attr("data-userId")
                            },
                            function (response) {
                                notify(response["user"].creationTime);
                            });
                    });

                    $(".users button").click(function () {
                        const button = $(this);

                        window.ajax({
                                action: "addAdmin",
                                userId: $(this).attr("data-userId"),
                                buttonText: button.text()
                            },
                            function (response) {
                                if (button.text() === "enable") {
                                    button.text("disable");
                                    notify("Admin enabled");
                                } else {
                                    button.text("enable");
                                    notify("Admin disabled");
                                }
                            })
                    })
                })
        })
    </script>
</@c.page>
