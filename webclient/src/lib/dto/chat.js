/**
 * @class ChatMessage
 * @description Represents a chat message
 * @property {string} sender The sender uuid
 * @property {string} senderName The sender name
 * @property {string} contents The message
 */
export class ChatMessageDTO {
    constructor(senderName, sender, contents) {
        this.sender = sender;
        this.senderName = senderName;
        this.contents = contents;
    }
}