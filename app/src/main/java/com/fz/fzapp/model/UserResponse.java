package com.fz.fzapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Dibuat oleh : ignat
 * Tanggal : 24-Jul-17
 * HP/WA : 0857 7070 6 777
 */
public class UserResponse
{
  @SerializedName("UserID")
  @Expose
  private int userid;
  @SerializedName("Name")
  @Expose
  private String name;
  @SerializedName("Phone")
  @Expose
  private String phone;
  @SerializedName("lnkRoleID")
  @Expose
  private int lnkRoleID;
  @SerializedName("Brand")
  @Expose
  private String brand;
  @SerializedName("Type")
  @Expose
  private String type;
  @SerializedName("TimeTrackLocation")
  @Expose
  private long timeTrackLocation;

  public int getUserid() {
    return userid;
  }

  public void setUserid(int userid) {
    this.userid = userid;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getPhone()
  {
    return phone;
  }

  public void setPhone(String phone)
  {
    this.phone = phone;
  }

  public int getLnkRoleID()
  {
    return lnkRoleID;
  }

  public void setLnkRoleID(int lnkRoleID)
  {
    this.lnkRoleID = lnkRoleID;
  }

  public String getBrand()
  {
    return brand;
  }

  public void setBrand(String brand)
  {
    this.brand = brand;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public long getTimeTrackLocation() {
    return timeTrackLocation;
  }

  public void setTimeTrackLocation(long timeTrackLocation) {
    this.timeTrackLocation = timeTrackLocation;
  }
}
