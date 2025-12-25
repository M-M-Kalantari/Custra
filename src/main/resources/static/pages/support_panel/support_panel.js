$(document).ready(function () {

    // Tabs
    $('.tab-btn').click(function () {
        const tabId = $(this).data('tab');
        $('.tab-btn').removeClass('active');
        $(this).addClass('active');
        $('.tab-content').removeClass('active');
        $('#' + tabId).addClass('active');
    });

    // Get Data from Server
    function loadRequests() {
        $.getJSON(`/api/custra/v1/requests/support`, function (data) {
            $('#submitted-requests').empty();
            $('#reviewing-requests').empty();

            let req_status_en = "";
            let req_status_fa = "";

            data.forEach(req => {
                req_status_en = req.status;
                switch (req_status_en) {
                    case "SENT":
                        req_status_fa = "ارسال شد";
                        break;
                    case "IN_REVIEW":
                        req_status_fa = "در حال بررسی ⏳";
                        break;
                    case "DONE":
                        req_status_fa = "انجام شد ✅";
                        break;
                    case "RETURNED":
                        req_status_fa = "برگشت شد ⚠️";
                        break;
                }
                const row = `<tr>
                    <td>${req.requestTime}</td>
                    <td>${req.supportName || "-"}</td>
                    <td>${req.customerName || "-"}</td>
                    <td>${req.id}</td>
                    <td>${req.subject}</td>
                    <td>${req_status_fa}</td>
                    <td>${req.completionTime}</td>
                    <td>${req.returnReason}</td>
                    <td>${req.supportNote}</td>
                    <td><i class="ph ph-file-text review-icon"></i></td>
                </tr>`;

                if(req_status_en === "SENT" || req_status_en === "DONE") {
                    $('#submitted-requests').append(row);
                } else {
                    $('#reviewing-requests').append(row);
                }
            });

            $('table tbody tr').each(function () {
                const statusCell = $(this).find('td:nth-child(6)');
                const statusText = statusCell.text().trim();
                let statusValue = "";
                switch (statusText) {
                    case "در حال بررسی":
                    case "در حال بررسی ⏳":
                        statusValue = "PENDING";
                        break;
                    case "انجام شد":
                    case "انجام شد ✅":
                        statusValue = "DONE";
                        break;
                    case "برگشت شد":
                    case "برگشت شد ⚠️":
                        statusValue = "RETURNED";
                        break;
                    // default:
                    //     statusValue = "PENDING";
                }

                if (statusValue === 'DONE')
                    statusCell.html('<span class="status-text-done">انجام شد ✅</span>');
                else if (statusValue === 'RETURNED')
                    statusCell.html('<span class="status-text-returned">برگشت شد ⚠️</span>');
                else if (statusValue === 'PENDING')
                    statusCell.html('<span class="status-text-pending">در حال بررسی ⏳</span>');
                // else
                //     statusCell.html('<span class="status-text-pending">در حال بررسی ⏳</span>');

                refreshCSS();
            });


            // Modal Open
            $('.review-icon').click(function () {
                $('#review-modal').css('display', 'flex').hide().fadeIn();

                let row = this.closest("tr");
                $('#reqNumber').val(row.cells[3].innerText);
                $('#reqTime').val(row.cells[0].innerText);
                $('#reqSubject').val(row.cells[4].innerText);

                const statusText = row.cells[5].innerText.trim();
                let statusValue = "";
                switch (statusText) {
                    case "در حال بررسی":
                    case "در حال بررسی ⏳":
                        statusValue = "PENDING";
                        break;
                    case "انجام شد":
                    case "انجام شد ✅":
                        statusValue = "DONE";
                        break;
                    case "برگشت شد":
                    case "برگشت شد ⚠️":
                        statusValue = "RETURNED";
                        break;
                    default:
                        statusValue = "PENDING";
                }
                $(`input[name="reqStatus"][value="${statusValue}"]`).prop('checked', true);

                $('#reqReview').val(row.cells[8].innerText === "-" ? "" : row.cells[8].innerText);
            });
        });
    }

    loadRequests();

    // Modal Close
    $('#btnCancelRequest').click(function () {
        $('#review-modal').fadeOut();
    });
    $('#review-modal').click(function (e) {
        if(e.target === this) $(this).fadeOut();
    });

    // Send request to server
    $('#btnSubmitRequest').click(function () {
        const reqId = $('#reqNumber').val();
        const status = $('input[name="reqStatus"]:checked').val();
        const review = $('#reqReview').val().trim();

        if(review === "") {
            $('#errorMsg').fadeIn();
            $('#reqReview').trigger('focus');
            return;
        }

        $.ajax({
            url: `/api/custra/v1/requests/update`,
            type: "POST",
            data: {
                id: reqId,
                status: status,
                review: review
            },
            success: function () {
                alert("تغییرات ثبت شد!");
                $('#review-modal').fadeOut();
                loadRequests();
            },
            error: function (xhr) {
                if(xhr.status === 403) alert("فقط کارشناسان می‌توانند تغییر دهند.");
                else if(xhr.status === 404) alert("درخواست یافت نشد.");
                else alert("خطا در ثبت تغییرات!");
            }
        });
    });
});

function refreshCSS() {
    const link = document.getElementById("theme-css");
    const href = link.getAttribute("href").split("?")[0];
    link.setAttribute("href", href + "?v=" + new Date().getTime());
}