package com.example.demo.endpoint.event.model;

import lombok.*;

import java.time.Duration;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class GrayscaleImageRequested extends PojaEvent{
    private Long id;
    private String bucketKey;
    private String fileName;
    private String email;

    @Override
    public Duration maxConsumerDuration() {
        return Duration.ofSeconds(45);
    }

    @Override
    public Duration maxConsumerBackoffBetweenRetries() {
        return Duration.ofSeconds(30);
    }
}
