// JavaScript code here
document.addEventListener("DOMContentLoaded", function () {
  const createWallet = document.getElementById("create-wallet");
  const token = localStorage.getItem("token");
  const resultMessage = document.createElement("p");
  resultMessage.classList.add("text-center", "mb-2");

  createWallet?.addEventListener("submit", function (e) {
    e.preventDefault();

    const idCard = document.getElementById("idCard").value;
    const ownerName = document.getElementById("ownerName").value;
    const bankType = document.getElementById("create_paymentGateway").value;
    const bankConnect = document.getElementById("bankBrand").value;
    const bankNumber = document.getElementById("bankNumber").value;
    // Replace with your API endpoint for authentication
    const apiUrl = "http://localhost:8099/api/v1/consumer/wallet/create";

    // Make a POST request to the API
    fetch(apiUrl, {
      method: "POST",
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
      }),
    })
      .then((response) => {
        if (response.status === 401) {
          window.location.href = "../authenticate/login.html";
        }
        if (response.ok) {
          // Authentication successful, redirect or perform actions
          resultMessage.textContent = "update successfully!";
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

document.addEventListener("DOMContentLoaded", function () {
  const id = localStorage.getItem("TransactionId");
  const apiUrl = "http://localhost:8099/api/v1/consumer/wallet/my-money";
  const token = localStorage.getItem("token");

  fetch(apiUrl, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then((response) => {
      console.log(response);
      if (response.status === 401) {
        window.location.href = "../authenticate/login.html";
      }
      if (response.ok) {
        return response.json();
      } else {
        throw new Error("Failed to fetch data");
      }
    })

    .then((data) => {
      const totalMoney = document.getElementById("totalMoney");

      totalMoney.textContent =
        data.totalMoney !== null ? data.totalMoney : "N/A";
    })
    .catch((error) => {
      console.error("Error:", error);
      // Xử lý lỗi khi fetch dữ liệu không thành công
    });
});
