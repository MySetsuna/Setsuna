package entity;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装对象
 * @author Steven
 *
 */
public class PageResult<T> implements Serializable {
   private long total;//总记录数
   private List<T> rows;//当前页结果

   public PageResult() {
   }

   public PageResult(long total, List<T> rows) {
      super();
      this.total = total;
      this.rows = rows;
   }
   //省略了get..与set....


   public long getTotal() {
      return total;
   }

   public void setTotal(long total) {
      this.total = total;
   }

   public List<T> getRows() {
      return rows;
   }

   public void setRows(List<T> rows) {
      this.rows = rows;
   }
}
