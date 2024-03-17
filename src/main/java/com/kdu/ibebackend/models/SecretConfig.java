package com.kdu.ibebackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SecretConfig {
    @JsonProperty("graphql.url")
    private String graphqlUrl;

    @JsonProperty("graphql.api.key")
    private String graphqlApiKey;

    @JsonProperty("currency.api.url")
    private String currencyApiUrl;

    @JsonProperty("currency.api.key")
    private String currencyApiKey;
}
