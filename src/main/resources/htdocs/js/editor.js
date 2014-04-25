var editor = ace.edit("editor");
editor.setTheme("ace/theme/monokai");
editor.getSession().setMode("ace/mode/javascript");

$(document).ready(function(){
    var heightUpdateFunction = function() {
        // http://stackoverflow.com/questions/11584061/
        var newHeight = (editor.getSession().getScreenLength() + 1)
            * editor.renderer.lineHeight
            + editor.renderer.scrollBar.getWidth();

        $('#editor').height(newHeight.toString() + "px");
        $('#editor-section').height(newHeight.toString() + "px");

        // This call is required for the editor to fix all of
        // its inner structure for adapting to a change in size
        editor.resize();
    };

    // Set initial size to match initial content
    heightUpdateFunction();

    // Whenever a change happens inside the ACE editor, update
    // the size again
    editor.getSession().on('change', heightUpdateFunction);
});

editor.load = function(url){
    $.ajax({
        "url": url,
        "dataType": "text"
    }).done(function (src) {
        editor.setValue(src);
        editor.gotoLine(0)
    });
};