import { LitElement, html, css } from "https://cdn.jsdelivr.net/gh/lit/dist@3/all/lit-all.min.js";

export class ChatBox extends LitElement {
    static get properties() {
        return {
            messages: { type: Array }
        };
    }

    static get styles() {
        return css`
            textarea {
                width: 100%;
                height: 400px;
                overflow: scroll;
                auto-scroll: true;
            }

            input {
                width: 100%;
            }
        `;
    }

    constructor() {
        super();
        this.messages = [];

        setTimeout(() => {
            this.fetchData();
        }, 50);
    }

    fetchData() {
        fetch("/api/chat/log")
            .then(response => response.json())
            .then(data => {
                this.messages = data;
            });
        setTimeout(() => {
            this.fetchData();
        }, 50);
    }

    render() {
        return html`
            <textarea readonly>${this.messages.map(formatMessage)}</textarea>
            <input type="text" @change="${this._handleInput}" @keypress="${this._handleEnter}"/>
        `;
    }

    _handleInput(e) {
        this._handleSend(e.target.value);
        e.target.value = "";
    }

    _handleSend(message) {
        fetch("/api/chat/send?message=" + message, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        });
    }
}

/**
 * Format the message as string
 * @param {*} message 
 * @returns {string} The formatted chat message
 */
const formatMessage = (message) => {
    let name = message.senderName == undefined ? ">" : "[" + message.senderName + "]: ";
    return name + message.contents + "\n";
}

customElements.define("chat-box", ChatBox);