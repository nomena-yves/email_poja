package com.example.demo.Services;

import static java.nio.file.Files.createTempFile;

import com.example.demo.endpoint.event.model.GrayscaleImageRequested;
import com.example.demo.file.bucket.BucketComponent;
import com.example.demo.mail.Email;
import com.example.demo.mail.Mailer;
import jakarta.mail.internet.InternetAddress;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GrayscaleImageRequestedService implements Consumer<GrayscaleImageRequested> {
  private final BucketComponent bucketComponent;
  private final Mailer mailer;

  @SneakyThrows
  @Override
  public void accept(GrayscaleImageRequested requested) {
    // 1. Récupération de l'image originale depuis S3
    var originalFile = bucketComponent.download(requested.getBucketKey());

    // 2. Conversion en noir et blanc
    var suffix = requested.getFileName().substring(requested.getFileName().lastIndexOf('.') + 1);
    var grayscaleFile = createTempFile("grayscale-" + requested.getId(), "." + suffix).toFile();
    convertToGrayscale(originalFile, grayscaleFile, suffix);

    // 3. Upload de la version noir et blanc dans S3
    var grayscaleKey = "grayscale/" + requested.getId() + "." + suffix;
    bucketComponent.upload(grayscaleFile, grayscaleKey);

    // 4. Génération du lien pré-signé S3
    var presignedUrl = bucketComponent.presign(grayscaleKey, Duration.ofMinutes(60)).toString();

    // 5. Envoi de l'email avec le lien
    var recipient = new InternetAddress(requested.getEmail());
    var email =
        new Email(
            recipient,
            List.of(),
            List.of(),
            "Votre image en noir et blanc est prête",
            "Voici le lien vers votre image convertie : " + presignedUrl,
            List.of());

    mailer.accept(email);
  }

  private void convertToGrayscale(File source, File target, String format) throws Exception {
    var original = ImageIO.read(source);
    var grayscale =
        new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
    var g = grayscale.getGraphics();
    g.drawImage(original, 0, 0, null);
    g.dispose();
    ImageIO.write(grayscale, format, target);
  }
}
