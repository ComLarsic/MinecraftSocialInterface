import { LitElement, html, css } from "https://cdn.jsdelivr.net/gh/lit/dist@3/all/lit-all.min.js";
import { AuthManager } from "../../lib/auth/authmanager";

/**
 * The microsoft login button
 * @element microsoft-login-button
 */
export class MicrosoftLoginButton extends LitElement {
    render() {
        return html`
            <button @click="${this._handleClick}">Login with Microsoft</button>
        `
    }

    async _handleClick() {
        await AuthManager.getLoginUrl().then(url => {
            window.open(url, "Login");
        });
    }
}

customElements.define("microsoft-login-button", MicrosoftLoginButton);