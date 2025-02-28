(function() {
    // Notify localhost when on MonkeyType
    fetch("http://localhost:9974", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ onsite: true })
    }).catch(err => console.error("Error sending onsite status:", err));

    console.log("MonkeyType extension loaded!");

    function sendMode(mode) {
        console.log(`Active mode: ${mode}`);
        fetch("http://localhost:9974", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ mode: mode })
        }).catch(err => console.error("Error sending mode:", err));
    }

    function checkActiveMode() {
        const modes = ["time", "words", "quote", "zen", "custom"];
        for (let mode of modes) {
            let button = document.querySelector(`button.textButton[mode="${mode}"]`);
            if (button && button.classList.contains("active")) {
                sendMode(mode);
                return;
            }
        }
    }

    // Run on initial load
    checkActiveMode();

    // Set up an observer to track changes in button attributes
    const observer = new MutationObserver(mutations => {
        mutations.forEach(mutation => {
            if (mutation.type === "attributes" && mutation.attributeName === "class") {
                checkActiveMode();
            }
        });
    });

    // Attach observer to mode buttons
    const modes = ["time", "words", "quote", "zen", "custom"];
    modes.forEach(mode => {
        let button = document.querySelector(`button.textButton[mode="${mode}"]`);
        if (button) {
            observer.observe(button, { attributes: true });
        }
    });

})();