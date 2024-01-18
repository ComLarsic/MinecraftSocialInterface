import "../../support/component";

/**
 * Retreives the root element of the app
 * @returns {Cypress.Chainable<JQuery<HTMLElement>>}
 */
const root = (): Cypress.Chainable<JQuery<HTMLElement>> => cy.get("app-root").shadow();

describe("Login Test", () => {
    beforeEach(() => {
        cy.visit("http://localhost:8080/");
        // Clear local storage before each test
        cy.clearAllLocalStorage();
    });
    
    it.only("shows login button", () => {   
        root().find("microsoft-login-button").should("exist");
    });

    it.only("shows chatroom after login", () => {
        root().find("chat-box").should("not.exist");
        root().find("player-list").should("not.exist");
    });
});