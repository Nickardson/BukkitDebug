var result = [];

var fields = reflection.getClass(object).getDeclaredFields();
for (var i = 0; i < fields.length; i++) {
    var field = fields[i];
    result.push({
        name: String(field.name),
        type: String(field.type.name)
    });
}

print(JSON.stringify(result));
