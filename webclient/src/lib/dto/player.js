/**
 * @class PlayerDTO
 * @description The player data transfer object
 * @property {string} uuid The player uuid
 * @property {string} name The player name
 * @property {number} ping The players latency
 * @property {number} x The players x position
 * @property {number} y The players y position
 * @property {number} z The players z position
 */
export class PlayerDTO {
    constructor(name, profile) {
        this.name = name;
        this.profile = profile;
    }
}