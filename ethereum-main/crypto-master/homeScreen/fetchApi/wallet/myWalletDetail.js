document.addEventListener("DOMContentLoaded", function () {
  const id = localStorage.getItem("TransactionId");
  const apiUrl = "http://localhost:8099/api/v1/consumer/wallet/my-wallet";
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
      const ownerName = document.getElementById("ownerName");
      const totalMoney = document.getElementById("totalMoney");
      const createdDate = document.getElementById("createdDate");
      const code = document.getElementById("code");
      const bankConnect = document.getElementById("bankConnect");
      const bankNumber = document.getElementById("bankNumber");
      const bankType = document.getElementById("bankType");
      const codeWallet = document.getElementById("codeWallet");
      const idCard = document.getElementById("idCard");

      idInput.textContent = data.id !== null ? data.id : "N/A";
      localStorage.setItem("walletId", idInput);

      ownerName.textContent = data.ownerName !== null ? data.ownerName : "N/A";

      totalMoney.textContent =
        data.totalMoney !== null ? data.totalMoney : "N/A";
      const dateValue = data.createdDate ? new Date(data.createdDate) : null;
      createdDate.textContent = dateValue
        ? dateValue.toLocaleDateString()
        : "N/A";

      code.textContent = data.code !== null ? data.code : "N/A";

      bankConnect.textContent =
        data.bankConnect !== null ? data.bankConnect : "N/A";

      bankNumber.textContent =
        data.bankNumber !== null ? data.bankNumber : "N/A";

      bankType.textContent = data.bankType !== null ? data.bankType : "N/A";
      idCard.textContent = data.idCard !== null ? data.idCard : "N/A";
      codeWallet.textContent =
        data.codeWallet !== null ? data.codeWallet : "N/A";
    })
    .catch((error) => {
      console.error("Error:", error);
      // Xử lý lỗi khi fetch dữ liệu không thành công
    });
});
