import { Message } from "./message";

/**
 * @class SocketManager
 * @description Manages the connection to the server socket
 * @property {WebSocket} _socket The connection to the server socket
 */
export class SocketManager {
    /// The connection to the server socket
    static _socket = undefined;
    /// The list of callbacks for recieving a message
    static _onrecieved = [];

    /**
     * @description Initialize the socket manager
     */
    static async init() {
        this._socket = new WebSocket("ws://localhost:8080/ws/socket")
        this._socket.onmessage = (message) => {
            this._onrecieved.forEach(callback => callback(JSON.parse(message.data)));
        };
    }

    /**
     * @description Get the socket when its ready
     */
    static async socket() {
        const checkCycles = 10;
        return new Promise((resolve, reject) => {
            let cycles = 0;
            const interval = setInterval(() => {
                if (this._socket !== undefined) {
                    clearInterval(interval);
                    resolve(this._socket);
                }
                if (cycles > checkCycles) {
                    reject("Failed to get socket")
                }
                cycles += 1;
            }, 100);
        });
    }

    /**
     * @description The repository for the chat
     * @param {string} message The message to send
     */
    static async sendMessage(message) {
        const socket = await this.socket();
        socket.send(JSON.stringify(message));
    }

    /**
     * @description Add a callback for when a message is recieved
     * @param {(msg: Message) => void} callback The callback
     */
    static async registerCallback(callback) {
        this._onrecieved.push(callback);
    }
}