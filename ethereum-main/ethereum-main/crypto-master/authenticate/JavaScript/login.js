document.addEventListener("DOMContentLoaded", function () {
  const loginForm = document.getElementById("login-form");
  const errorMessage = document.getElementById("error-message");

  loginForm.addEventListener("submit", function (e) {
    e.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    // Replace with your API endpoint for authentication
    const apiUrl = "http://192.168.1.11:8099/api/v1/consumer/authenticate";

    // Make a POST request to the API
    fetch(apiUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, password }),
    })
      .then((response) => {
        if (response.ok) {
          // Authentication successful, get the token from response
          return response.json(); // Parse response body as JSON
        } else {
          // Authentication failed, display an error message
          errorMessage.textContent = "Invalid credentials. Please try again.";
          alert("Email or password is incorrect");
          throw new Error("Authentication failed");
        }
      })
      .then((data) => {
        // Save token to Local Storage or Session Storage
        const token = data.token; // Replace 'token' with the actual property name in your response
        localStorage.setItem("token", token); // Save token to Local Storage
        window.location.href = "../homeScreen/index.html"; // Redirect after successful authentication
      })
      .catch((error) => {
        console.error("Error:", error);
        errorMessage.textContent = "An error occurred. Please try again later.";
      });
  });
});
