function run(code, callback) {
    $.ajax({
        url: "/eval",
        data: {
            "code": code
        },
        "success": callback
    });
}

function runFile(url, callback, preface) {
    $.ajax({
        url: url,
        success: function (data) {
            run((preface || "") + data, callback);
        },
        dataType: "text"
    });
}

function inspectFields(query) {
    runFile("/js/server/jsonReflect.js", function (result) {
        result = JSON.parse(result);

        appendConsole(JSON.stringify(result));
    }, "var object = " + query + "; ");
}