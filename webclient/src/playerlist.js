import { LitElement, html } from "https://cdn.jsdelivr.net/gh/lit/dist@3/all/lit-all.min.js";

/**
 * The player list
 * @element player-list
 */
export class PlayerList extends LitElement {
    static get properties() {
      return { players: { type: Array } };
    }

    constructor() {
        super();
        this.players = [];

        setTimeout(() => {
            this.fetchData();
        }, 50);
    }

    fetchData() {
        fetch("/api/players")
            .then(response => response.json())
            .then(data => {
              this.players = data;
            });
        setTimeout(() => {
            this.fetchData();
        }, 50);
    }
  
    render() {
      return html`
        <h3>Online:</h3>
        <ul>
          ${
            Object.values(this.players)
                .map(player => html`<li>${player.name}</li>`)
            }
        </ul>
      `;
    }
}

customElements.define("player-list", PlayerList);