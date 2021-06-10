package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ActorVO implements Serializable {

   // private static final long serialVersionUID = -7450246890907524430L;
   private String imgAddress;
   private String directorName;
   private String roleName;
}
