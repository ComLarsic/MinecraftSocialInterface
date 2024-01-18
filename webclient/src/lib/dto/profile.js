/**
 * @class ProfileDTO
 * @description The profile data transfer object
 * @property {string} uuid The player uuid
 * @property {boolean} is_active Whether the player is active
 */
export class ProfileDTO {
    constructor(uuid, is_active) {
        this.uuid = uuid;
        this.is_active = is_active;
        this.skin_url = none;
    }
}