var plugin = Bukkit.pluginManager.getPlugin(args.get("name")[0]);

var entityMap = {
    "&": "&amp;",
    "<": "&lt;",
    ">": "&gt;",
    '"': '&quot;',
    "'": '&#39;',
    "/": '&#x2F;'
};

function escapeHtml(string) {
    return String(string).replace(/[&<>"'\/]/g, function (s) {
        return entityMap[s];
    });
}

function set(key, value) {
    if (value != null) {
        write('<dt>' + key + '</dt>');
        write('<dd>' + value + '&nbsp;</dd>');
    }
}

write('<dl class="dl-horizontal">');

set('Name', plugin.name);

// Write whether enabled
if (plugin.enabled) {
    set('Enabled', '<span class="label label-success">Yes</span>');
} else {
    set('Enabled', '<span class="label label-danger">No</span>');
}

set('Description', plugin.description.description);
set('Version', plugin.description.version && '<code>' + plugin.description.version + '</code>');
set('Folder', plugin.dataFolder);
set('Main Class', plugin.description.main);

// Display links to the BukkitDev pages of the authors.
var authors = plugin.description.authors;
if (authors.size() > 0) {
    var authorList = [];
    for (i = 0; i < authors.size(); i++) {
        var author = authors.get(i);
        authorList.push('<a href="http://dev.bukkit.org/profiles/' + author + '/">' + author + '</a>');
    }
    set('Authors', authorList.join(", "));
}

// Link to the plugin website.
var site = plugin.description.website;
if (site != null) {
    set('Website', '<a href="' + site + '">' + site + '</a>');
}

set('Dependencies', plugin.description.depend);
set('Load After', plugin.description.softDepend);
set('Load Before', plugin.description.loadBefore);

// Commands
var commands = plugin.description.commands;
if (commands) {
    write('<dt>Commands</dt>');
    {
        var it = commands.entrySet().iterator();

        function add(key, value, ls) {
            if (value != null) {
                ls.push('<dt>' + escapeHtml(key) + '</dt>');
                ls.push('<dd>' + escapeHtml(value) + '</dd>');
            }
        }

        write('<dd>');
        while (it.hasNext()) {
            var c = it.next(),
                content = [];

            content.push('<dl>');
            add('Name', c.key, content);
            add('Description', c.value.get('description'), content);
            add('Aliases', c.value.get('aliases'), content);
            add('Permission', c.value.get('permission'), content);
            add('Usage', c.value.get('usage').replace("<command>", c.key), content);
            content.push('</dl>');

            write('<code class="popoverbtn btn btn-default btn-sm" data-placement="bottom" data-original-title="Command" data-container="#plugin-info-modal" data-toggle="popover" data-html="true" data-content="' + content.join('') + '">/' + c.key + '</code>');
        }
        write('</dd>');
    }
}

// Permissions
var permissions = plugin.description.permissions;
if (permissions.size() > 0) {
    write('<dt>Permissions</dt>');
    for (var i = 0; i < permissions.size(); i++) {
        var permission = permissions.get(i);
        write('<dd><b>' + permission.name + '</b></dd>');
        write('<dd><i>(' + permission.default + ')</i>: ' + permission.description + '</dd>');
    }
}

write('<script>$(".popoverbtn").popover()</script>');

write('</dl>');