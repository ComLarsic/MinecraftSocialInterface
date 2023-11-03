import { LitElement, html } from "https://cdn.jsdelivr.net/gh/lit/dist@3/all/lit-all.min.js";
import "./playerlist.js";
import "./chatbox.js";

/**
 * The app root component
 * @element app-root
 */
export class App extends LitElement {
  static get properties() {
    return { name: { type: String } };
  }
  
  render() {
    return html`
      <player-list></player-list>
      <chat-box></chat-box>
    `;
  }

  _handleInput(e) {
    this.name = e.target.value;
  }
}

customElements.define("app-root", App);