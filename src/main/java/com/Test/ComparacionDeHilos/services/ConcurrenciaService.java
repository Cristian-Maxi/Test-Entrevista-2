package com.Test.ComparacionDeHilos.services;

import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ConcurrenciaService {

    private static final int NUM_TAREAS = 10_000;

    public String ejecutarConFixedThreadPool() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(NUM_TAREAS);
        Instant inicio = Instant.now();

        for (int i = 0; i < NUM_TAREAS; i++) {
            executor.submit(() -> {
                simularTareaIO();
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        Instant fin = Instant.now();
        long duracion = Duration.between(inicio, fin).toMillis();
        return "FixedThreadPool: " + duracion + " ms | Hilos activos: " + Thread.activeCount();
    }

    public String ejecutarConVirtualThreads() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(NUM_TAREAS);
        Instant inicio = Instant.now();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < NUM_TAREAS; i++) {
                executor.submit(() -> {
                    simularTareaIO();
                    latch.countDown();
                });
            }

            latch.await();
        }

        Instant fin = Instant.now();
        long duracion = Duration.between(inicio, fin).toMillis();
        return "VirtualThreads: " + duracion + " ms | Hilos activos: " + Thread.activeCount();
    }

    private void simularTareaIO() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
