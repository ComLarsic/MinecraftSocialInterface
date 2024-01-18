package net.msimod.common.networking.dto;

import java.io.Serializable;

/**
 * The player dto
 */
public class PlayerDto implements Serializable {
    public String uuid;
    public String name;
    public int ping;
    public int x;
    public int y;
    public int z;
}
