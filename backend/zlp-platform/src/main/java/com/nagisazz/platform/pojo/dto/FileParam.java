package com.nagisazz.platform.pojo.dto;

import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FileParam {

    private Long fileId;

    private List<Long> fileIds;

    private String systemId;
}
