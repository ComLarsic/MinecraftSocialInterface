import { LitElement, html, css } from "https://cdn.jsdelivr.net/gh/lit/dist@3/all/lit-all.min.js";
import { Player, PlayerList } from "../lib/player";
import { Profile } from "../lib/profile";

/**
 * The player list
 * @element player-list
 */
export class PlayerListElement extends LitElement {
    static get properties() {
        return {
            players: { type: Array },
            profiles: { type: {} },
        };
    }

    static get styles() {
        return css`
            h3 {
                color: #fff;
            }

            ul {
                color: #fff;
                list-style-type: none;
                margin: 0;
                padding: 0;
                background-color: rgba(0, 0, 0, 0.5);
                border: 1px solid #fff;
            }

            li {
                display: inline-block;
                margin: 0.5em;
            }

            .label {
                margin-left: 0.5em;
            }

            .player-list {
                margin: 0.5em;
            }

            .profile-card {
                display: flex;
                flex-direction: row;
                align-items: center;
                overflow: hidden;
            }

            img {
                width: 64px;
                height: 64px;
                border: 1px solid rgb(0, 0, 0);
                background-size: 800% 800%;
                background-position: -64px -64px;
            }
        `;
    }

    constructor() {
        super();
        this.players = [];
        this.profiles = [];

        setTimeout(async () => {
            await this.fetchData();
        }, 1000);
    }

    async fetchData() {
        this.players = await PlayerList.getOnline();
        Object.values(this.players).forEach(async player => {
            if (this.profiles[player.uuid] === undefined) {
                try {
                    let profile = await player.getProfile();
                    this.profiles[player.uuid] = profile;
                }
                catch {
                    this.profiles[player.uuid] = null;
                }
            }
        });
        localStorage.setItem("profiles", JSON.stringify(this.profiles));
        setTimeout(async () => {
            await this.fetchData();
        }, 5000);
    }

    render() {
        return html`
        <div class="player-list">
            <h3>Online:</h3>
            <ul>
              ${Object.values(this.players)
                    .map(player => formatProfileCard(player, this.profiles[player.uuid]))
                }
            </ul>
        </div>
      `; 
    }
}

/// Format a profile card
const formatProfileCard = (player, profile) => {
    if (profile === undefined) {
        return html`<li>${player.name}</li>`;
    }
    return html`<li>
        <div class="profile-card">
            <img style="background-image: url(${profile.skin_url})"/>
            <div class="label">
                <b>${player.name}</b> (${profile.uuid})
            </div>
        </div>
    </li>`;
}

customElements.define("player-list", PlayerListElement);