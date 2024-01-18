import { AuthManager } from "./auth/authmanager";
import { Message, MessageHandlers, MessageTypes } from "./socket/message";
import { SocketManager } from "./socket/socketmanager";

/// The routes for the chat api
const routes = {
    chatLog: "/api/chat/log",
};

/**
 * @class ChatMessage
 * @description Represents a chat message
 * @property {string} sender The sender uuid
 * @property {string} senderName The sender name
 * @property {string} contents The message
 */
export class ChatMessage {
    constructor(dto) {
        this.senderName = dto.senderName;
        this.sender = dto.sender;
        this.contents = dto.contents;
    }
} 

/**
 * @class Chat
 * @description The chat handler
 */
export class Chat {
    /// The list of callbacks for recieving a message
    static _onrecieved = [];

    /**
     * @description Initialize the chat 
     */
    static async init() {
        const accessToken = localStorage.getItem("accessToken");
        if (accessToken === null) {
            throw new Error("Not logged in");
        }
        SocketManager.sendMessage(new Message(MessageHandlers.CHAT, MessageTypes.REGISTER, accessToken, {}));
        SocketManager.registerCallback((message) => {
            this._onrecieved.forEach(callback => callback(message));
        });
    }

    /**
     * @description The repository for the chat
     * @returns {ChatMessage[]} The chat log 
     */
    static async getLog() {
        const accessToken = localStorage.getItem("accessToken");
        if (accessToken === null) {
            throw new Error("Not logged in");
        }
        const response = await fetch(routes.chatLog, {
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + accessToken,
            },
        });
        const json = await response.json();
        return json.map(x => new ChatMessage(x));
    }

    /**
     * @description The repository for the chat
     * @param {string} message The message to send
     */
    static async sendChat(message) {
        const accessToken = localStorage.getItem("accessToken");
        if (accessToken === null) {
            throw new Error("Not logged in");
        }
        SocketManager.sendMessage(new Message(MessageHandlers.CHAT, MessageTypes.UPDATE, accessToken, message));
    }

    /**
     * @description Register a callback for recieving a message
     * @param {(msg: string) => void} callback
     */
    static registerCallback(callback) {
        this._onrecieved.push(callback);
    }
}
