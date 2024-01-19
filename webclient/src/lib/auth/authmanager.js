import { Message, MessageHandlers, MessageTypes } from "../socket/message";
import { SocketManager } from "../socket/socketmanager";

/**
 * @class AuthManager
 * @description The auth manager
 */
export class AuthManager {
    /// The url for login
    static _loginUrl = undefined;
    /// The access token
    static _accessToken = undefined;

    /**
     * @description Initialize the auth manager
     */
    static async init() {
        const cached = localStorage.getItem("accessToken");
        if (cached !== null) {
            this._accessToken = cached;
            return;
        }

        SocketManager.sendMessage(new Message(MessageHandlers.AUTH, MessageTypes.REGISTER, null, {}));
        SocketManager.registerCallback((message) => {
            // The first message is the login url
            if (message.handler !== MessageHandlers.AUTH) {
                return;
            }
            if (message.type === MessageTypes.REGISTER) {
                this._loginUrl = message.data;
                return;
            }
            if (message.type === MessageTypes.UPDATE) {
                // The second message is the access token
                this._accessToken = message.data;
                localStorage.setItem("accessToken", this._accessToken);
                return;
            }
        });
    }

    /**
     * @description Returns whether the user is logged in
     * @returns {boolean} Whether the user is logged in
     */
    static isLoggedIn() {
        return this._accessToken !== undefined;
    }

    /**
     * @description Get the login url
     * @returns {string} The login url
     */
    static async getLoginUrl() {
        return new Promise((resolve, reject) => {
            const interval = setInterval(() => {
                if (this._loginUrl !== undefined) {
                    clearInterval(interval);
                    resolve(this._loginUrl);
                }
            }, 100);
        });
    }
}