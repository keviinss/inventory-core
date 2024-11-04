package com.app.inventory.helpers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseJson<T extends Object> {

    private Number status_code = 200;
    private String messages = "Success";
    private T data = null;

}
