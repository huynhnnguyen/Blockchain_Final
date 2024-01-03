document.addEventListener("DOMContentLoaded", function () {
  const apiUrl = "http://localhost:8099/api/v1/consumer/ethereum/total";
  const token = localStorage.getItem("token");

  fetch(apiUrl, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then((response) => {
      console.log("thang ne", response);
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
      const ethereumTotalElement = document.getElementById("ethereumTotal");
      const ethereumMiningTotalElement = document.getElementById(
        "ethereumMiningTotal"
      );
      const totalTradingElement = document.getElementById("totalTrading");
      const totalCustomerElement = document.getElementById("totalCustomer");

      ethereumTotalElement.textContent =
        data.totalEthereum !== null ? data.totalEthereum : "N/A";
      ethereumMiningTotalElement.textContent =
        data.totalEthereumMining !== null ? data.totalEthereumMining : "N/A";
      totalTradingElement.textContent =
        data.totalTrading !== null ? data.totalTrading : "N/A";
      totalCustomerElement.textContent =
        data.totalCustomer !== null ? data.totalCustomer : "N/A";
    })
    .catch((error) => {
      console.error("Error:", error);
      // Xử lý lỗi khi fetch dữ liệu không thành công
    });
});
