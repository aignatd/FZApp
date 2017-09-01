package com.fz.fzapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Dibuat oleh : ignat
 * Tanggal : 25-Jul-17
 * HP/WA : 0857 7070 6 777
 */
public class TaskListResponse
{
  @SerializedName("TaskID")
  @Expose
  private Integer taskID;
  @SerializedName("Description")
  @Expose
  private String description;
  @SerializedName("DisplayName")
  @Expose
  private String displayName;
  @SerializedName("EstimateDate")
  @Expose
  private String estimateDate;
  @SerializedName("EstimateTime")
  @Expose
  private String estimateTime;
  @SerializedName("ActualDate")
  @Expose
  private String actualdate;
  @SerializedName("ActualTime")
  @Expose
  private String actualtime;
  @SerializedName("StatusTask")
  @Expose
  private Integer statustask;

  public Integer getTaskID() {
    return taskID;
  }

  public void setTaskID(Integer taskID) {
    this.taskID = taskID;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getEstimateTime() {
    return estimateTime;
  }

  public void setEstimateTime(String estimateTime) {
    this.estimateTime = estimateTime;
  }

  public String getEstimateDate() {
    return estimateDate;
  }

  public void setEstimateDate(String estimateDate) {
    this.estimateDate = estimateDate;
  }

  public String getActualdate()
  {
    return actualdate;
  }

  public void setActualdate(String actualdate)
  {
    this.actualdate = actualdate;
  }

  public String getActualtime()
  {
    return actualtime;
  }

  public void setActualtime(String actualtime)
  {
    this.actualtime = actualtime;
  }

  public Integer getStatustask()
  {
    return statustask;
  }

  public void setStatustask(Integer statustask)
  {
    this.statustask = statustask;
  }
}
