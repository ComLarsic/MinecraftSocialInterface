/// The profile repository
const routes = {
    profile: "/api/profiles",
};

/**
 * @class Profile
 * @description Represents a profile
 * @property {string} uuid The profile uuid
 * @property {boolean} is_active Whether the profile is active
 */
export class Profile {
    constructor(dto) {
        this.uuid = dto.uuid;
        this.is_active = dto.is_active;
        this.skin_url = dto.skin_url;
    }
}

/**
 * @class ProfileList
 * @description The profile list handler
 */
export class ProfileList {
    /**
     * Get the profile list
     * @returns {Profile[]} The profile list
     */
    static async get() {
        const accessToken = localStorage.getItem("accessToken");
        if (accessToken === null) {
            throw new Error("Not logged in");
        }
        const response = await fetch(routes.profile, {
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + accessToken,
            },
        });
        const json = await response.json();
        return json;
    }

    /**
     * Get a profile by uuid
     * @param {string} uuid The profile uuid
     * @returns {Profile} The profile
     */
    static async getByUUID(uuid) {
        const accessToken = localStorage.getItem("accessToken");
        if (accessToken === null) {
            throw new Error("Not logged in");
        }
        const response = await fetch(routes.profile + "?id=" + uuid, {
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + accessToken,
            },
        });
        const json = await response.json();
        return json;
    }
}