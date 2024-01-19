import { PlayerDTO } from "./dto/player.js";
import { Profile, ProfileList } from "./profile.js";

/// The routes for the player api
const routes = {
    players: "/api/players",
}

/**
 * @class Player
 * @description Represents a player
 * @property {string} uuid The player uuid
 * @property {string} name The player name
 * @property {number} ping The players latency
 * @property {number} x The players x position
 * @property {number} y The players y position
 * @property {number} z The players z position
 */
export class Player {
    constructor(dto) {
        this.uuid = dto.uuid;
        this.name = dto.name;
        this.name = dto.name;
        this.ping = dto.ping;
        this.x = dto.x;
        this.y = dto.y;
        this.z = dto.z;
    }

    /**
     * Get the player profile
     * @returns {Profile} The player data transfer object
     */
    async getProfile() {
        return ProfileList.getByUUID(this.uuid);
    }
}


/**
 * @class PlayerList
 * @description The player list handler
 */
export class PlayerList {
    /**
     * Get the list of online players
     * @returns {Player[]} The player list
     */
    static async getOnline() {
        const accessToken = localStorage.getItem("accessToken");
        if (accessToken === null) {
            throw new Error("Not logged in");
        }
        const response = await fetch(routes.players, {
            headers: {
                "Content-Type": "application/json",
                "Authorization": accessToken,
            },
        });
        const json = await response.json();
        return json.map(x => new Player(x));
    }
}