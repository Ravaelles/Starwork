package bwapi;

import java.util.HashMap;
import java.util.Map;

/**
 * Functions in BWAPI may set an error code. To retrieve the error code, call Game::getLastError.
 */
public class Error {

    private int id;

    private Error(int id) {
        this.id = id;
    }


    public static Error Unit_Does_Not_Exist;

    public static Error Unit_Not_Visible;

    public static Error Unit_Not_Owned;

    public static Error Unit_Busy;

    public static Error Incompatible_UnitType;

    public static Error Incompatible_TechType;

    public static Error Incompatible_State;

    public static Error Already_Researched;

    public static Error Fully_Upgraded;

    public static Error Currently_Researching;

    public static Error Currently_Upgrading;

    public static Error Insufficient_Minerals;

    public static Error Insufficient_Gas;

    public static Error Insufficient_Supply;

    public static Error Insufficient_Energy;

    public static Error Insufficient_Tech;

    public static Error Insufficient_Ammo;

    public static Error Insufficient_Space;

    public static Error Invalid_Tile_Position;

    public static Error Unbuildable_Location;

    public static Error Unreachable_Location;

    public static Error Out_Of_Range;

    public static Error Unable_To_Hit;

    public static Error Access_Denied;

    public static Error File_Not_Found;

    public static Error Invalid_Parameter;

    public static Error None;

    public static Error Unknown;

    private static Map<Long, Error> instances = new HashMap<Long, Error>();

    private Error(long pointer) {
        this.pointer = pointer;
    }

    private static Error get(long pointer) {
        Error instance = instances.get(pointer);
        if (instance == null) {
            instance = new Error(pointer);
            instances.put(pointer, instance);
        }
        return instance;
    }

    private long pointer;

    public String c_str() {
        return c_str_native(pointer);
    }

    public String toString(){
        return c_str();
    }

    private native String c_str_native(long pointer);
}