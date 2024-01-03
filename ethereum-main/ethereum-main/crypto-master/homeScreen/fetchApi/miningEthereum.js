document.addEventListener("DOMContentLoaded", function () {
  const startMiningBtn = document.getElementById("startMiningBtn");
  const resultMessage = document.createElement("p");
  resultMessage.classList.add("text-center", "mb-2");
  const token = localStorage.getItem("token");
  startMiningBtn.addEventListener("click", function () {
    const apiUrlInit = "http://localhost:8099/api/v1/consumer/mining-ethereum";

    fetch(apiUrlInit, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((response) => {
        if (response.status === 401) {
          window.location.href = "../authenticate/login.html";
        }
        if (response.ok) {
          resultMessage.textContent = "Mining started successfully!";
          resultMessage.classList.add("text-success");
        } else {
          resultMessage.textContent = "You can't mine after 1 hour.";
          resultMessage.classList.add("text-danger");
        }
        // Hiển thị thông báo kết quả
        document.querySelector(".sidebar-card").appendChild(resultMessage);
        setTimeout(() => {
          resultMessage.remove();
        }, 3000);
      })
      .catch((error) => {
        console.error("Error:", error);
        resultMessage.textContent =
          "An error occurred. Please try again later.";
        resultMessage.classList.add("text-danger");
        // Hiển thị thông báo lỗi
        document.querySelector(".sidebar-card").appendChild(resultMessage);
        setTimeout(() => {
          resultMessage.remove();
        }, 3000);
      });
  });
});

//get list mining history

const token = localStorage.getItem("token");

const headers = new Headers();
headers.append("Authorization", `Bearer ${token}`);

const requestOptions = {
  method: "GET",
  headers: headers,
  redirect: "follow",
};

fetch("http://localhost:8099/api/v1/consumer/mining-ethereum", requestOptions)
  .then((response) => response.json())
  .then((data) => {
    const tableBody = document.getElementById("tableBodyHistoryEthreum");

    data.forEach((row) => {
      const tr = document.createElement("tr");

      const id = document.createElement("td");
      id.textContent = row.id;
      tr.appendChild(id);

      const userId = document.createElement("td");
      userId.textContent = row.userId;
      tr.appendChild(userId);

      const timeMining = document.createElement("td");
      timeMining.textContent = new Date(row.timeMining).toLocaleString();
      tr.appendChild(timeMining);

      const totalMining = document.createElement("td");
      totalMining.textContent = row.totalMining;
      tr.appendChild(totalMining);

      const totalEthereumNow = document.createElement("td");
      totalEthereumNow.textContent = row.totalEthereumNow;
      tr.appendChild(totalEthereumNow);

      const userName = document.createElement("td");
      userName.textContent = row.userName;
      tr.appendChild(userName);

      tableBody.appendChild(tr);
    });
  })
  .catch((error) => {
    console.error("Error fetching data:", error);
  });
