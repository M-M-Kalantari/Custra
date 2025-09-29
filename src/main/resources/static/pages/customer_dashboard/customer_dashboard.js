$(document).ready(function () {

  const formatter = new Intl.DateTimeFormat('fa-IR', {
    year: 'numeric', month: '2-digit', day: '2-digit',
    hour: '2-digit', minute: '2-digit'
  });

  // Get Data from Server
  function loadUserRequests() {
    $.ajax({
      url: '/api/custra/v1/requests',
      method: 'GET',
      dataType: 'json',
      success: function (requests) {
        const tbody = $('#requestTableBody');
        tbody.empty();
        if (requests.length === 0) {
          tbody.append('<tr><td colspan="7" style="text-align:center;">درخواستی وجود ندارد</td></tr>');
          return;
        }
        requests.forEach(req => {
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
          const row = `
                        <tr>
                            <td>${req.id}</td>
                            <td>${req.requestTime}</td>
                            <td>${req_status}</td>
                            <td>${req.subject}</td>
                            <td>${req.completionTime}</td>
                            <td>${req.supportNote}</td>
                            <td>${req.returnReason}</td>
                        </tr>
                    `;
          tbody.append(row);
        });
      },
      error: function () {
        alert('خطا در بارگذاری درخواست‌ها.');
      }
    });
  }

  loadUserRequests();

  // Modal Open
  $('#btnNewRequest').click(function () {
    const now = new Date();
    const faDate = now.toLocaleDateString('fa-IR') + ' ' +
        now.toLocaleTimeString('fa-IR', { hour: '2-digit', minute: '2-digit' });

    $('#reqTime').val(faDate);
    $('#reqSubject').val('');
    $('#errorMsg').hide();

    $('#newRequestModal').css('display', 'flex');
    setTimeout(() => $('#reqSubject').trigger('focus'), 0);
  });

  // Modal Close
  $('#btnCancelRequest').click(function () {
    $('#newRequestModal').fadeOut();
  });

  $('#newRequestModal').click(function (e) {
    if (e.target === this) $(this).fadeOut();
  });

  // Hide Error on Input
  $('#reqSubject').on('input', function () {
    if ($(this).val().trim() !== '') {
      $('#errorMsg').hide();
    }
  });

  // Submit New Request
  $('#btnSubmitRequest').click(function () {
    const subject = $('#reqSubject').val().trim();
    const description = $('#reqDescription').val() || '';

    if (subject === '') {
      $('#errorMsg').fadeIn();
      $('#reqSubject').trigger('focus');
      return;
    }

    // Send request to server
    $.post('/api/custra/v1/requests/new-request', { subject: subject, description: description })
        .done(function (data) {
          $('#newRequestModal').fadeOut();
          $('#reqSubject').val('');
          $('#reqDescription').val('');
          loadUserRequests(); // جدول آپدیت می‌شود
        })
        .fail(function () {
          alert('خطا در ثبت درخواست.');
        });
  });

});