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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * AttributeCollection
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-02-14T15:06:09.233Z[GMT]")
public class AttributeCollection {
  @SerializedName("links")
  private Links links = null;

  @SerializedName("items")
  private List<Attribute> items = null;

  @SerializedName("page")
  private Page page = null;

  public AttributeCollection links(Links links) {
    this.links = links;
    return this;
  }

   /**
   * Get links
   * @return links
  **/
  @Schema(description = "")
  public Links getLinks() {
    return links;
  }

  public void setLinks(Links links) {
    this.links = links;
  }

  public AttributeCollection items(List<Attribute> items) {
    this.items = items;
    return this;
  }

  public AttributeCollection addItemsItem(Attribute itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<Attribute>();
    }
    this.items.add(itemsItem);
    return this;
  }

   /**
   * Get items
   * @return items
  **/
  @Schema(description = "")
  public List<Attribute> getItems() {
    return items;
  }

  public void setItems(List<Attribute> items) {
    this.items = items;
  }

  public AttributeCollection page(Page page) {
    this.page = page;
    return this;
  }

   /**
   * Get page
   * @return page
  **/
  @Schema(description = "")
  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttributeCollection attributeCollection = (AttributeCollection) o;
    return Objects.equals(this.links, attributeCollection.links) &&
        Objects.equals(this.items, attributeCollection.items) &&
        Objects.equals(this.page, attributeCollection.page);
  }

  @Override
  public int hashCode() {
    return Objects.hash(links, items, page);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttributeCollection {\n");

    sb.append("    links: ").append(toIndentedString(links)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
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