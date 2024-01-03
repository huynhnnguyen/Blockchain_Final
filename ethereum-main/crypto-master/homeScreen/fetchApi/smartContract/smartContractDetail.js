document.addEventListener("DOMContentLoaded", function () {
  const id = localStorage.getItem("SmartContractId");
  const apiUrl = `http://localhost:8099/api/v1/consumer/smart-contract/${id}`;
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
      const sellerName = document.getElementById("sellerName");
      const buyerName = document.getElementById("buyerName");
      const price = document.getElementById("price");
      const quantityEth = document.getElementById("quantityEth");
      const createdDate = document.getElementById("createdDate");

      idInput.textContent = data.id !== null ? data.id : "N/A";

      sellerName.textContent =
        data.sellerName !== null ? data.sellerName : "N/A";

      buyerName.textContent = data.buyerName !== null ? data.buyerName : "N/A";

      price.textContent = data.price !== null ? data.price : "N/A";

      quantityEth.textContent =
        data.quantityEth !== null ? data.quantityEth : "N/A";

      const createdDateValue = data.createdDate
        ? new Date(data.createdDate)
        : null;
      createdDate.textContent = createdDateValue
        ? createdDateValue.toLocaleDateString()
        : "N/A";
    })
    .catch((error) => {
      console.error("Error:", error);
      // Xử lý lỗi khi fetch dữ liệu không thành công
    });
});
