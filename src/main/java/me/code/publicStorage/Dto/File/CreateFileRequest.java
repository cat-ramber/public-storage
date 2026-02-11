package me.code.publicStorage.Dto.File;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateFileRequest {
    private String name;
    private String fileParentId;
    private String text;
}
