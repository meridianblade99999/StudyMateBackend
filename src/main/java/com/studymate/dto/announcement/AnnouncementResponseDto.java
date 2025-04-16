package com.studymate.dto.announcement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.studymate.dto.tag.TagResponseDto;
import lombok.Data;

@Data
public class AnnouncementResponseDto {

    private long id;
    private String title;

    @JsonInclude(Include.NON_NULL)
    private String description;

    @JsonProperty("bg_color")
    private String bgColor;

    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("user_name")
    private String name;

    @JsonInclude(Include.NON_NULL)
    private TagResponseDto tags[];

    @JsonInclude(Include.NON_NULL)
    private Boolean liked;

}
