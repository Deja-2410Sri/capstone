package com.cropadvisory.platform.service;

import com.cropadvisory.platform.dto.market.MarketPriceResponse;
import com.cropadvisory.platform.model.entity.MarketPrice;
import com.cropadvisory.platform.repository.MarketPriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for market price information retrieval.
 */
@Service
public class MarketPriceService {

    private static final Logger log = LoggerFactory.getLogger(MarketPriceService.class);

    private final MarketPriceRepository marketPriceRepository;

    public MarketPriceService(MarketPriceRepository marketPriceRepository) {
        this.marketPriceRepository = marketPriceRepository;
    }

    public List<MarketPriceResponse> getAllMarketPrices() {
        return marketPriceRepository.findAllByOrderByRecordedDateDesc()
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<MarketPriceResponse> getMarketPricesByCropId(Long cropId) {
        return marketPriceRepository.findByCropIdOrderByRecordedDateDesc(cropId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private MarketPriceResponse mapToResponse(MarketPrice m) {
        return MarketPriceResponse.builder()
                .id(m.getId()).cropId(m.getCrop().getId()).cropName(m.getCrop().getCropName())
                .marketName(m.getMarketName()).location(m.getLocation())
                .price(m.getPrice()).unit(m.getUnit())
                .recordedDate(m.getRecordedDate()).build();
    }
}
