package com.hotmail.steven.main;

import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public enum Settings {

    PARTICLE_ENABLED(true, "particle_enabled"),
    PARTICLE_TYPE("", "particle.type"),
    PARTICLE_TYPE_FOUND("", "particle.found.type"),

    NEAR_RADIUS(0, "near.radius"),
    NEAR_MESSAGE("", "near.message.message"),

    REWARD_FIND_TITLE_MAJOR("", "rewards.find.title.major"),
    REWARD_FIND_TITLE_MINOR("", "rewards.find.title.minor"),
    REWARD_FIND_BROADCAST("", "rewards.find.broadcast"),
    REWARD_FIND_COMMANDS("", "rewards.find.command"),

    REWARD_FINDALL_TITLE_MAJOR("", "rewards.find-all.title.major"),
    REWARD_FINDALL_TITLE_MINOR("", "rewards.find-all.title.minor"),
    REWARD_FINDALL_BROADCAST("", "rewards.find-all.broadcast"),
    REWARD_FINDALL_COMMANDS("", "rewards.find-all.command"),

    MYSQL_ENABLED("", "mysql.enabled"),
    MYSQL_USER("", "mysql.user"),
    MYSQL_PASS("", "mysql.pass"),
    MYSQL_DATABASE("", "mysql.database"),
    MYSQL_PREFIX("", "mysql.table-prefix");

    Settings(Object value, String node)
    {
        this.value = value;
        this.node = node;
    }

    private Object value;
    private String node;

    public String getNode()
    {
        return node;
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

    public boolean getBoolean()
    {
        return (boolean)value;
    }

    public String getString()
    {
        return (String)value;
    }

    public Integer getInteger()
    {
        return (Integer)value;
    }

    public List<String> getStringList()
    {
        return (List<String>)value;
    }

    public Particle getParticle()
    {
        return Particle.valueOf(((String)value).toUpperCase());
    }

}
