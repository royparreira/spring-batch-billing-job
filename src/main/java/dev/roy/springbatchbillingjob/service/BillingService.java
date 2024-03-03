package dev.roy.springbatchbillingjob.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BillingService {
    public void processBill() {
        log.info("Processing billing job");
    }
}
