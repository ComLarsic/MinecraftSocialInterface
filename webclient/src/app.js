import { LitElement, html, css } from "https://cdn.jsdelivr.net/gh/lit/dist@3/all/lit-all.min.js";
import "./components/playerlist.js";
import "./components/chatbox.js";
import "./components/auth/microsoftlogin.js";
import { AuthManager } from "./lib/auth/authmanager.js";
import { SocketManager } from "./lib/socket/socketmanager.js";
import { Chat } from "./lib/chat.js";

window.onload = () => {
    const routeParams = new URLSearchParams(window.location.search);
    const close = routeParams.get("close");
    if (close == "true") {
        window.close();
    }
}

/**
 * The app root component
 * @element app-root
 */
export class App extends LitElement {
    static get properties() {
      return {
          isLoggedIn: { type: Boolean }
      };
    }

    static get styles() {
      // Minecraft adds a black overlay to the background image
      return css`
        .overlay {
          z-index: -100;
          position: fixed;
          top: 0;
          left: 0;
          width: 100%;
          height: 100%;
          background-color: rgba(0, 0, 0, 0.5);
        }
      `;
    }

    constructor() {
        super();
        this.isLoggedIn = false;
        SocketManager.init()
            .then(setTimeout(() => {
                AuthManager.init();
            }, 200));
        const checkLoggedIn = setInterval(async () => {
            if (await AuthManager.isLoggedIn()) {
                clearInterval(checkLoggedIn);
                Chat.init();
                this.isLoggedIn = true;
            }
        }, 200);
    }
  
    render() {
        // If the user is not logged in, show the login button
        if (!this.isLoggedIn) {
          return html`
            <div class="overlay"></div>
            <microsoft-login-button></microsoft-login-button>
          `;
        }

        return html`
          <div class="overlay"></div>

          <player-list></player-list>
          <chat-box></chat-box>
        `;
    }

    _handleInput(e) {
        this.name = e.target.value;
    }
}

customElements.define("app-root", App);