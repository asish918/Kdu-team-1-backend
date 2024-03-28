package com.kdu.ibebackend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RoomReviewDTO {
    @NotBlank(message = "User email must not be blank")
    @Email(message = "Invalid email format")
    @JsonProperty("user_email")
    private String userEmail;

    @NotNull
    @Min(0)
    @Max(5)
    @JsonProperty("rating")
    private Double rating;

    @NotNull
    @JsonProperty("review")
    private String review;

    @NotNull
    @Min(1)
    @Max(6)
    @JsonProperty("room_type_id")
    private Integer roomTypeId;
}
