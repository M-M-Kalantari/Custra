package custra.server.spring.core.Users;

import java.util.List;

public class SampleUsers {
    public static List<User> getAllSampleUsers() {
        return List.of(
                /*/ ----- Customers ----- /*/
                new User(null, "هاشم ترابی", "h.torabi@example.com", "09130000001", "123456", UserRole.CUSTOMER),
                new User(null, "زهرا رضایی", "zahra.r@example.com", "09130000002", "123456", UserRole.CUSTOMER),
                new User(null, "علی احمدی", "ali.a@example.com", "09130000003", "123456", UserRole.CUSTOMER),
                new User(null, "سارا کریمی", "sara.k@example.com", "09130000004", "123456", UserRole.CUSTOMER),
                new User(null, "حسین قاسمی", "hossein.g@example.com", "09130000005", "123456", UserRole.CUSTOMER),
                new User(null, "ندا مرادی", "neda.m@example.com", "09130000006", "123456", UserRole.CUSTOMER),
                new User(null, "مهدی عباسی", "mahdi.a@example.com", "09130000007", "123456", UserRole.CUSTOMER),
                new User(null, "لیلا یوسفی", "leila.y@example.com", "09130000008", "123456", UserRole.CUSTOMER),
                new User(null, "پرویز سلطانی", "parviz.s@example.com", "09130000009", "123456", UserRole.CUSTOMER),
                new User(null, "مهسا شریفی", "mahsa.sh@example.com", "09130000010", "123456", UserRole.CUSTOMER),

                /*/ ----- Supports ----- /*/
                new User(null, "کارشناس پشتیبانی ۱", "support1@example.com", "09131000011", "123456", UserRole.SUPPORT),
                new User(null, "کارشناس پشتیبانی ۲", "support2@example.com", "09131000012", "123456", UserRole.SUPPORT),
                new User(null, "کارشناس پشتیبانی ۳", "support3@example.com", "09131000013", "123456", UserRole.SUPPORT),
                new User(null, "کارشناس پشتیبانی ۴", "support4@example.com", "09131000014", "123456", UserRole.SUPPORT),
                new User(null, "کارشناس پشتیبانی ۵", "support5@example.com", "09131000015", "123456", UserRole.SUPPORT)
        );
    }
}
