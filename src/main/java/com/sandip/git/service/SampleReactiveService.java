package com.sandip.git.service;

import com.sandip.git.dto.HeaderDTO;
import com.sandip.git.dto.SampleRequestDTO;
import com.sandip.git.dto.SampleResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class SampleReactiveService {

    /**
     * Demonstrates a MONO workflow (Single asynchronous result).
     * Simulates non-blocking processing like a database query or REST call.
     */
    public Mono<SampleResponseDTO> processSingleTransaction(SampleRequestDTO request) {
        long startTime = System.currentTimeMillis();

        log.info("Starting reactive processing for msgId: {}", request.getHeader().getMsgId());

        // Simulate a non-blocking 150ms downstream latency
        return Mono.delay(Duration.ofMillis(150))
                .map(delay -> {
                    long duration = System.currentTimeMillis() - startTime;

                    return SampleResponseDTO.builder()
                            .header(HeaderDTO.builder()
                                    .msgId(request.getHeader().getMsgId())
                                    .channelId(request.getHeader().getChannelId())
                                    .timestamp(Instant.now().toString())
                                    .build())
                            .status("SUCCESS")
                            .transactionReference("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                            .responseMessage("Processed successfully in non-blocking thread")
                            .processingTimeMs(duration)
                            .build();
                })
                .doOnSuccess(res -> log.info("Completed processing for msgId: {} in {} ms",
                        res.getHeader().getMsgId(), res.getProcessingTimeMs()))
                .doOnError(err -> log.error("Error occurred while processing transaction", err));
    }

    /**
     * Demonstrates a FLUX workflow (Streaming 0 to N items asynchronously).
     * Yields a continuous stream of transaction status updates over time.
     */
    public Flux<SampleResponseDTO> streamTransactionStatuses(String channelId) {
        log.info("Initiating Flux stream for channelId: {}", channelId);

        // Generate 5 continuous events, emitting one every 500 milliseconds
        return Flux.range(1, 5)
                .delayElements(Duration.ofMillis(500))
                .map(sequence -> SampleResponseDTO.builder()
                        .header(HeaderDTO.builder()
                                .msgId("STREAM-MSG-" + sequence)
                                .channelId(channelId)
                                .timestamp(Instant.now().toString())
                                .build())
                        .status(sequence == 5 ? "COMPLETED" : "IN_PROGRESS")
                        .transactionReference("STREAM-REF-" + sequence)
                        .responseMessage("Streaming status packet " + sequence + " of 5")
                        .processingTimeMs(500L * sequence)
                        .build())
                .doOnComplete(() -> log.info("Successfully finished emitting Flux stream for channel: {}", channelId));
    }
}