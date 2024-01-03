//get list smart contract

const token = localStorage.getItem("token");

const headers = new Headers();
headers.append("Authorization", `Bearer ${token}`);

const requestOptions = {
  method: "GET",
  headers: headers,
  redirect: "follow",
};

fetch("http://localhost:8099/api/v1/consumer/smart-contract", requestOptions)
  .then((response) => response.json())
  .then((data) => {
    const tableBody = document.getElementById("listSmartContract");

    data.forEach((row) => {
      const tr = document.createElement("tr");

      const id = document.createElement("td");
      id.textContent = row.id;
      tr.appendChild(id);

      const sellerName = document.createElement("td");
      sellerName.textContent = row.sellerName;
      tr.appendChild(sellerName);

      const createdDate = document.createElement("td");
      createdDate.textContent = new Date(row.createdDate).toLocaleString();
      tr.appendChild(createdDate);

      const buyerName = document.createElement("td");
      buyerName.textContent = row.buyerName;
      tr.appendChild(buyerName);

      const price = document.createElement("td");
      price.textContent = row.price;
      tr.appendChild(price);

      const quantityEth = document.createElement("td");
      quantityEth.textContent = row.quantityEth;
      tr.appendChild(quantityEth);

      const action = document.createElement("td");

      // Tạo nút viewDetail
      const getDetail = document.createElement("button");
      getDetail.classList.add("btn", "btn-primary");
      getDetail.textContent = "Detail";
      getDetail.addEventListener("click", () => {
        // Lấy ID của giao dịch từ dữ liệu hàng (row)
        const id = row.id;

        localStorage.setItem("SmartContractId", id);

        // Chuyển hướng đến trang chi tiết với ID tương ứng
        window.location.href = `../homeScreen/smartContractDetail.html`;
      });

      action.appendChild(getDetail);

      tr.appendChild(action);

      tableBody.appendChild(tr);
    });
  })
  .catch((error) => {
    console.error("Error fetching data:", error);
  });
