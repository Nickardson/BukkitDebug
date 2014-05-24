var editor = ace.edit("editor");
editor.getSession().setMode("ace/mode/javascript");

editor.load = function(url){
    $.ajax({
        "url": url,
        "dataType": "text"
    }).done(function (src) {
        editor.setValue(src);
        editor.gotoLine(0)
    });
};

function scrollConsoleDown() {
    var out = $("#console-output")[0];
    out.scrollTop = out.scrollHeight;
}

function appendConsole(d) {
    $("<div>").appendTo("#console-output").append(d);
    scrollConsoleDown();
}

editor.commands.addCommand({
    name: "send",
    bindKey: {win: "Ctrl-Enter", mac: "Command-Option-Enter"},
    exec: function () {
        runCode();
    }
});

editor.setOption("maxLines",25);

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
        $("#console-output").empty();
    });
});