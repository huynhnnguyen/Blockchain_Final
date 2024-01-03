document.addEventListener('DOMContentLoaded', function () {
  const checkOtpForm = document.getElementById('check_otp');
  const resultMessage = document.getElementById('resultMessage');

  checkOtpForm.addEventListener('submit', function (e) {
    e.preventDefault();

    const otp = document.getElementById('otpInput').value;
    const email = localStorage.getItem('userEmail');

    const apiUrlValidate = `http://192.168.1.11:8099/api/v1/consumer/otp/validate/${email}`;

    fetch(apiUrlValidate, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ otp }),
    })
    .then((response) => {
      if (response.ok) {
        return response.json(); // Chuyển đổi phản hồi sang JSON
      } else {
        throw new Error('Network response was not ok.');
      }
    })
    .then((data) => {
      const activatedKey = data.activatedKey; // Lấy giá trị activatedKey từ phản hồi
      // Sử dụng giá trị activatedKey ở đây để thực hiện các hành động tiếp theo

      // Ví dụ: Lưu activatedKey vào localStorage
      localStorage.setItem('activatedKey', activatedKey);

      // Sau khi lấy được activatedKey, có thể thực hiện chuyển hướng hoặc các hành động khác ở đây
      window.location.href = './register.html';
    })
    .catch((error) => {
      console.error('Error:', error);
      resultMessage.textContent = 'An error occurred. Please try again later.';
      // Xử lý khi có lỗi kết nối hoặc yêu cầu
    });
  });
});