package com.example.FashionE_CommercewithVirtualTry_On.module.tryon.entity;

public enum TryOnStatus {
    PENDING,      // Session created, waiting for AR processing
    COMPLETED,    // Result image successfully generated
    FAILED        // AR API returned an error
}