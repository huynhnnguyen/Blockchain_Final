// JavaScript code here
document.addEventListener("DOMContentLoaded", function () {
  const register = document.getElementById("register-form");
  const errorMessage = document.getElementById("error-message");

  register.addEventListener("submit", function (e) {
    e.preventDefault();

    const activatedKey = localStorage.getItem("activatedKey");
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const phone = document.getElementById("phone").value;
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;

    // Replace with your API endpoint for authentication
    const apiUrl = "http://192.168.1.11:8099/api/v1/consumer/register";

    // Make a POST request to the API
    fetch(apiUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        activatedKey,
        username,
        password,
        phone,
        name,
        email,
      }),
    })
      .then((response) => {
        if (response.ok) {
          // Authentication successful, redirect or perform actions
          window.location.href = "./login.html";
        } else {
          // Authentication failed, display an error message
          errorMessage.textContent = "Invalid credentials. Please try again.";
          alert("Email or password is incorrect");
        }
      })
      .catch((error) => {
        console.error("Error:", error);
        errorMessage.textContent = "An error occurred. Please try again later.";
      });
  });
});
