document.addEventListener("DOMContentLoaded", function () {
  const id = localStorage.getItem("TransactionId");
  const apiUrl = `http://localhost:8099/api/v1/consumer/ethereum-transactions/${id}`;
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
      const idInput = document.getElementById("getId");
      const codeInput = document.getElementById("getCode");
      const paymentGatewayInput = document.getElementById("paymentGateway");
      const quantityEthInput = document.getElementById("quantityEth");
      const noteInput = document.getElementById("note");
      const priceInput = document.getElementById("price");
      const sellerNameInput = document.getElementById("sellerName");
      const emailInput = document.getElementById("email");
      const dateInput = document.getElementById("date");
      const lastModifiedDateInput = document.getElementById("lastModifiedDate");

      idInput.textContent = data.id !== null ? data.id : "N/A";

      codeInput.textContent = data.code !== null ? data.code : "N/A";

      paymentGatewayInput.textContent =
        data.paymentGateway !== null ? data.paymentGateway : "N/A";

      quantityEthInput.textContent =
        data.quantityEth !== null ? data.quantityEth : "N/A";

      noteInput.textContent = data.note !== null ? data.note : "N/A";

      priceInput.textContent = data.price !== null ? data.price : "N/A";

      sellerNameInput.textContent =
        data.sellerName !== null ? data.sellerName : "N/A";

      emailInput.textContent = data.email !== null ? data.email : "N/A";

      const dateValue = data.date ? new Date(data.date) : null;
      dateInput.textContent = dateValue
        ? dateValue.toLocaleDateString()
        : "N/A";

      const lastModifiedDateValue = data.date ? new Date(data.date) : null;
      lastModifiedDateInput.textContent = lastModifiedDateValue
        ? lastModifiedDateValue.toLocaleDateString()
        : "N/A";

      const buyButton = document.getElementById("buyTranSaction");
      buyButton.addEventListener("click", () => {
        const confirmBuy = confirm("are you sure wnat to buy this ethereum");
        const id = row.id;
        if (confirmBuy) {
          fetch("http://localhost:8099/api/v1/consumer/smart-contract", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },

            body: JSON.stringify({
              ethereumTransactionId: id,
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
                buyButton.appendChild(resultMessage);
              } else {
                // Authentication failed, display an error message
                resultMessage.textContent = "You are not enough eth.";
                resultMessage.classList.add("text-danger");
                buyButton.appendChild(resultMessage);
              }
            })
            .catch((error) => {
              console.log("thang", error);
              resultMessage.textContent = "there some error";
              resultMessage.classList.add("text-danger");

              buyButton.appendChild(resultMessage);
            });
        }
      });
    })
    .catch((error) => {
      console.error("Error:", error);
      // Xử lý lỗi khi fetch dữ liệu không thành công
    });
});
