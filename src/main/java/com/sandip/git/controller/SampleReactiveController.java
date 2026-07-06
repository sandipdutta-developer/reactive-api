package com.sandip.git.controller;

import com.sandip.git.dto.SampleRequestDTO;
import com.sandip.git.dto.SampleResponseDTO;
import com.sandip.git.service.SampleReactiveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/reactive")
@RequiredArgsConstructor
@Slf4j
public class SampleReactiveController {

    private final SampleReactiveService reactiveService;

    /**
     * 1. MONO ENDPOINT (Standard Asynchronous POST)
     * Accepts a JSON payload wrapped in a Mono and returns a single Mono response.
     */
    @PostMapping(value = "/process",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<SampleResponseDTO>> processTransaction(
            @Valid @RequestBody Mono<SampleRequestDTO> requestMono,
            @RequestAttribute(value = "AUDIT_MSG_ID", required = false) String traceMsgId) {

        log.debug("Received request on reactive controller. Audit Trace ID: {}", traceMsgId);

        return requestMono
                .flatMap(reactiveService::processSingleTransaction)
                .map(responseDTO -> ResponseEntity.status(HttpStatus.OK).body(responseDTO))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    /**
     * 2. FLUX ENDPOINT (Server-Sent Events / Event Streaming)
     * Streams multiple response objects back to the client as they are generated over time.
     */
    @GetMapping(value = "/stream/{channelId}",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SampleResponseDTO> streamStatuses(@PathVariable String channelId) {
        log.debug("Received stream request for channel: {}", channelId);
        return reactiveService.streamTransactionStatuses(channelId);
    }
}