package com.rest.api.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MultiResult<T> extends CommonResult {
    private List<T> list;
}
