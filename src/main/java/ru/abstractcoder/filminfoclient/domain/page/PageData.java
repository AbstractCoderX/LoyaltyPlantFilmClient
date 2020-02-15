package ru.abstractcoder.filminfoclient.domain.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class PageData<T> {

    private int page;
    private int totalResults;
    private int totalPages;
    @JsonProperty("results")
    private List<T> values;

}
