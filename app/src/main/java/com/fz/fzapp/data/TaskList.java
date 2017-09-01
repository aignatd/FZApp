package com.fz.fzapp.data;

/**
 * Dibuat oleh : ignat
 * Tanggal : 25-Jul-17
 * HP/WA : 0857 7070 6 777
 */
public class TaskList
{
  private int UserID;
  private int RoleID;
  private String SyncDate;
  private String SyncTime;

  public int getUserID() {
    return UserID;
  }

  public void setUserID(int userID) {
    UserID = userID;
  }

  public int getRoleID()
  {
    return RoleID;
  }

  public void setRoleID(int roleID)
  {
    RoleID = roleID;
  }

  public String getSyncDate() {
    return SyncDate;
  }

  public void setSyncDate(String syncDate) {
    SyncDate = syncDate;
  }

  public String getSyncTime() {
    return SyncTime;
  }

  public void setSyncTime(String syncTime) {
    SyncTime = syncTime;
  }
}
