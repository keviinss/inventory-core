package com.app.inventory.helpers;

import java.util.HashMap;
import java.util.List;
import org.springframework.data.domain.Page;

public class ResponseMapping {

    public static HashMap<String, Object> Search(Page<?> data) {

        HashMap<String, Object> pages = new HashMap<>();
        pages.put("page", data.getNumber() + 1);
        pages.put("limit", data.getSize());
        pages.put("total_pages", data.getTotalPages());
        pages.put("total_items", data.getTotalElements());

        HashMap<String, Object> response = new HashMap<>();

        List<?> items = data.getContent();
        if (items.size() <= 0) {
            items = null;
        }
        response.put("items", items);
        response.put("pages", pages);

        return response;
    }

}
