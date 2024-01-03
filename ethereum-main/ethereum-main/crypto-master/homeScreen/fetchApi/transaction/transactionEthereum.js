// JavaScript code here
document.addEventListener("DOMContentLoaded", function () {
  const createTransaction = document.getElementById("create-transaction");
  const token = localStorage.getItem("token");
  const resultMessage = document.createElement("p");
  resultMessage.classList.add("text-center", "mb-2");

  createTransaction?.addEventListener("submit", function (e) {
    e.preventDefault();

    const price = document.getElementById("create-price").value;
    const quantityEth = document.getElementById("create_quantityEth").value;
    const paymentGateway = document.getElementById(
      "create_paymentGateway"
    ).value;
    const note = document.getElementById("create_note").value;

    // Replace with your API endpoint for authentication
    const apiUrl = "http://localhost:8099/api/v1/consumer/ethereum-transaction";

    // Make a POST request to the API
    fetch(apiUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        price,
        quantityEth,
        paymentGateway,
        note,
      }),
    })
      .then((response) => {
        if (response.status === 401) {
          window.location.href = "../authenticate/login.html";
        }
        if (response.status === 500) {
          alert("you don't have a wallet");
          throw new Error("Authentication failed");
        }
        if (response.ok) {
          // Authentication successful, redirect or perform actions
          resultMessage.textContent = "create transaction successfully!";
          resultMessage.classList.add("text-success");
          createTransaction.appendChild(resultMessage);
        } else {
          // Authentication failed, display an error message
          resultMessage.textContent = "You are not enough eth.";
          resultMessage.classList.add("text-danger");
          createTransaction.appendChild(resultMessage);
        }
      })
      .catch((error) => {
        console.log("thang", error);
        resultMessage.textContent = "there some error";
        resultMessage.classList.add("text-danger");

        createTransaction.appendChild(resultMessage);
      });
  });
});

//get my list transaction
const token = localStorage.getItem("token");

const headers = new Headers();
headers.append("Authorization", `Bearer ${token}`);

const requestOptions = {
  method: "GET",
  headers: headers,
  redirect: "follow",
};

fetch(
  "http://localhost:8099/api/v1/consumer/ethereum-transactions",
  requestOptions
)
  .then((response) => response.json())
  .then((data) => {
    const tableBody = document.getElementById("tableBodyHistoryTransaction");

    data.forEach((row) => {
      const tr = document.createElement("tr");

      const id = document.createElement("td");
      id.textContent = row.id;
      tr.appendChild(id);

      const paymentGateway = document.createElement("td");
      paymentGateway.textContent = row.paymentGateway;
      tr.appendChild(paymentGateway);

      const quantityEth = document.createElement("td");
      quantityEth.textContent = row.quantityEth;
      tr.appendChild(quantityEth);

      const price = document.createElement("td");
      price.textContent = row.price;
      tr.appendChild(price);

      const sellerName = document.createElement("td");
      sellerName.textContent = row.sellerName;
      tr.appendChild(sellerName);

      const date = document.createElement("td");
      date.textContent = new Date(row.date).toLocaleString();
      tr.appendChild(date);

      const action = document.createElement("td");

      // Tạo nút viewDetail
      const getDetail = document.createElement("button");
      getDetail.classList.add("btn", "btn-primary");
      getDetail.textContent = "Detail";
      getDetail.addEventListener("click", () => {
        // Lấy ID của giao dịch từ dữ liệu hàng (row)
        const id = row.id;

        localStorage.setItem("TransactionId", id);

        // Chuyển hướng đến trang chi tiết với ID tương ứng
        window.location.href = `../homeScreen/GetDetailTransaction.html`;
      });

      // cách nhau bởi dấu |
      action.appendChild(getDetail);
      const separator = document.createElement("span");
      separator.textContent = " | ";
      action.appendChild(separator);

      // Tạo nút Delete
      const deleteButton = document.createElement("button");
      deleteButton.classList.add("btn", "btn-danger");
      deleteButton.textContent = "Delete";
      deleteButton.addEventListener("click", () => {
        const confirmDelete = confirm(
          "Are you sure you want to delete this item?"
        );
        if (confirmDelete) {
          const idToDelete = row.id; // Lấy ID của giao dịch từ dữ liệu hàng (row)

          // Gọi API để xóa giao dịch với ID tương ứng
          fetch(
            `http://localhost:8099/api/v1/consumer/ethereum-transactions/${idToDelete}`,
            {
              method: "DELETE",
              headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
              },
            }
          )
            .then((response) => {
              if (response.ok) {
                // Xóa thành công - có thể thực hiện các hành động cần thiết tại đây
                console.log(
                  `Transaction with ID ${idToDelete} deleted successfully.`
                );
                // Có thể làm mới hoặc cập nhật lại giao diện người dùng sau khi xóa
              } else {
                // Xóa không thành công
                console.error(
                  `Failed to delete transaction with ID ${idToDelete}.`
                );
                // Có thể hiển thị thông báo lỗi cho người dùng
              }
            })
            .catch((error) => {
              console.error("Error deleting transaction:", error);
            });
        }
      });
      action.appendChild(deleteButton);

      tr.appendChild(action);

      tableBody?.appendChild(tr);
    });
  })
  .catch((error) => {
    console.error("Error fetching data:", error);
  });

//get list transaction all

fetch(
  "http://localhost:8099/api/v1/consumer/ethereum-transaction/all",
  requestOptions
)
  .then((response) => response.json())
  .then((data) => {
    const tableBody = document.getElementById("tableBodyAllHistoryTransaction");

    data.forEach((row) => {
      const tr = document.createElement("tr");

      const id = document.createElement("td");
      id.textContent = row.id;
      tr.appendChild(id);

      const paymentGateway = document.createElement("td");
      paymentGateway.textContent = row.paymentGateway;
      tr.appendChild(paymentGateway);

      const quantityEth = document.createElement("td");
      quantityEth.textContent = row.quantityEth;
      tr.appendChild(quantityEth);

      const price = document.createElement("td");
      price.textContent = row.price;
      tr.appendChild(price);

      const sellerName = document.createElement("td");
      sellerName.textContent = row.sellerName;
      tr.appendChild(sellerName);

      const date = document.createElement("td");
      date.textContent = new Date(row.date).toLocaleString();
      tr.appendChild(date);

      const action = document.createElement("td");

      // Tạo nút viewDetail
      const getDetail = document.createElement("button");
      getDetail.classList.add("btn", "btn-primary");
      getDetail.textContent = "Detail";
      getDetail.addEventListener("click", () => {
        // Lấy ID của giao dịch từ dữ liệu hàng (row)
        const id = row.id;

        localStorage.setItem("TransactionId", id);

        // Chuyển hướng đến trang chi tiết với ID tương ứng
        window.location.href = `../homeScreen/GetDetailTransaction.html`;
      });

      // cách nhau bởi dấu |
      action.appendChild(getDetail);
      const separator = document.createElement("span");
      separator.textContent = " | ";
      action.appendChild(separator);

      // Tạo nút Delete
      const buyButton = document.createElement("button");
      buyButton.classList.add("btn", "btn-success");
      buyButton.textContent = "Buy";
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
              if (response.status === 500) {
                alert("you don't have a wallet");
                throw new Error("Authentication failed");
              }
              if (response.status === 400) {
                window.alert("Not enough money");
                // Hoặc có thể sử dụng các thư viện hoặc phương thức khác để hiển thị thông báo đẹp hơn, ví dụ: Swal, Bootstrap Alert...
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
      action.appendChild(buyButton);

      tr.appendChild(action);

      tableBody?.appendChild(tr);
    });
  })
  .catch((error) => {
    console.error("Error fetching data:", error);
  });
