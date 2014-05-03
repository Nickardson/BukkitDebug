var table = [];

Bukkit.pluginManager.plugins.forEach(function (plugin) {
    var name = plugin.name;

    if (name == 'BukkitDebug') {
        table.push([
            name,
            '<span class="label label-success">N/A</span>',
            '<span class="label label-danger">N/A</span>',
            '<span class="label label-primary">N/A</span>',
            '<span class="label label-info">N/A</span>'
        ])
    } else {
        table.push([
            name,
            '<button title="Enable" data-plugin="' + name + '" class="btn btn-success plugin-enable"><span class="glyphicon glyphicon-ok"></span></button>',
            '<button title="Disable" data-plugin="' + name + '" class="btn btn-danger plugin-disable"><span class="glyphicon glyphicon-remove"></span></button>',
            '<button title="Reload" data-plugin="' + name + '" class="btn btn-primary plugin-reload"><span class="glyphicon glyphicon-refresh"></span></button>',
            '<button title="Info" data-plugin="' + name + '" class="btn btn-info plugin-query"><span class="glyphicon glyphicon-question-sign"></span></button>'
        ]);
    }
});

writeTable(table);
