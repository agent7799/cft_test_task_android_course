package com.example.myapplication.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "numeric",
        "alpha2",
        "name",
        "emoji",
        "currency",
        "latitude",
        "longitude"
})
@Generated("jsonschema2pojo")
public class Country {

    @JsonProperty("numeric")
    private String numeric;
    @JsonProperty("alpha2")
    private String alpha2;
    @JsonProperty("name")
    private String name;
    @JsonProperty("emoji")
    private String emoji;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("latitude")
    private Integer latitude;
    @JsonProperty("longitude")
    private Integer longitude;
//    @JsonIgnore
//    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("numeric")
    public String getNumeric() {
        return numeric;
    }

    @JsonProperty("numeric")
    public void setNumeric(String numeric) {
        this.numeric = numeric;
    }

    @JsonProperty("alpha2")
    public String getAlpha2() {
        return alpha2;
    }

    @JsonProperty("alpha2")
    public void setAlpha2(String alpha2) {
        this.alpha2 = alpha2;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("emoji")
    public String getEmoji() {
        return emoji;
    }

    @JsonProperty("emoji")
    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("latitude")
    public Integer getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("longitude")
    public Integer getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

//    @JsonAnyGetter
//    public Map<String, Object> getAdditionalProperties() {
//        return this.additionalProperties;
//    }
//
//    @JsonAnySetter
//    public void setAdditionalProperty(String name, Object value) {
//        this.additionalProperties.put(name, value);
//    }

    @Override
    public String toString() {
        return "COUNTRY: " +'\n' +
                "numeric: " + numeric  +
                ", alpha2: " + alpha2 + '\n' +
                "name: " + name  +
                ", emoji='" + emoji + '\'' +
                ", currency=" + currency + '\n' +
                "latitude: " + latitude + ", longitude: " + longitude;
    }
}