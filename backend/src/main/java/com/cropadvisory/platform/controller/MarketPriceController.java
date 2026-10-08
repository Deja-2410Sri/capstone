package com.cropadvisory.platform.controller;

import com.cropadvisory.platform.dto.common.ApiResponse;
import com.cropadvisory.platform.dto.market.MarketPriceResponse;
import com.cropadvisory.platform.service.MarketPriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for market price endpoints.
 */
@RestController
@RequestMapping("/api/v1/market-prices")
public class MarketPriceController {

    private final MarketPriceService marketPriceService;

    public MarketPriceController(MarketPriceService marketPriceService) {
        this.marketPriceService = marketPriceService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MarketPriceResponse>>> getAllMarketPrices() {
        return ResponseEntity.ok(ApiResponse.success(marketPriceService.getAllMarketPrices()));
    }

    @GetMapping("/{cropId}")
    public ResponseEntity<ApiResponse<List<MarketPriceResponse>>> getMarketPricesByCropId(@PathVariable Long cropId) {
        return ResponseEntity.ok(ApiResponse.success(marketPriceService.getMarketPricesByCropId(cropId)));
    }
}
