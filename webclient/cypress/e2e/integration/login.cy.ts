describe("Login Test", () => {
    beforeEach(() => {
        cy.visit("http://localhost:8080/");
        // Clear local storage before each test
        cy.clearAllLocalStorage();
    });
    
    it.apply("shows login button", () => {
        cy.get(".microsoft-login-button").should("exist");
    });

    it.only("shows chatroom after login", () => {
        cy.get(".chat-box").should("not.exist");
        cy.get(".player-list").should("not.exist");
    });
});