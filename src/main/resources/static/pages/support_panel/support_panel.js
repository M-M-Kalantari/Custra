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

            data.forEach(req => {
                let req_status = "";
                switch (req.status) {
                    case "SENT":
                        req_status = "ارسال شد";
                        break;
                    case "IN_REVIEW":
                        req_status = "در حال بررسی ⏳";
                        break;
                    case "DONE":
                        req_status = "انجام شد ✅";
                        break;
                    case "RETURNED":
                        req_status = "برگشت شد ⚠️";
                        break;
                }
                const row = `<tr>
                    <td>${req.requestTime}</td>
                    <td>${req.supportName || "-"}</td>
                    <td>${req.customerName || "-"}</td>
                    <td>${req.id}</td>
                    <td>${req.subject}</td>
                    <td>${req_status}</td>
                    <td>${req.completionTime}</td>
                    <td>${req.returnReason}</td>
                    <td>${req.supportNote}</td>
                    <td><i class="ph ph-file-text review-icon"></i></td>
                </tr>`;

                if(req.status === "SENT" || req.status === "DONE") {
                    $('#submitted-requests').append(row);
                } else {
                    $('#reviewing-requests').append(row);
                }
            });

            $('table tbody tr').each(function () {
                const statusCell = $(this).find('td:nth-child(6)');
                const status = statusCell.text().trim();
                if (status === 'DONE') statusCell.html('<span class="status-text-done">انجام شد ✅</span>');
                else if (status === 'RETURNED') statusCell.html('<span class="status-text-returned">برگشت شد ⚠️</span>');
                else if (status === 'PENDING') statusCell.html('<span class="status-text-pending">در حال بررسی ⏳</span>');
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

    // Update Date from Server
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