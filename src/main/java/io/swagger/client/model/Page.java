/*
 * Metadata API
 * RESTful API to create/read/update/delete metadata.
 *
 * OpenAPI spec version: 3.0.0-SNAPSHOT
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.model;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Page
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-02-14T15:06:09.233Z[GMT]")
public class Page {
  @SerializedName("size")
  private BigDecimal size = null;

  @SerializedName("totalElements")
  private BigDecimal totalElements = null;

  @SerializedName("totalPages")
  private BigDecimal totalPages = null;

  @SerializedName("number")
  private BigDecimal number = null;

  public Page size(BigDecimal size) {
    this.size = size;
    return this;
  }

   /**
   * Get size
   * minimum: 0
   * @return size
  **/
  @Schema(required = true, description = "")
  public BigDecimal getSize() {
    return size;
  }

  public void setSize(BigDecimal size) {
    this.size = size;
  }

  public Page totalElements(BigDecimal totalElements) {
    this.totalElements = totalElements;
    return this;
  }

   /**
   * Get totalElements
   * minimum: 0
   * @return totalElements
  **/
  @Schema(required = true, description = "")
  public BigDecimal getTotalElements() {
    return totalElements;
  }

  public void setTotalElements(BigDecimal totalElements) {
    this.totalElements = totalElements;
  }

  public Page totalPages(BigDecimal totalPages) {
    this.totalPages = totalPages;
    return this;
  }

   /**
   * Get totalPages
   * minimum: 0
   * @return totalPages
  **/
  @Schema(required = true, description = "")
  public BigDecimal getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(BigDecimal totalPages) {
    this.totalPages = totalPages;
  }

  public Page number(BigDecimal number) {
    this.number = number;
    return this;
  }

   /**
   * Get number
   * minimum: 0
   * @return number
  **/
  @Schema(required = true, description = "")
  public BigDecimal getNumber() {
    return number;
  }

  public void setNumber(BigDecimal number) {
    this.number = number;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Page page = (Page) o;
    return Objects.equals(this.size, page.size) &&
        Objects.equals(this.totalElements, page.totalElements) &&
        Objects.equals(this.totalPages, page.totalPages) &&
        Objects.equals(this.number, page.number);
  }

  @Override
  public int hashCode() {
    return Objects.hash(size, totalElements, totalPages, number);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Page {\n");

    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    totalElements: ").append(toIndentedString(totalElements)).append("\n");
    sb.append("    totalPages: ").append(toIndentedString(totalPages)).append("\n");
    sb.append("    number: ").append(toIndentedString(number)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}