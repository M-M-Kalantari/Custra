package custra.server.spring.core.SupportRequest;

import java.time.LocalDateTime;
import java.util.List;

public class SampleRequests {
    public static List<SupportRequest> getAll() {
        return List.of(
                new SupportRequest(null, 1L, 11L, 1,
                        "مشکل ورود به سیستم", "نمی‌توانم وارد حساب کاربری شوم",
                        RequestStatus.SENT, LocalDateTime.now().minusDays(2),
                        LocalDateTime.now().minusDays(2), null,
                        null, null),

                new SupportRequest(null, 1L, 11L, 2,
                        "قطع شدن سرویس", "اینترنت من از دیروز قطع شده است",
                        RequestStatus.IN_REVIEW, LocalDateTime.now().minusDays(1),
                        LocalDateTime.now(), null,
                        null, null),

                new SupportRequest(null, 2L, 12L, 1,
                        "مشکل پرداخت", "پرداخت من در سایت ناموفق بود",
                        RequestStatus.DONE, LocalDateTime.now().minusDays(5),
                        LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(3),
                        "مشکل از بانک بود و حل شد", null),

                new SupportRequest(null, 3L, 13L, 1,
                        "پیشنهاد ویژگی جدید", "امکان ارسال فایل در سیستم اضافه شود",
                        RequestStatus.RETURNED, LocalDateTime.now().minusDays(4),
                        LocalDateTime.now().minusDays(2), null,
                        null, "این موضوع در محدوده پشتیبانی نیست"),

                new SupportRequest(null, 5L, 14L, 1,
                        "کندی سیستم", "سرعت سایت خیلی پایین است",
                        RequestStatus.IN_REVIEW, LocalDateTime.now().minusHours(10),
                        LocalDateTime.now().minusHours(5), null,
                        null, null),

                new SupportRequest(null, 6L, 11L, 1,
                        "خطا در اپلیکیشن", "اپلیکیشن روی گوشی باز نمی‌شود",
                        RequestStatus.DONE, LocalDateTime.now().minusDays(7),
                        LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1),
                        "بروزرسانی اپلیکیشن انجام شد", null),

                new SupportRequest(null, 6L, 11L, 2,
                        "درخواست پشتیبانی فوری", "نیاز به بررسی فوری مشکل دارم",
                        RequestStatus.SENT, LocalDateTime.now().minusHours(6),
                        LocalDateTime.now().minusHours(6), null,
                        null, null)
        );
    }
}
