/**
 * Message hadlers
 */
export const MessageHandlers = {
    NONE: 'NONE',
    AUTH: 'AUTH',
    CHAT: 'CHAT',
};

/**
 * Message types
 */
export const MessageTypes = {
    REGISTER: 'REGISTER',
    UPDATE: 'UPDATE',
    UNREGISTER: 'UNREGISTER',
    FAILURE: 'FAILURE',
    UNAUTHORIZED: 'UNAUTHORIZED',
};

/**
 * @class Message
 * @description A message sent from the client to the server
 * @property {string} handler The handler for the message
 * @property {string} type The type of the message
 * @property {string?} accessToken The access token of the message
 * @property {any} data The data of the message
 */
export class Message {
    constructor(handler, type, accessToken, data) {
        this.handler = handler;
        this.type = type;
        this.accessToken = accessToken;
        this.data = data;
    }
}