package com.example.FashionE_CommercewithVirtualTry_On.module.tryon.client;

import org.springframework.stereotype.Component;

/**
 * ArApiClient simulates calling an external AR / virtual try-on API.
 *
 * HOW TO WIRE A REAL SERVICE:
 *  1. Inject RestTemplate or WebClient.
 *  2. POST { userImageUrl, productImageUrl } to the AR provider endpoint.
 *  3. Return the result image URL from the response body.
 *
 * Providers you can plug in: Replicate (IDM-VTON), Fashn.ai, Revery.ai, etc.
 */
@Component
public class ArApiClient {

    /**
     * Calls the external AR API to overlay the product onto the user's photo.
     *
     * @param userImageUrl    publicly accessible URL of the user's photo
     * @param productImageUrl publicly accessible URL of the product image
     * @return URL of the generated try-on result image
     * @throws RuntimeException if the AR API call fails
     */
    public String performTryOn(String userImageUrl, String productImageUrl) {

        // ── STUB IMPLEMENTATION ────────────────────────────────────────────
        // Replace the lines below with a real HTTP call when you have an API key.
        //
        // Example (Replicate / IDM-VTON):
        //   Map<String, Object> body = Map.of(
        //       "version", "<model-version-hash>",
        //       "input",   Map.of("human_img", userImageUrl, "garm_img", productImageUrl)
        //   );
        //   ResponseEntity<Map> resp = restTemplate.postForEntity(
        //       "https://api.replicate.com/v1/predictions", body, Map.class,
        //       HttpHeaders with "Authorization: Token <your-key>"
        //   );
        //   return (String) ((Map<?,?>) resp.getBody().get("output")).get("output_image");
        // ──────────────────────────────────────────────────────────────────

        if (userImageUrl == null || userImageUrl.isBlank()) {
            throw new RuntimeException("AR API error: userImageUrl is required");
        }
        if (productImageUrl == null || productImageUrl.isBlank()) {
            throw new RuntimeException("AR API error: product has no image to overlay");
        }

        // Simulated result — in production this comes from the real API
        return "https://tryon-results.example.com/result_"
                + System.currentTimeMillis() + ".jpg";
    }
}