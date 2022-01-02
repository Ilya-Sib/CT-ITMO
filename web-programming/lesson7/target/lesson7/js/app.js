window.notify = function (message) {
    $.notify(message, {
        position: "right bottom",
        className: "success"
    });
}

window.ajax = function (data, success, $error, url = "") {
    $.ajax({
        type: "POST",
        url: url,
        dataType: "json",
        data: data,
        success: function (response) {
            if (response["error"]) {
                $error.text(response["error"]);
            } else if (response["redirect"]) {
                location.href = response["redirect"];
            } else {
                success(response)
            }
        }
    });
}