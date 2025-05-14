package com.br.gasto_comum.document;

import jakarta.persistence.*;
import lombok.*;
import org.aspectj.bridge.Message;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Entity(name = "Document")
@Table(name = "document")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;
    private String name;
    @Column(name = "mime_type")
    private String mimeType;
    private long size;
    @Column(name = "hash", nullable = false, unique = true)
    private String hash;

    public static final int RADIX = 16;

    public void setHash() throws NoSuchAlgorithmException {
        String transformedName = this.name + this.mimeType + this.size + new Date().getTime();
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(transformedName.getBytes(StandardCharsets.UTF_8));
        this.hash = new BigInteger(1, messageDigest.digest()).toString(RADIX);
    }

}
