// JavaScript code here
document.addEventListener("DOMContentLoaded", function () {
  const createWallet = document.getElementById("update-wallet");
  const token = localStorage.getItem("token");
  const resultMessage = document.createElement("p");
  resultMessage.classList.add("text-center", "mb-2");

  createWallet?.addEventListener("submit", function (e) {
    e.preventDefault();

    const idCard = document.getElementById("identifyCard").value;
    const ownerName = document.getElementById("ownerNames").value;
    const bankType = document.getElementById("create_paymentGateway").value;
    const bankConnect = document.getElementById("bankBrand").value;
    const bankNumber = document.getElementById("bankNumber").value;
    const totalAdd = document.getElementById("totalAdd").value;
    // Replace with your API endpoint for authentication
    const apiUrl = "http://localhost:8099/api/v1/consumer/wallet/update";

    // Make a POST request to the API
    fetch(apiUrl, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        idCard,
        ownerName,
        bankType,
        bankConnect,
        bankNumber,
        totalAdd,
      }),
    })
      .then((response) => {
        if (response.status === 401) {
          window.location.href = "../authenticate/login.html";
        }
        if (response.ok) {
          // Authentication successful, redirect or perform actions
          resultMessage.textContent = "create transaction successfully!";
          resultMessage.classList.add("text-success");
          createWallet.appendChild(resultMessage);
        } else {
          // Authentication failed, display an error message
          resultMessage.textContent = "You have already wallet.";
          resultMessage.classList.add("text-danger");
          createWallet.appendChild(resultMessage);
        }
      })
      .catch((error) => {
        console.log("thang", error);
        resultMessage.textContent = "there some error";
        resultMessage.classList.add("text-danger");

        createWallet.appendChild(resultMessage);
      });
  });
});
