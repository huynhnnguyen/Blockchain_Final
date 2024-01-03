document.addEventListener("DOMContentLoaded", function () {
  const emailInput = document.getElementById("emailInput");
  const checkForm = document.getElementById("check_email");
  const resultMessage = document.getElementById("resultMessage");

  checkForm.addEventListener("submit", function (e) {
    e.preventDefault(); // Ngăn form submit mặc định

    const email = emailInput.value; // Lấy giá trị email khi người dùng nhập vào

    // Lưu email vào localStorage
    localStorage.setItem("userEmail", email);

    const apiUrlInit = `http://192.168.1.11:8099/api/v1/consumer/otp/init/${email}`;

    fetch(apiUrlInit, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => {
        if (response.ok) {
          window.location.href = "./checkOtp.html"; // Email tồn tại trong cơ sở dữ liệu
        } else {
          resultMessage.classList.add("alert", "alert-danger");
          resultMessage.textContent =
            "Email does not exist in the database or an error occurred";
          setTimeout(() => {
            resultMessage.classList.remove("alert", "alert-danger");
            resultMessage.textContent = "";
          }, 3000);
          // Hiển thị thông báo bằng Bootstrap hoặc thông báo alert tùy ý
        }
      })
      .catch((error) => {
        console.error("Error:", error);
        resultMessage.classList.add("alert", "alert-danger");
        resultMessage.textContent =
          "An error occurred. Please try again later.";
        // Xử lý khi có lỗi kết nối hoặc yêu cầu
      });
  });
});
