package com.pinyougou.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "user_test")
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;
   //@Column(name="username") 如果属性与数据库相同可以不加上
   private String username;// 用户姓名
   private String sex;// 性别
   private Date birthday;// 生日
   private String address;// 地址
   private String uuid2;
   //省略了get...,set....与toString()

   @Override
   public String toString() {
      return "User{" +
              "id=" + id +
              ", username='" + username + '\'' +
              ", sex='" + sex + '\'' +
              ", birthday=" + birthday +
              ", address='" + address + '\'' +
              ", uuid2='" + uuid2 + '\'' +
              '}';
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getSex() {
      return sex;
   }

   public void setSex(String sex) {
      this.sex = sex;
   }

   public Date getBirthday() {
      return birthday;
   }

   public void setBirthday(Date birthday) {
      this.birthday = birthday;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String getUuid2() {
      return uuid2;
   }

   public void setUuid2(String uuid2) {
      this.uuid2 = uuid2;
   }
}
