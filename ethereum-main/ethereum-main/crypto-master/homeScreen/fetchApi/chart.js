const token = localStorage.getItem("token");

const filtterTime = document.getElementById("addFilterTime");
const today = new Date();

// Lấy ngày, tháng và năm hiện tại
const year = today.getFullYear();
const month = (today.getMonth() + 1).toString().padStart(2, "0"); // Tháng bắt đầu từ 0
const day = today.getDate().toString().padStart(2, "0");

// Tạo chuỗi định dạng YYYY-MM-DDT00:00:00Z
const currentDate = `${year}-${month}-${day}T00:00:00Z`;
console.log(currentDate);

const fetchData = (fromDate, toDate) => {
  console.log("th", fromDate);
  if (!fromDate && !toDate) return;

  fetch(
    `http://localhost:8099/api/v1/consumer/ethereum-price/chart?fromDate=${fromDate}&toDate=${toDate}`,
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  )
    .then((response) => response.json())
    .then((data) => {
      const times = data.map((entry) => {
        const date = new Date(entry.time);
        return date.toLocaleString(); // Chuyển đổi thành ngày tháng địa phương
      });
      const prices = data.map((entry) => entry.price);
      // Tạo biểu đồ
      const ctx = document.getElementById("myAreaChartNew").getContext("2d");
      const myChart = new Chart(ctx, {
        type: "line",
        data: {
          labels: times,
          datasets: [
            {
              label: "Ethereum Price",
              data: prices,
              borderColor: "blue",
              borderWidth: 1,
              fill: false,
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            x: {
              type: "time",
              time: {
                unit: "minute",
              },
              title: {
                display: true,
                text: "Time",
              },
            },
            y: {
              title: {
                display: false,
                text: "Price",
              },
            },
          },
        },
      });
    })
    .catch((error) => console.error("Error fetching data:", error));
};

fetchData(currentDate, currentDate);

filtterTime.addEventListener("click", () => {
  const fromDate = new Date(
    document.getElementById("fromDate").value
  ).toISOString();
  const toDate = new Date(
    document.getElementById("toDate").value
  ).toISOString();
  fetchData(fromDate, toDate);
});
