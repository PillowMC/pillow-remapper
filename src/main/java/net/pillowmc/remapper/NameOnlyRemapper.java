package net.pillowmc.remapper;

import java.util.Map;
import java.util.function.Predicate;

import org.objectweb.asm.commons.Remapper;

public class NameOnlyRemapper extends Remapper {
    private final Map<String, String> classes, methods, fields;
    private Predicate<String> nameFilter;
    public NameOnlyRemapper(Map<String, String> classes, Map<String, String> methods, Map<String, String> fields) {
        this.classes = classes;
        this.methods = methods;
        this.fields = fields;
    }

    public NameOnlyRemapper(Map<String, String> classes, Map<String, String> methods, Map<String, String> fields, Predicate<String> nameFilter) {
        this(classes, methods, fields);
        this.nameFilter = nameFilter;
    }

    @Override
    public String map(String internalName) {
        return classes.getOrDefault(internalName, internalName);
    }
    @Override
    public String mapMethodName(String owner, String name, String descriptor) {
        return mapInvokeDynamicMethodName(name, descriptor);
    }
    @Override
    public String mapFieldName(String owner, String name, String descriptor) {
        if (nameFilter != null && !nameFilter.test(name)) return name;
        return fields.getOrDefault(name, name);
    }
    @Override
    public String mapInvokeDynamicMethodName(String name, String descriptor) {
        if (nameFilter != null && !nameFilter.test(name)) return name;
        return methods.getOrDefault(name+descriptor, methods.getOrDefault(name, name));
    }
}
