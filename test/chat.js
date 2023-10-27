/**
 * This script tests chatting functionality of the api routes
 */

const url = "http://localhost";
const port = 8080;

/// Get the url for the api
const uri = (route) => url + ":" + port + "/" + route;

/// The status enum
const Status = {
    Success: "Success",
    Failure: "Failure"
}

/// Tests the chat message functionality
const sendMessageTest = async () => {
    const messages = [
        "Hello, world!",
        "Hello\nworld!",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
        "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
        "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
        "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    ];
    // The results
    let result = [];

    // Send each message
    for (const value of messages) {
        const response = await fetch(
            uri("chat/send?message=" + value), {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
        });
        if (response.ok) {
            result.push({
                status: Status.Success,
                message: value,
                code: response.status
            });
            continue;
        }
        // Handle exception
        result.push({
            status: Status.Failure,
            code: response.status,
            message: value,
            reason: response.statusText,
        });
    }
    return result;
}

/// Tests fetching the chat messages
const fetchMessagesTest = async () => {
    const response = await fetch(uri("chat/log"), {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        },
    });
    if (response.ok) {
        return {
            status: Status.Success,
            code: response.status,
            messages: await response.json()
        };
    }
    // Handle exception
    return {
        status: Status.Failure,
        code: response.status,
        reason: response.statusText,
    };
}

/// Run all the tests and return the results
const runAllTests = async () => {
    // The tests to run
    const tests = [
        sendMessageTest,
        fetchMessagesTest
    ];
    let results = [];
    for (const test of tests) {
        results.push({
            test: test.name,
            result: await test(),
        });
    }
    return results;
}

// Run all the tests
runAllTests().then(results =>
    console.log(JSON.stringify(results, null, 2)));