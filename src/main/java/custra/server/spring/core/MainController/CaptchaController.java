package custra.server.spring.core.MainController;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;

@RestController
@RequestMapping("/Custra/captcha")
public class CaptchaController {

    @GetMapping("/image")
    public ResponseEntity<byte[]> getCaptcha(HttpSession session) throws Exception {
        int width = 150;
        int height = 50;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        Random random = new Random();
        switch (random.nextInt(5)) {
            case 0:
                g.setColor(Color.LIGHT_GRAY);
                break;
            case 1:
                g.setColor(Color.PINK);
                break;
            case 2:
                g.setColor(Color.MAGENTA);
                break;
            case 3:
                g.setColor(Color.GREEN);
                break;
            case 4:
                g.setColor(Color.RED);
                break;
        }
        g.fillRect(0, 0, width, height);

        String captchaText = String.valueOf(random.nextInt(87654 - 1) + 12345);
        session.setAttribute("captcha", captchaText);

        g.setFont(new Font("Arial", Font.ITALIC, 30));
        g.setColor(Color.BLUE);
        g.drawString(captchaText, 30, 35);
        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return ResponseEntity.ok().headers(headers).body(baos.toByteArray());
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyCaptcha(@RequestParam String input, HttpSession session) {
        String captcha = (String) session.getAttribute("captcha");

        if (captcha == null) return ResponseEntity.badRequest().body("کپچا منقضی شده است.");
        if (captcha.equals(input)) return ResponseEntity.ok("کپچا درست است ✅");
        return ResponseEntity.status(400).body("کپچا نادرست است ❌");
    }
}