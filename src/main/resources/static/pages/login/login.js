// Error message container
const errorMsg = document.getElementById("errorMsg");

// Captcha Loading
function loadCaptcha() {
    document.getElementById('captchaImage').src = "http://localhost:8080/Custra/captcha/image?time=" + new Date().getTime();
}

document.getElementById('refreshCaptcha').addEventListener('click', loadCaptcha);

window.onload = loadCaptcha;

// Submit Button
document.getElementById('loginForm').addEventListener('submit', function (e) {
    e.preventDefault();
    errorMsg.innerText = "";

    const phone = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();
    const captchaValue = document.getElementById('captchaInput').value.trim();

    if (!phone) {
        errorMsg.innerText = "لطفاً نام کاربری را وارد کنید.";
        return;
    }

    if (!password) {
        errorMsg.innerText = "لطفاً رمز عبور را وارد کنید.";
        return;
    }

    if (!captchaValue) {
        errorMsg.innerText = "لطفاً کد کپچا را وارد کنید.";
        return;
    }

    fetch("http://localhost:8080/Custra/captcha/verify?input=" + captchaValue, { method: "POST" })
        .then(res => {
            if (res.ok) return res.text();
            else throw res;
        })
        .then(msg => {
            console.log("Captcha valid:", msg);

            return fetch("http://localhost:8080/api/custra/v1/login", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: new URLSearchParams({ phone, password })
            });
        })
        .then(response => {
            if (response.ok) return response.text();
            else throw response;
        })
        .then(text => {
            console.log("Login success:", text);
            window.location.href = "http://localhost:8080/Custra/dashboard";
        })
        .catch(async err => {
            let msg = "خطا در ورود.";
            if (err.text) msg = await err.text();
            console.error("Error:", msg);
            errorMsg.innerText = msg;
            loadCaptcha();
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