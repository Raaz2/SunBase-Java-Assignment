const baseUrl = "http://localhost:8080/auth";

// Function to handle registration
async function registerAdmin(admin) {
    try {
        const response = await fetch(`${baseUrl}/register`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(admin)
        });

        if (response.ok) {
            const result = await response.json();
            console.log("Admin Registered:", result);
            alert("Registration successful!");
        } else {
            const errorData = await response.json();
            console.error("Error during registration:", errorData);
            alert("Registration failed!");
        }
    } catch (error) {
        console.error("Error during registration:", error);
        alert("An error occurred during registration.");
    }
}

// Function to handle login
async function loginAdmin(admin) {
    try {
        const response = await fetch(`${baseUrl}/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(admin)
        });

        if (response.ok) {
            const token = await response.text();
            console.log("Jwt Token:", token);
            alert("Login successful!");
            localStorage.setItem('jwt', token);
            window.location.href = '../Customers/customers.html';
        } else {
            const errorData = await response.json();
            console.error("Error during login:", errorData);
            alert("Login failed!");
        }
    } catch (error) {
        console.error("Error during login:", error);
        alert("An error occurred during login.");
    }
}

// Function to handle form submissions
document.getElementById('authForm').addEventListener('submit', async function (event) {
    event.preventDefault();

    const loginId = document.getElementById('loginId').value;
    const password = document.getElementById('password').value;
    const action = document.querySelector('input[name="action"]:checked').value;

    const admin = {
        loginId: loginId,
        password: password
    };

    if (action === 'register') {
        await registerAdmin(admin);
    } else if (action === 'login') {
        await loginAdmin(admin);
    }
});