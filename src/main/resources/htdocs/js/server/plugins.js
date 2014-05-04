var table = [];

Bukkit.pluginManager.plugins.forEach(function (plugin) {
    var name = plugin.name;

    var infoButton = '<button title="Info" data-plugin="' + name + '" class="btn btn-info plugin-query"><span class="glyphicon glyphicon-question-sign"></span></button>';

    if (name == 'BukkitDebug') {
        table.push([
            name,
            '<span class="label label-success">N/A</span>',
            '<span class="label label-primary">N/A</span>',
            infoButton
        ])
    } else {
        var onOffButton = '<button data-plugin="' + name + '" class="btn btn-' + (plugin.isEnabled() ? "success" : "danger") + ' plugin-toggle"><span class="glyphicon glyphicon-' + (plugin.isEnabled() ? "ok" : "remove") + '"></span></button>';

        table.push([
            name,
            onOffButton,
            '<button title="Reload" data-plugin="' + name + '" class="btn btn-primary plugin-reload"><span class="glyphicon glyphicon-refresh"></span></button>',
            infoButton
        ]);
    }
});

writeTable(table);
