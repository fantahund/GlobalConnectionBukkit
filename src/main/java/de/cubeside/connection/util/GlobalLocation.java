package de.cubeside.connection.util;

import de.cubeside.connection.ConnectionAPI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class GlobalLocation implements ConfigurationSerializable, Comparable<GlobalLocation> {
    
    private static ConnectionAPI connectionApi;
    
    private static String getThisServerName() {
        if (connectionApi == null) {
            connectionApi = Bukkit.getServicesManager().load(ConnectionAPI.class);
        }
        return connectionApi.getThisServer().getName();
    }
    
    private String server;
    private String world;
    private double x, y, z;
    private float yaw, pitch;
    
    public GlobalLocation(String server, String world, double x, double y, double z, float yaw,
            float pitch) {
        super();
        this.server = server;
        this.world = Objects.requireNonNull(world);
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    public GlobalLocation(String world, double x, double y, double z, float yaw, float pitch) {
        this(getThisServerName(), world, x, y, z, yaw, pitch);
    }
    
    public GlobalLocation(String server, String world, double x, double y, double z) {
        this(server, world, x, y, z, 0.0f, 0.0f);
    }
    
    public GlobalLocation(String world, double x, double y, double z) {
        this(world, x, y, z, 0.0f, 0.0f);
    }
    
    public GlobalLocation(Location loc) {
        this(getThisServerName(), loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(),
                loc.getYaw(), loc.getPitch());
    }
    
    public GlobalLocation(Map<String, Object> serialized) {
        this.server = Objects.requireNonNull((String) serialized.get("server"));
        this.world = Objects.requireNonNull((String) serialized.get("world"));
        this.x = ((Number) serialized.get("x")).doubleValue();
        this.y = ((Number) serialized.get("y")).doubleValue();
        this.z = ((Number) serialized.get("z")).doubleValue();
        this.yaw = serialized.containsKey("yaw") ? ((Number) serialized.get("yaw")).floatValue()
                : 0.0f;
        this.pitch =
                serialized.containsKey("pitch") ? ((Number) serialized.get("pitch")).floatValue()
                        : 0.0f;
    }
    
    public Location getLocation() {
        if (!isOnThisServer()) {
            return null;
        }
        // Do not cache, Location is mutable...
        return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z, this.yaw,
                this.pitch);
    }
    
    public GlobalLocation toBlockLocation() {
        return new GlobalLocation(this.server, this.world, Math.floor(this.x), Math.floor(this.y),
                Math.floor(this.z));
    }
    
    public GlobalLocation stripDirection() {
        return new GlobalLocation(this.server, this.world, this.x, this.y, this.z);
    }
    
    public String getServer() {
        return this.server;
    }
    
    public boolean isOnThisServer() {
        return this.server.equals(getThisServerName());
    }
    
    public String getWorld() {
        return this.world;
    }
    
    public World getBukkitWorld() {
        return !isOnThisServer() ? null : Bukkit.getWorld(this.world);
    }
    
    public double getX() {
        return this.x;
    }
    
    public int getBlockX() {
        return (int) Math.floor(this.x);
    }
    
    public double getY() {
        return this.y;
    }
    
    public int getBlockY() {
        return (int) Math.floor(this.y);
    }
    
    public double getZ() {
        return this.z;
    }
    
    public int getBlockZ() {
        return (int) Math.floor(this.z);
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>(7);
        result.put("server", this.server);
        result.put("world", this.world);
        result.put("x", this.x);
        result.put("y", this.y);
        result.put("z", this.z);
        if (this.yaw != 0.0f) {
            result.put("yaw", this.yaw);
        }
        if (this.pitch != 0.0f) {
            result.put("pitch", this.pitch);
        }
        return result;
    }
    
    @Override
    public int compareTo(GlobalLocation other) {
        int result = this.server.compareTo(other.server);
        if (result != 0) {
            return result;
        }
        
        result = this.world.compareTo(other.world);
        if (result != 0) {
            return result;
        }
        
        result = Double.compare(this.x, other.x);
        if (result != 0) {
            return result;
        }
        
        result = Double.compare(this.y, other.y);
        if (result != 0) {
            return result;
        }
        
        result = Double.compare(this.z, other.z);
        if (result != 0) {
            return result;
        }
        
        result = Float.compare(this.yaw, other.yaw);
        if (result != 0) {
            return result;
        }
        
        result = Float.compare(this.pitch, other.pitch);
        return result;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SafeLocation[");
        builder.append("server: ").append(this.server);
        builder.append(", ").append("world: ").append(this.world);
        builder.append(", ").append("x: ").append(this.x);
        builder.append(", ").append("y: ").append(this.y);
        builder.append(", ").append("z: ").append(this.z);
        if (this.yaw != 0.0f || this.pitch != 0.0f) {
            builder.append(", ").append("yaw: ").append(this.yaw);
            builder.append(", ").append("pitch: ").append(this.pitch);
        }
        builder.append("]");
        return builder.toString();
    }
    
    public boolean isSimilar(GlobalLocation other) {
        return other != null && this.server.equals(other.server) && this.world.equals(other.world)
                && this.x == other.x && this.y == other.y && this.z == other.z;
    }
    
    public boolean isSimilar(Location loc) {
        return loc != null && isOnThisServer() && this.world.equals(loc.getWorld().getName())
                && this.x == loc.getX() && this.y == loc.getY() && this.z == loc.getZ();
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GlobalLocation)) {
            return false;
        }
        
        GlobalLocation loc = (GlobalLocation) other;
        if (!this.server.equals(loc.server)) {
            return false;
        }
        if (!(this.world.equals(loc.world))) {
            return false;
        }
        if (this.x != loc.x) {
            return false;
        }
        if (this.y != loc.y) {
            return false;
        }
        if (this.z != loc.z) {
            return false;
        }
        if (this.yaw != loc.yaw) {
            return false;
        }
        if (this.pitch != loc.pitch) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int result = this.server.hashCode();
        result = 31 * result + this.world.hashCode();
        result = 31 * result + Double.hashCode(this.x);
        result = 31 * result + Double.hashCode(this.y);
        result = 31 * result + Double.hashCode(this.z);
        result = 31 * result + Float.hashCode(this.yaw);
        result = 31 * result + Float.hashCode(this.pitch);
        return result;
    }
    
}
