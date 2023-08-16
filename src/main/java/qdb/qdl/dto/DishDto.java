package qdb.qdl.dto;
import lombok.Data;
import qdb.qdl.entity.Dish;
import qdb.qdl.entity.DishFlavor;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
