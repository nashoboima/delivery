package ru.ddd.delivery.adapters.in.http.openapi.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Location
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-09T21:28:33.331041314+03:00[Europe/Moscow]", comments = "Generator version: 7.19.0")
public class Location {

  private Integer x;

  private Integer y;

  public Location() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Location(Integer x, Integer y) {
    this.x = x;
    this.y = y;
  }

  public Location x(Integer x) {
    this.x = x;
    return this;
  }

  /**
   * X
   * minimum: 0
   * @return x
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "x", description = "X", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("x")
  public Integer getX() {
    return x;
  }

  public void setX(Integer x) {
    this.x = x;
  }

  public Location y(Integer y) {
    this.y = y;
    return this;
  }

  /**
   * Y
   * minimum: 0
   * @return y
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "y", description = "Y", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("y")
  public Integer getY() {
    return y;
  }

  public void setY(Integer y) {
    this.y = y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Location location = (Location) o;
    return Objects.equals(this.x, location.x) &&
        Objects.equals(this.y, location.y);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Location {\n");
    sb.append("    x: ").append(toIndentedString(x)).append("\n");
    sb.append("    y: ").append(toIndentedString(y)).append("\n");
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

