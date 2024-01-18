import { LitElement, html, css } from "https://cdn.jsdelivr.net/gh/lit/dist@3/all/lit-all.min.js";
import { Chat, ChatMessage } from "../lib/chat";

/**
 * The live chat box
 */
export class ChatBoxElement extends LitElement {
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

                background-color: rgba(0, 0, 0, 0.5);
                color: #fff;
            }

            input {
                width: 100%;
                background-color: rgba(0, 0, 0, 0.5);
                color: #fff;
            }
        `;
    }

    constructor() {
        super();
        this.messages = [];
        Chat.getLog()
            .then(data => {
                data.map(msg => this.messages.push(msg));
                this.messages = [...this.messages];
            });
        Chat.registerCallback(msg => {
            this.messages.push(new ChatMessage(msg.data));
            this.messages = [...this.messages];
        })
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

    async _handleSend(message) {
        await Chat.sendChat(message);
    };
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

customElements.define("chat-box", ChatBoxElement);