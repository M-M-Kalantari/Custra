// Submit Button
document.getElementById('registerForm').addEventListener('submit', function (e) {
    e.preventDefault();

    // Check Fields
    const fullname = document.getElementById('fullname').value.trim();
    const email = document.getElementById('email').value.trim();
    const phone = document.getElementById('phone').value.trim();
    const password = document.getElementById('password').value.trim();

    const errorDiv = document.getElementById('errorMsg');
    errorDiv.textContent = "";

    if (fullname === "") {
        errorDiv.textContent = "لطفاً نام کامل خود را وارد کنید.";
        return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        errorDiv.textContent = "لطفاً ایمیل معتبر وارد کنید.";
        return;
    }

    const phoneRegex = /^09\d{9}$/;
    if (!phoneRegex.test(phone)) {
        errorDiv.textContent = "لطفاً شماره تلفن معتبر وارد کنید (مثل 09123456789).";
        return;
    }

    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
    if (!passwordRegex.test(password)) {
        errorDiv.textContent = "رمز عبور باید حداقل 8 کاراکتر و شامل حرف بزرگ، کوچک و عدد باشد.";
        return;
    }

    // Send Request to Server
    fetch("http://localhost:8080/api/custra/v1/register", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: new URLSearchParams({
            fullName: fullname,
            email: email,
            phone: phone,
            password: password
        })
    })
        .then(response => {
            if (response.ok) return response.text();
            else throw response;
        })
        .then(msg => {
            console.log("Register success:", msg);
            window.location.href = "http://localhost:8080/pages/customer_dashboard/customer_dashboard.html";
        })
        .catch(async err => {
            let msg = "خطا در ثبت‌نام.";
            if (err.text) msg = await err.text();
            console.error("Register error:", msg);
            errorDiv.textContent = msg;
        });
});

// Password Show or Hide
document.getElementById('togglePassword').addEventListener('click', function () {
    const passwordInput = document.getElementById('password');
    const icon = this.querySelector("i");

    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        icon.classList.remove("ph-eye");
        icon.classList.add("ph-eye-closed");
    } else {
        passwordInput.type = "password";
        icon.classList.remove("ph-eye-closed");
        icon.classList.add("ph-eye");
    }
});