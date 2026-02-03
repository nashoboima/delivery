package ru.ddd.delivery.adapters.in.http.openapi.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CreateCourierResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-09T21:28:33.331041314+03:00[Europe/Moscow]", comments = "Generator version: 7.19.0")
public class CreateCourierResponse {

  private @Nullable UUID courierId;

  public CreateCourierResponse courierId(@Nullable UUID courierId) {
    this.courierId = courierId;
    return this;
  }

  /**
   * Get courierId
   * @return courierId
   */
  @Valid 
  @Schema(name = "courierId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("courierId")
  public @Nullable UUID getCourierId() {
    return courierId;
  }

  public void setCourierId(@Nullable UUID courierId) {
    this.courierId = courierId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateCourierResponse createCourierResponse = (CreateCourierResponse) o;
    return Objects.equals(this.courierId, createCourierResponse.courierId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(courierId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateCourierResponse {\n");
    sb.append("    courierId: ").append(toIndentedString(courierId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(@Nullable Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

