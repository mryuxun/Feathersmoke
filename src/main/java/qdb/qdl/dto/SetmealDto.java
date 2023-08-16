package qdb.qdl.dto;

import qdb.qdl.entity.Setmeal;
import qdb.qdl.entity.SetmealDish;
import lombok.Data;
import qdb.qdl.entity.SetmealDish;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
