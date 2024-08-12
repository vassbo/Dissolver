package net.vassbo.vanillaemc.config.model;

import java.util.Objects;

public class ConfigEntry<T> {


    final String property;
    final T aDefault;

    public ConfigEntry(
        String property,
        T aDefault
    ) {
        this.property = property;
        this.aDefault = aDefault;
    }

    public String getProperty() {
        return property;
    }

    public T getDefault() {
        return aDefault;
    }

    @Override
    public String toString() {
        return "new ConfigEntry(\"" + property + "\",\"" + aDefault + "\");";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ConfigEntry<?> that = (ConfigEntry<?>) o;
        return Objects.equals(property, that.property) && Objects.equals(aDefault, that.aDefault);
    }

    @Override
    public int hashCode() {
        return Objects.hash(property, aDefault);
    }
}