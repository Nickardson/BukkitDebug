function openDirectory(parent, directory) {
    $.ajax({
        url: "/api/run/",
        data: {
            file: 'fs/files.js',
            directory: directory || '.'
        },
        success: function (data) {
            createTree(JSON.parse(data)).appendTo($(parent));
        }
    });
}

function editFile(path) {
    if (path.indexOf("\\") == 0 || path.indexOf("/") == 0) {
        path = path.substr(1);
    }

    editor.getSession().setMode(getModeForPath(path).mode);

    $.ajax({
        url: "/api/run/",
        data: {
            file: 'fs/read_file.js',
            path: path
        },
        success: function (data) {
            editor.setValue(data);
            editor.gotoLine(0);
        }
    });
}

function createTree(data, isRoot) {
    var ls = $('<ul class="list-unstyled"></ul>'),
        list = data.list.sort(function(a, b) {
            if (a.directory == b.directory) {
                return a.name > b.name;
            } else {
                if (a.directory) {
                    return -1;
                }
                return 1;
            }
        });

    for (var i = 0; i < list.length; i++) {
        if (list[i].directory) {
            $('<li data-directory="' + list[i].path + '" class="folder '+(isRoot?'folder-root':'')+' folder-closed"><div class="file-header"><span class="fs-icon glyphicon glyphicon-folder-close"></span>' + data.list[i].name + '</div></li>')
                .appendTo(ls)
                .find('.file-header')
                .click(function () {
                    $(this)
                        .toggleClass('folder-closed')
                        .toggleClass('folder-open')
                        .find("span.glyphicon")
                        .toggleClass("glyphicon-folder-close")
                        .toggleClass("glyphicon-folder-open");

                    if ($(this).hasClass('folder-open')) {
                        openDirectory($(this).parent(), $(this).parent().data('directory'));
                    } else {
                        $(this).parent().find('ul').remove();
                    }
                });
        } else {
            $('<li data-file="' + list[i].path + '" class="file"><span class="fs-icon glyphicon glyphicon-file"></span>' + data.list[i].name + '</li>').appendTo(ls).click(function () {
                editFile($(this).data("file"));
            });
        }
    }
    return ls;
}

openDirectory('#pane');

var editor = ace.edit("editor");
editor.setTheme("ace/theme/monokai");
editor.setReadOnly(true);