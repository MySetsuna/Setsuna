package entity;

import java.io.Serializable;

/**
 * 操作结果封装对象
 * @author Steven
 *
 */
public class Result implements Serializable {
   //是否成功
    private boolean success;
    //操作返回消息
   private String message;
   public Result(boolean success, String message) {
      super();
      this.success = success;
      this.message = message;
   }
   //getter and setter.....

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
