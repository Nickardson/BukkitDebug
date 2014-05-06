function sendCode(code, silent) {
    return $.ajax({
        url: "/eval",
        data: {
            "code": code
        },
        "success": function (d) {
            if (!silent) {
                appendConsole(d);
            }
        },
        "error": function (e, b, status) {
            if (!silent) {
                appendConsole($('<span class="label label-danger">' + status + '</span>'));
                console.log("error", status);
            }
        }
    });
}

function runCode(silent) {
    $("#runcode").addClass("disabled");
    sendCode(editor.getValue(), silent).complete(function () {
        $("#runcode").removeClass("disabled");
    });
}

$(function() {
    $("#navbar").load("/ajax/navbar.html");

    editor.load("/code/example.js");

    $("#runcode").click(function () {
        runCode();
    });

    $("#clearconsole").click(function () {
        $("#consoleoutput").empty();
    });
});