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
 * NewCourier
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-09T21:28:33.331041314+03:00[Europe/Moscow]", comments = "Generator version: 7.19.0")
public class NewCourier {

  private String name;

  private Integer speed;

  public NewCourier() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NewCourier(String name, Integer speed) {
    this.name = name;
    this.speed = speed;
  }

  public NewCourier name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Имя
   * @return name
   */
  @NotNull @Size(min = 1) 
  @Schema(name = "name", description = "Имя", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public NewCourier speed(Integer speed) {
    this.speed = speed;
    return this;
  }

  /**
   * Скорость
   * minimum: 1
   * @return speed
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "speed", description = "Скорость", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("speed")
  public Integer getSpeed() {
    return speed;
  }

  public void setSpeed(Integer speed) {
    this.speed = speed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NewCourier newCourier = (NewCourier) o;
    return Objects.equals(this.name, newCourier.name) &&
        Objects.equals(this.speed, newCourier.speed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, speed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NewCourier {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    speed: ").append(toIndentedString(speed)).append("\n");
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

